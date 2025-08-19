package io.getstream.webrtc.sample.compose.services


import android.app.Service
import android.content.Intent

import android.os.IBinder
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Binder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.MainActivity
import io.getstream.webrtc.sample.compose.ui.screens.video.convertVideoFrameToBitmap
import io.getstream.webrtc.sample.compose.voiceclassification.CategoryDTO
import io.getstream.webrtc.sample.compose.webrtc.SignalingClient
import io.getstream.webrtc.sample.compose.webrtc.peer.StreamPeerConnectionFactory
import io.getstream.webrtc.sample.compose.webrtc.sessions.LocalWebRtcSessionManager
import io.getstream.webrtc.sample.compose.webrtc.sessions.WebRtcSessionManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.webrtc.VideoFrame
import org.webrtc.VideoSink
import org.webrtc.VideoTrack


class AlertService : Service() {


  private val binder = LocalBinder()
  private var roomId: String? = null
  private var sessionManager: WebRtcSessionManagerImpl? = null
  private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
  private lateinit var notificationBuilder: NotificationCompat.Builder


  companion object {

    const val ACTION_STOP_SERVICE = "io.getstream.webrtc.sample.ACTION_STOP_SERVICE"
    const val CHANNEL_ID = "channel1"
    const val NOTIFICATION_ID = 1111
  }

  inner class LocalBinder : Binder() {
    fun getService(): AlertService = this@AlertService
  }

  override fun onBind(intent: Intent?): IBinder = binder

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
  }


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

    if (intent?.action == ACTION_STOP_SERVICE) {
      stopWebRTCSession()
      stopForeground(STOP_FOREGROUND_REMOVE)
      serviceScope.cancel()
      stopSelf()

      return START_NOT_STICKY
    }

    roomId = intent?.getStringExtra("ROOM_ID")
    if (roomId.isNullOrEmpty()) {
      return START_NOT_STICKY
    }

    startOfferForegroundService(roomId!!)

    serviceScope.launch {
      sessionManager = WebRtcSessionManagerImpl(
        context = this@AlertService,
        signalingClient = SignalingClient(roomId!!),
        peerConnectionFactory = StreamPeerConnectionFactory(this@AlertService),
        role = AppUtils.Role.VIEWER
      )

      AppUtils.sessionManager = sessionManager // Keep it accessible to MainActivity

      sessionManager?.signalingClient?.chatMessages?.collect { message ->
        Log.d(AppUtils.TAG, "Alert from baby device: $message")
        try {
          // Parse JSON into CategoryDTO list
          val type = object : TypeToken<List<CategoryDTO>>() {}.type
          val receivedList: List<CategoryDTO> = Gson().fromJson(message, type)


          // Format with timestamp
          val displayText = receivedList.joinToString { dto ->
            "(${dto.timestamp}) ${dto.categoryName}"
          }

          // Update notification
          notificationBuilder
            .setContentText("Detected: $displayText")
            .setStyle(NotificationCompat.BigTextStyle().bigText(displayText))
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Pre-Oreo heads-up
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound, vibration, lights

          val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

          Log.d(AppUtils.TAG, "Parsed CategoryDTO list in service: $receivedList")
        } catch (e: Exception) {
          Log.e(AppUtils.TAG, "Failed to parse category list in service: ${e.message}")
        }
      }




    }

    return START_STICKY
  }


  private fun stopWebRTCSession() {
    // Close WebRTC connection here
    sessionManager?.disconnect() // or your actual cleanup logic
    sessionManager = null
  }



  override fun onDestroy() {
    super.onDestroy()
    serviceScope.cancel()

  }


  //...........................................................................

  private fun getNotificationBuilder(roomId: String): NotificationCompat.Builder {
    val stopIntent = Intent(this, AlertService::class.java).apply {
      action = ACTION_STOP_SERVICE
    }

    val stopPendingIntent = PendingIntent.getService(
      this, 1, stopIntent,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    ///pending intent code
//    val mainIntent = Intent(this, MainActivity::class.java).apply {
//      putExtra("ROOM_ID", roomId) // Pass room id back to activity
//      putExtra("ROLE", "viewer")
//      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    }
//
//    val mainPendingIntent = PendingIntent.getActivity(
//      this, 0, mainIntent,
//      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//    )

    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setContentTitle("Baby Activities!")
      .setContentText("Here we will show baby activities")
      .setOngoing(true)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      //.setContentIntent(mainPendingIntent)
      .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
  }
  private fun startOfferForegroundService(roomId: String) {
    notificationBuilder = getNotificationBuilder(roomId)
    startForeground(NOTIFICATION_ID, notificationBuilder.build())
  }
  private fun createNotificationChannel() {
    val channel = NotificationChannel(
      CHANNEL_ID,
      "Baby Monitor Alerts",
      NotificationManager.IMPORTANCE_DEFAULT
    )
//    val notificationManager = getSystemService(NotificationManager::class.java)
//    notificationManager.createNotificationChannel(channel)

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
    notificationManager?.createNotificationChannel(channel)
  }


}




