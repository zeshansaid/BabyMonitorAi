package io.getstream.webrtc.sample.compose.ui.screens.video

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip


import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.AppUtils.Companion.toggleBrightness
import io.getstream.webrtc.sample.compose.AppUtils.Companion.toggleTorch
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader
import io.getstream.webrtc.sample.compose.components.KeepScreenOn
import io.getstream.webrtc.sample.compose.ui.components.VideoRenderer
import io.getstream.webrtc.sample.compose.ui.theme.AppGreen
import io.getstream.webrtc.sample.compose.webrtc.SignalingClient
import io.getstream.webrtc.sample.compose.webrtc.SignalingCommand
import io.getstream.webrtc.sample.compose.webrtc.sessions.LocalWebRtcSessionManager
import io.github.crow_misia.libyuv.RowStride
import io.github.crow_misia.libyuv.Yuv.convertI420ToARGB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.webrtc.VideoFrame
import org.webrtc.VideoTrack
import java.nio.ByteBuffer

@SuppressLint("ContextCastToActivity", "DefaultLocale")
@Composable
fun VideoCallScreen(role: AppUtils.Role) {
  KeepScreenOn()
  val sessionManager = LocalWebRtcSessionManager.current
  val context = LocalContext.current
  var latestBitmap by remember { mutableStateOf<Bitmap?>(null) }
  var detectedObjects by remember { mutableStateOf<List<DetectedObject>>(emptyList()) }
  var frameCount by remember { mutableIntStateOf(0) }
  val scope = rememberCoroutineScope()
  var isTorchRequested by remember { mutableStateOf(false) }
  var isBrightnessHigh by remember { mutableStateOf(false) }

  LaunchedEffect(key1 = Unit) {
    sessionManager.onSessionScreenReady()
  }

  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    var parentSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }

    val remoteVideoTrackState by sessionManager.remoteVideoTrackFlow.collectAsState(null)
    val localVideoTrackState by sessionManager.localVideoTrackFlow.collectAsState(null)

    var videoTrack: VideoTrack?
    if (role == AppUtils.Role.VIEWER) {
      videoTrack = remoteVideoTrackState

/////////////////////////////////////////// 2. Start the service after track is available/////////////////////////////////////
//      val intent = Intent(context, StreamService::class.java)
//      ContextCompat.startForegroundService(context, intent)
    }
    else {
      videoTrack = localVideoTrackState
    }

    var callMediaState by remember { mutableStateOf(CallMediaState()) }

    if (videoTrack != null) {

      VideoRenderer(
        videoTrack = videoTrack,
        onFrameReceived = { frame ->
          if (role == AppUtils.Role.VIEWER) {
//            frameCount++
//            if (frameCount % 30 != 0) return@VideoRenderer
            val originalBuffer = frame.buffer
            originalBuffer.retain() // retain before coroutine touches it
            scope.launch(Dispatchers.IO) {
              try {
                val retainedFrame = VideoFrame(originalBuffer, frame.rotation, frame.timestampNs)

                val bitmap = convertVideoFrameToBitmap(retainedFrame)
                latestBitmap = bitmap
                retainedFrame.release()

                // Run ML Kit detection
                bitmap?.let { bmp ->
                  val inputImage = InputImage.fromBitmap(bmp, 0)

                  // [START create_local_model]
                  val localModel = LocalModel.Builder()
                    .setAssetFilePath("model_with_metadata.tflite")
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
                      detectedObjects = objects
                    }
                    .addOnFailureListener {
                      Log.e("MLKIT", "Detection failed", it)
                    }
                }

              } catch (e: Exception) {
                Log.e("FRAME", "Error converting frame to bitmap", e)
                originalBuffer.release()
              }
            }
          }
        },
        modifier = Modifier
          .fillMaxSize()
          .onSizeChanged { parentSize = it }
      )
      // Show detected labels
      if (detectedObjects.isNotEmpty()) {
        Column(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(16.dp)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          detectedObjects.forEach { obj ->
            val label = obj.labels.firstOrNull()?.text ?: "Unknown"
            val confidence = obj.labels.firstOrNull()?.confidence ?: 0f
            Card(
              modifier = Modifier.fillMaxWidth(0.6f),
              colors = CardDefaults.cardColors(
                containerColor = AppGreen
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
              Text(
                text = "$label (${String.format("%.2f", confidence)})",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }
    //if no video track available
    else {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        AnimatedPreloader(
          animationResId = R.raw.loading,
          modifier = Modifier.size(300.dp)
        )
      }
    }

    val activity = (LocalContext.current as? Activity)
    if (role == AppUtils.Role.STREAMER) {
      // Torch state
      var isTorchOn by remember { mutableStateOf(false) }
      // Message receiver for torch toggle
      LaunchedEffect(Unit) {
        AppUtils.sessionManager?.signalingClient?.setOnMessageReceivedListener(object :
          SignalingClient.OnMessageReceivedListener {
          override fun onMessageReceived(message: String) {
            when (message.lowercase()) {
              "torch_on" -> toggleTorch(context, true) { isTorchOn = true }
              "torch_off" -> toggleTorch(context, false) { isTorchOn = false }
              "brightness_high" -> sessionManager.flipCamera() //toggleBrightness(context, true) { isBrightnessHigh = true }
              "brightness_low" -> sessionManager.flipCamera() //toggleBrightness(context, false) { isBrightnessHigh = false }
              else -> Log.d("Streamer", "Unknown message: $message")
            }
          }
        })
      }


      // Main Video Call Controls
      VideoCallControls(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .clip(RoundedCornerShape(16.dp))
          .align(Alignment.BottomCenter),
        callMediaState = callMediaState,
        onCallAction = {
          when (it) {
            is CallAction.ToggleMicroPhone -> {
              val enabled = callMediaState.isMicrophoneEnabled.not()
              callMediaState = callMediaState.copy(isMicrophoneEnabled = enabled)
              sessionManager.enableMicrophone(enabled)
            }

            is CallAction.ToggleCamera -> {
              val enabled = callMediaState.isCameraEnabled.not()
              callMediaState = callMediaState.copy(isCameraEnabled = enabled)
              sessionManager.enableCamera(enabled)
            }

            CallAction.FlipCamera -> {
              sessionManager.flipCamera()
            }

            CallAction.LeaveCall -> {
              sessionManager.disconnect()
              activity?.finish()
            }
          }
        }
      )
    }

    else if (role == AppUtils.Role.VIEWER) {
      VideoControlsViewer (
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .clip(RoundedCornerShape(16.dp))
          .align(Alignment.BottomCenter),
        onToggleTorch = {
          isTorchRequested = !isTorchRequested
          val command = if (isTorchRequested) "torch_on" else "torch_off"

          AppUtils.sessionManager?.signalingClient?.sendCommand(
            SignalingCommand.MESSAGE,
            command
          )
        },
        onToggleBrightness = {
          isBrightnessHigh = !isBrightnessHigh
          val command = if (isBrightnessHigh) "brightness_high" else "brightness_low"

          AppUtils.sessionManager?.signalingClient?.sendCommand(
            SignalingCommand.MESSAGE,
            command
          )
        },
        onLeaveCall = {
          sessionManager.disconnectRemoteConnection()
          activity?.finish()
        }
      )
    }

  }
}

fun convertVideoFrameToBitmap(videoFrame: VideoFrame): Bitmap? {
  val buffer = videoFrame.buffer
  val i420Buffer = buffer.toI420() ?: return null // 👈 prevent NPE

  val width = i420Buffer.width
  val height = i420Buffer.height

  val dataY = i420Buffer.dataY
  val dataU = i420Buffer.dataU
  val dataV = i420Buffer.dataV
  val strideY = RowStride(i420Buffer.strideY)
  val strideU = RowStride(i420Buffer.strideU)
  val strideV = RowStride(i420Buffer.strideV)

  val argbStride = RowStride(width * 4)
  val argbBuffer = ByteBuffer.allocateDirect(argbStride.value * height)

  convertI420ToARGB(
    dataY, strideY, 0,
    dataU, strideU, 0,
    dataV, strideV, 0,
    argbBuffer, argbStride, 0,
    width, height
  )

  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  argbBuffer.rewind()
  bitmap.copyPixelsFromBuffer(argbBuffer)

  i420Buffer.release()

  return bitmap
}


