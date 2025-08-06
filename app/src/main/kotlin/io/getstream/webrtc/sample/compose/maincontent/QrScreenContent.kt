package io.getstream.webrtc.sample.compose.maincontent

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.components.QrCode
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.LightColors

@Composable
fun QrScreenContent(
  roomId: String,
  onDoneClicked: () -> Unit
) {
  val isDark = isSystemInDarkTheme()
  val colors = if (isDark) DarkColors else LightColors

  Scaffold(
    containerColor = colors.background,
    bottomBar = {
      Column(
        modifier = Modifier
          .padding(horizontal = 32.dp, vertical = 20.dp)
      ) {
        Button(
          onClick = onDoneClicked,
          shape = RoundedCornerShape(50),
          colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
          ),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
        ) {
          Text(
            text = "Done",
            style = MaterialTheme.typography.labelLarge.copy(
              fontWeight = FontWeight.Bold
            )
          )
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
    ) {
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.3f)
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
      ) {
        QrCode(roomId)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
          text = buildAnnotatedString {
            append("Scan this QR on another mobile device\n and then click on ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
              append("Done")
            }
            append("\nbutton below!")
          },
          color = colors.onBackground,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.W900,
            fontSize = 20.sp
          ),
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}
