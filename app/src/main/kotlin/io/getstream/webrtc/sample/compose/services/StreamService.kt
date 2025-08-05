package io.getstream.webrtc.sample.compose.services


import android.app.Service
import android.content.Intent

import android.os.IBinder
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.MainActivity
import io.getstream.webrtc.sample.compose.ui.screens.video.convertVideoFrameToBitmap
import io.getstream.webrtc.sample.compose.webrtc.sessions.LocalWebRtcSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.webrtc.VideoFrame
import org.webrtc.VideoSink
import org.webrtc.VideoTrack



class StreamService : Service() {

  val sessionManager = AppUtils.sessionManager

  private val TAG = "StreamServicee"
  private lateinit var notificationBuilder: NotificationCompat.Builder

  // Coroutine scope tied to service lifecycle
  private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate() {
    startOfferForegroundServices() // ensures notification is shown immediately

  }

  companion object {
    const val ACTION_STOP_SERVICE = "io.getstream.webrtc.sample.ACTION_STOP_SERVICE"
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    if (intent?.action == ACTION_STOP_SERVICE) {
      stopForeground(Service.STOP_FOREGROUND_REMOVE)
      serviceScope.cancel()
      stopSelf()
      return START_NOT_STICKY
    }


    serviceScope.launch {
      sessionManager?.remoteVideoTrackFlow?.collect { videoTrack ->
        // Add VideoSink to the VideoTrack
        videoTrack.addSink { frame ->

          val bitmap = convertVideoFrameToBitmap(frame)
          // Run ML Kit detection
          bitmap?.let { bmp ->
            val inputImage = InputImage.fromBitmap(bmp, 0)

            // [START create_local_model]
            val localModel = LocalModel.Builder()
              .setAssetFilePath("object_labeler.tflite")
              // or .setAbsoluteFilePath("absolute_file_path_to_tflite_model")
              .build()
            // [END create_local_model]


            // [START create_custom_options]
            // Live detection and tracking
            val options: CustomObjectDetectorOptions =
              CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(3)
                .build()


            val objectDetector = ObjectDetection.getClient(options)
            objectDetector.process(inputImage)
              .addOnSuccessListener { objects ->
                val labels = objects.flatMap { it.labels }.joinToString(", ") { it.text }

                // Update notification with detected labels
                notificationBuilder
                  .setContentText("Detected: $labels")
                  .setSilent(true)

                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1111, notificationBuilder.build())
              }
              .addOnFailureListener {
                Log.e("MLKIT", "Detection failed", it)
              }
          }
        }
      }
    }

    return START_STICKY
  }



  private suspend fun trackSeconds() {
    var secondsPassed = 0
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    while (true) {
//            Thread.sleep(1000)
      delay(1000) // Non-blocking delay
      secondsPassed++
      notificationBuilder
        .setContentText("Offer countdown: $secondsPassed seconds running...")
        .setSilent(true)
      notificationManager.notify(1111, notificationBuilder.build())
    }
  }
  @RequiresApi(Build.VERSION_CODES.O)
  fun startOfferForegroundServices() {
    notificationBuilder = getNotificationBuilder()
//    createNotificationChannel()
    startForeground(1111, notificationBuilder.build())

  }

  private fun getNotificationBuilder(): NotificationCompat.Builder {
    val stopIntent = Intent(this, StreamService::class.java).apply {
      action = ACTION_STOP_SERVICE
    }

    val stopPendingIntent = PendingIntent.getService(
      this, 1, stopIntent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder = NotificationCompat.Builder(this, "channel1")
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle("Baby Activities !")
      .setContentText("Here we will show baby activities")
      .setOngoing(true)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setContentIntent(getPendingIntent())
      .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
    return notificationBuilder
  }


  private fun getPendingIntent(): PendingIntent {

    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
      this, 0, intent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    return pendingIntent
  }

  override fun onDestroy() {
    super.onDestroy()
    serviceScope.cancel()

  }

  override fun onBind(p0: Intent?): IBinder? {
    return null
  }


  //  @RequiresApi(Build.VERSION_CODES.O)
//  fun createNotificationChannel(): NotificationChannel {
//    val channel = NotificationChannel(
//      "channel1", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT
//    )
//    val notificationManager =
//      ContextCompat.getSystemService(this, NotificationManager::class.java)
//    notificationManager?.createNotificationChannel(channel)
//    return channel
//
//  }

}




