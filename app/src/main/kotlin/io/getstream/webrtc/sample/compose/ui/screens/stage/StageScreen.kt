package io.getstream.webrtc.sample.compose.ui.screens.stage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader
import io.getstream.webrtc.sample.compose.ui.theme.AppGreen
import io.getstream.webrtc.sample.compose.ui.theme.AppRed
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors

import io.getstream.webrtc.sample.compose.ui.theme.Gray800
import io.getstream.webrtc.sample.compose.ui.theme.LightColors
import io.getstream.webrtc.sample.compose.ui.theme.robotoFont
import io.getstream.webrtc.sample.compose.webrtc.WebRTCSessionState

@Composable
fun StageScreen(
  state: WebRTCSessionState, onJoinCall: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()

  ) {
    var enabledCall by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val colors = if (isDark) DarkColors else LightColors

    val text = when (state) {
      WebRTCSessionState.Offline -> {
        enabledCall = false
        stringResource(id = R.string.button_start_session)
      }

      WebRTCSessionState.Impossible -> {
        enabledCall = false
        stringResource(id = R.string.session_impossible)
      }

      WebRTCSessionState.Ready -> {
        enabledCall = true
        stringResource(id = R.string.session_ready)
      }

      WebRTCSessionState.Creating -> {
        enabledCall = true
        stringResource(id = R.string.session_creating)
      }

      WebRTCSessionState.Active -> {
        enabledCall = false
        stringResource(id = R.string.session_active)
      }
    }


    Box(
      modifier = Modifier
        .fillMaxSize()
        ,
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Top 50% — Animated Preloader
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Bottom
        )
        {
          AnimatedPreloader(
            animationResId = R.raw.connecting,
            modifier = Modifier
              .width(300.dp).height(200.dp)
          )

          Text(
            text = "Initializing Secure Connection",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFont,
            color = colors.onBackground
          )
          Text(
            text = "This will only take a moment",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = robotoFont,
            color = colors.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
          )

        }

        // Bottom 50% — Button
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 40.dp),
          verticalArrangement = Arrangement.Bottom
        )
        {
          Button(
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            enabled = enabledCall,
            onClick = { onJoinCall.invoke() },
            colors = ButtonDefaults.buttonColors(
              containerColor = colors.primary,
              contentColor = colors.onPrimary,
              disabledContainerColor = AppRed,
              disabledContentColor = colorResource(R.color.white)
            )
          ) {
            Text(
              text = text,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = robotoFont
            )
          }

          Spacer(modifier = Modifier.height(32.dp))
        }
      }
    }


  }
}
