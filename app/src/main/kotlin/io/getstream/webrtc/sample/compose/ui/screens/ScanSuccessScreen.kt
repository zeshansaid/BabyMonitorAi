package io.getstream.webrtc.sample.compose.ui.screens

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.Gray0
import io.getstream.webrtc.sample.compose.ui.theme.Gray300
import io.getstream.webrtc.sample.compose.ui.theme.LightColors
import io.getstream.webrtc.sample.compose.ui.theme.robotoFont

@Composable
fun ScanSuccessScreen(
  modifier: Modifier = Modifier,
  onDoneClick: () -> Unit
) {
  val isDark = isSystemInDarkTheme()
  val colors = if (isDark) DarkColors else LightColors

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(colorResource(R.color.app_color)),
    contentAlignment = Alignment.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.Center)
    ) {
      AnimatedPreloader(
        animationResId = R.raw.qr_sucess,
        modifier = Modifier.size(200.dp)
      )
      Text(
        text = "Scanned Successfully",
        color = Gray0,
        fontSize = 32.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W600,
        fontFamily = FontFamily.SansSerif,
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth()
      )
      Text(
        text = buildAnnotatedString {
          append("Click on ")
          withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
            append("Done")
          }
          append(" button below!")
        },
        color = Gray300,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.W600,
        fontFamily = FontFamily.SansSerif,
        modifier = Modifier.fillMaxWidth()
      )
    }

    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(start = 32.dp, end = 32.dp, bottom = 40.dp)
    ) {
      OutlinedButton(
        onClick = onDoneClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = colors.primary,
          contentColor = colors.onPrimary
        ),
      ) {
        Text(
          text = "Done",
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = robotoFont
        )
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}
