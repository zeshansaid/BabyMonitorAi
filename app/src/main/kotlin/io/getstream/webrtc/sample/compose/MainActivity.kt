package io.getstream.webrtc.sample.compose

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mediapipe.tasks.audio.core.RunningMode
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader
import io.getstream.webrtc.sample.compose.components.Scanner

import io.getstream.webrtc.sample.compose.db.CategoryViewModel
import io.getstream.webrtc.sample.compose.db.CategoryViewModelFactory
import io.getstream.webrtc.sample.compose.maincontent.QrScreenContent
import io.getstream.webrtc.sample.compose.ui.screens.ScanSuccessScreen
import io.getstream.webrtc.sample.compose.ui.screens.ViewerSessionSetupScreen
import io.getstream.webrtc.sample.compose.ui.screens.stage.StageScreen
import io.getstream.webrtc.sample.compose.ui.screens.video.VideoCallScreen
import io.getstream.webrtc.sample.compose.ui.theme.AppGreen
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.Gray0
import io.getstream.webrtc.sample.compose.ui.theme.Gray300
import io.getstream.webrtc.sample.compose.ui.theme.LightColors
import io.getstream.webrtc.sample.compose.ui.theme.WebrtcSampleComposeTheme
import io.getstream.webrtc.sample.compose.ui.theme.robotoFont
import io.getstream.webrtc.sample.compose.voiceclassification.AudioClassifierHelper
import io.getstream.webrtc.sample.compose.voiceclassification.CategoryDTO
import io.getstream.webrtc.sample.compose.webrtc.SignalingClient
import io.getstream.webrtc.sample.compose.webrtc.SignalingCommand
import io.getstream.webrtc.sample.compose.webrtc.peer.StreamPeerConnectionFactory
import io.getstream.webrtc.sample.compose.webrtc.sessions.WebRtcSessionManagerImpl
import io.getstream.webrtc.sample.compose.webrtc.sessions.LocalWebRtcSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity(), AudioClassifierHelper.ClassifierListener {
  private lateinit var audioClassifierHelper: AudioClassifierHelper

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)


    /*
    * get the selected Role either STREAMER or VIEWER from previous activity
    * */
    val initialRole = when (intent.getStringExtra("ROLE")) {
      "STREAMER" -> AppUtils.Role.STREAMER
      "VIEWER" -> AppUtils.Role.VIEWER
      else -> AppUtils.Role.STREAMER
    }

    /*
    * if STREAMER:
    * Create an 32 ROOM ID:
    * */
    val randomRoomId =
      if (initialRole == AppUtils.Role.STREAMER) AppUtils.generate32BitRandomKey() else ""


    /*
    * viewmodel for db
    * */
    val categoryViewModel: CategoryViewModel by viewModels {
      CategoryViewModelFactory(this)
    }


    setContent {
      WebrtcSampleComposeTheme {
        val isDark = isSystemInDarkTheme()
        val colors = if (isDark) DarkColors else LightColors
        var roomId by remember { mutableStateOf(randomRoomId) }
        var onCallScreen by remember { mutableStateOf(false) }
        var isSessionInitialized by remember { mutableStateOf(false) }
        var isCallStarted by remember { mutableStateOf(false) }
        val role by remember { mutableStateOf(initialRole) }
        val receivedCategories = remember { mutableStateOf<List<CategoryDTO>>(emptyList()) }


        /*
        * ask for permission on audio for both STREAMER and  VIEWER
        * */
        requestPermissions(
          arrayOf(Manifest.permission.RECORD_AUDIO), 0
        )


        /*
        *  1. check role
        *  2.ask camera permission
        *  3. Show QR CODE
        *  4. Create Session onDoneClicked
        * */

        if (role == AppUtils.Role.STREAMER && !isCallStarted) {

          if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
          ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
          }

          QrScreenContent(
            roomId = roomId,
            onDoneClicked = {
              if (!isCallStarted) {

                if (roomId.isNotEmpty() && !isSessionInitialized) {

                  requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)

                  AppUtils.sessionManager = WebRtcSessionManagerImpl(
                    context = this@MainActivity,
                    signalingClient = SignalingClient(roomId),
                    peerConnectionFactory = StreamPeerConnectionFactory(this@MainActivity),
                    role = role
                  )

                  isSessionInitialized = true
                  isCallStarted = true
                }
              }
            }
          )
        }

        /*
        * 1. check role
        * 2. Show Scanner
        * 3. SHOW SUCCESS SCANNED SCREEN
        * 4. START SESSION
        * */

        if (role == AppUtils.Role.VIEWER && !isCallStarted && !isSessionInitialized) {
          ViewerSessionSetupScreen(
            onSessionReady = { scannedRoomId ->
              AppUtils.sessionManager = WebRtcSessionManagerImpl(
                context = this@MainActivity,
                signalingClient = SignalingClient(scannedRoomId),
                peerConnectionFactory = StreamPeerConnectionFactory(this@MainActivity),
                role = role
              )

              isSessionInitialized = true
              isCallStarted = true
            },
            onCancelled = {
              finish()
            }
          )
        }


        /*
        * just for testing messaging on WEBRTC
        * */
        if (isSessionInitialized && role == AppUtils.Role.VIEWER) {

          var message by remember { mutableStateOf("Hello from $role") }
          AppUtils.sessionManager?.signalingClient?.sendCommand(
            SignalingCommand.MESSAGE,
            message
          )
        }


        /*
        * Receive baby activities from BABY STATION FROM VOICE CLASSIFICATION
        * */
        LaunchedEffect(AppUtils.sessionManager) {

//          if (role == AppUtils.Role.STREAMER) {
//            AppUtils.sessionManager?.signalingClient?.chatMessages?.collect { message ->
//              Log.d(AppUtils.TAG, "Received message from viewer: $message")
//              // You can add a Snack bar or a Composable list to show this in UI
//            }
//          }

          if (role == AppUtils.Role.VIEWER) {
            AppUtils.sessionManager?.signalingClient?.chatMessages?.collect { result ->

              Log.d(AppUtils.TAG, "Received message:$result")

              try {
                // Try to parse the entire message as a JSON array of CategoryDTO
                val type = object : TypeToken<List<CategoryDTO>>() {}.type
                val receivedList: List<CategoryDTO> = Gson().fromJson(result, type)

                // If successful, update the state to trigger recomposition
                receivedCategories.value = receivedList
                categoryViewModel.insertCategoryDTOs(receivedList)
                Log.d(AppUtils.TAG, "Parsed CategoryDTO list: $receivedList")

              } catch (e: Exception) {
                Log.e(AppUtils.TAG, "Failed to parse category list: ${e.message}")
              }
            }
          }
        }

