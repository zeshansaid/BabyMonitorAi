package io.getstream.webrtc.sample.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import io.getstream.webrtc.sample.compose.components.MainNavigationWithBottomBar
import io.getstream.webrtc.sample.compose.ui.screens.BabyMonitorWelcomeScreen
import io.getstream.webrtc.sample.compose.ui.screens.DeviceSetupScreen
import io.getstream.webrtc.sample.compose.ui.screens.InstallLinkScreen
import io.getstream.webrtc.sample.compose.ui.theme.BabyMonitorTheme


class StartActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BabyMonitorTheme {
        val context = LocalContext.current
        var currentScreen by remember { mutableStateOf("welcome") }

        when (currentScreen) {
          "welcome" -> BabyMonitorWelcomeScreen(
            skipClicked = false,
            onSkip = { currentScreen = "mainApp" },
            onStartTutorial = { currentScreen = "deviceSetup" }
          )

          "deviceSetup" -> DeviceSetupScreen(
            onContinue = { currentScreen = "install" }
          )

          "install" -> InstallLinkScreen(
            onContinue = { currentScreen = "mainApp" }
          )

          // Bottom nav screens are now wrapped inside this composable
          "mainApp" -> MainNavigationWithBottomBar(
            onBabyStationClick = {
              val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("ROLE", "STREAMER")
              }
              context.startActivity(intent)
            },
            onParentStationClick = {
              val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("ROLE", "VIEWER")
              }
              context.startActivity(intent)
            }
          )
        }
      }
    }
  }
}
