package io.getstream.webrtc.sample.compose

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mediapipe.tasks.audio.core.RunningMode

import io.getstream.webrtc.sample.compose.db.CategoryViewModel
import io.getstream.webrtc.sample.compose.db.CategoryViewModelFactory
import io.getstream.webrtc.sample.compose.maincontent.QrScreenContent
import io.getstream.webrtc.sample.compose.services.AlertService
import io.getstream.webrtc.sample.compose.ui.screens.MusicPlayerSheet
import io.getstream.webrtc.sample.compose.ui.screens.ViewerSessionSetupScreen
import io.getstream.webrtc.sample.compose.ui.screens.stage.StageScreen
import io.getstream.webrtc.sample.compose.ui.screens.video.VideoCallScreen
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.LightColors
import io.getstream.webrtc.sample.compose.ui.theme.WebrtcSampleComposeTheme
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
        var showMusicBottomSheet by remember { mutableStateOf(false) }
        var bottomSheetMessage by remember { mutableStateOf("") }


        var role by remember { mutableStateOf(initialRole) }

        val receivedCategories = remember { mutableStateOf<List<CategoryDTO>>(emptyList()) }








        /*
        * ask for permission on audio for both STREAMER and  VIEWER
        * */
        requestPermissions(
          arrayOf(Manifest.permission.RECORD_AUDIO), 0
        )


        /*
        * Show Music Player Sheet
        * */
        //show  NoPremium BottomSheet
        if (showMusicBottomSheet) {
          MusicPlayerSheet(
            message = bottomSheetMessage,
            onDismiss = { showMusicBottomSheet = false },
            onSubscribeClick = {

            }
          )
        }


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

              val serviceIntent = Intent(this@MainActivity, AlertService::class.java).apply {
                putExtra("ROOM_ID", scannedRoomId)
              }

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
              } else {
                startService(serviceIntent)
              }



//              AppUtils.sessionManager = WebRtcSessionManagerImpl(
//                context = this@MainActivity,
//                signalingClient = SignalingClient(scannedRoomId),
//                peerConnectionFactory = StreamPeerConnectionFactory(this@MainActivity),
//                role = role
//              )

//              isSessionInitialized = true
//              isCallStarted = true
            },
            onCancelled = {
              finish()
            }
          )
        }


        //

        LaunchedEffect(Unit) {
          while (AppUtils.sessionManager == null) {
            kotlinx.coroutines.delay(200)
          }
          Log.d(AppUtils.TAG, "SessionManager is ready from service")
          isSessionInitialized = true
          isCallStarted = true
        }


        /*
        * just for testing messaging on WEBRTC
        * */
//        if (isSessionInitialized && role == AppUtils.Role.VIEWER) {
//
//          var message by remember { mutableStateOf("Hello from $role") }
//          AppUtils.sessionManager?.signalingClient?.sendCommand(
//            SignalingCommand.MESSAGE,
//            message
//          )
//        }


        /*
        * 1. Receive baby activities from BABY STATION FROM VOICE CLASSIFICATION
        *  2. Receive  messages  from Parent STATION TO PLAY MUSIC
        * */
        LaunchedEffect(AppUtils.sessionManager) {

          if (role == AppUtils.Role.STREAMER) {

            // We use these messages to play Music
            AppUtils.sessionManager?.signalingClient?.chatMessages?.collect { message ->

              Log.d(AppUtils.TAG, "Received message from viewer: $message")


              if (!showMusicBottomSheet) {
                bottomSheetMessage = message
                showMusicBottomSheet = true
              } else {
                // Close then re-open
                showMusicBottomSheet = false
                kotlinx.coroutines.delay(300) // wait for dismiss animation
                bottomSheetMessage = message
                showMusicBottomSheet = true
              }

            }
          }

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


    // Baby & Child related label indexes from YAMNet taxonomy
    val babyChildIndexes = setOf(4, 14, 20, 3, 5, 7, 13)


    val formatter = SimpleDateFormat("HH:mm dd MMM", Locale.getDefault())
    val currentTime = formatter.format(Date()) // Example: "12:32 25 Jun"

    // Filter categories by our interest
    val filteredCategories = categories.filter { it.index() in babyChildIndexes }

    // If nothing matches, don't send
    if (filteredCategories.isEmpty()) {
      Log.d(AppUtils.TAG, "No baby/child sounds detected.")
      return
    }

    val dtoList = filteredCategories.map {
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

    Log.d(AppUtils.TAG, "Filtered baby/child categories sent: $dtoList")


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