//        ............................................................................................
        // --- Screen switching ---
        if (isSessionInitialized && AppUtils.sessionManager != null) {
          CompositionLocalProvider(LocalWebRtcSessionManager provides AppUtils.sessionManager!!) {
            val state by AppUtils.sessionManager!!.signalingClient.sessionStateFlow.collectAsState()
            if (!onCallScreen) {
              StageScreen(state = state) { onCallScreen = true }
            } else {
              if (role == AppUtils.Role.STREAMER) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                  != PackageManager.PERMISSION_GRANTED
                ) {
                  requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0)
                } else {
                  initClassifier()
                }

              }
              Box(modifier = Modifier.fillMaxSize()) {
                VideoCallScreen(role = role)
              }
            }
          }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()

    if (::audioClassifierHelper.isInitialized) {
      audioClassifierHelper.stopAudioClassification()
    }
  }

  override fun onError(error: String) {
    Log.d(AppUtils.TAG, "onError: $error")
  }

  override fun onResult(resultBundle: AudioClassifierHelper.ResultBundle) {
    val categories = resultBundle.results.firstOrNull()?.classificationResults()
      ?.firstOrNull()
      ?.classifications()
      ?.firstOrNull()
      ?.categories() ?: emptyList()

//    val maxResults = 10
//    if (results.size + categories.size > maxResults) {
//      results.removeRange(0, results.size + categories.size - maxResults)
//    }
//    results.addAll(categories)

//    AppUtils.sessionManager?.signalingClient?.sendCommand(
//      SignalingCommand.MESSAGE,
//      categories.toString()
//    )
    Log.d(AppUtils.TAG, "onResult in streamerr: $categories")
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val currentTime = formatter.format(Date()).lowercase()

    val dtoList = categories.map {
      CategoryDTO(
        score = it.score(),
        index = it.index(),
        categoryName = it.categoryName(),
        displayName = it.displayName(),
        timestamp = currentTime
      )
    }

    val json = Gson().toJson(dtoList)

    AppUtils.sessionManager?.signalingClient?.sendCommand(
      SignalingCommand.MESSAGE,
      json
    )


  }

  private fun initClassifier() {
    lifecycleScope.launch {
      withContext(Dispatchers.Default) {
        audioClassifierHelper = AudioClassifierHelper(
          context = this@MainActivity,
          classificationThreshold = 0.2f,
          overlap = 1,
          numOfResults = 3,
          runningMode = RunningMode.AUDIO_STREAM,
          listener = this@MainActivity
        )
      }
    }

  }


}


