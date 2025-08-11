package io.getstream.webrtc.sample.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.LightColors

@Composable
fun SettingRow(
  title: String,
  trailingIcon: Painter,
  onClick: () -> Unit
) {
  val isDark = isSystemInDarkTheme()

  val colors = if (isDark) DarkColors else LightColors

  val bgColor = if (isDark) Color(0xFF20242A) else Color(0xFFF1F2F5)
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(bgColor)
        .padding(horizontal = 16.dp)
        .clickable { onClick() }
    ) {
      Text(
        text = title,
         color = colors.onBackground,
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.weight(1f))
      Icon(
        modifier = Modifier.size(20.dp),
        painter = trailingIcon,
        contentDescription = null,
        tint =   colors.onBackground,
      )
    }
  }
}



