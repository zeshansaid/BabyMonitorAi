package io.getstream.webrtc.sample.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.sample.compose.R

@Composable
fun SettingRow(
  title: String,
  trailingIcon: Painter,
  onClick: () -> Unit
) {
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
        .background(colorResource(R.color.card_color))
        .padding(horizontal = 16.dp)
        .clickable { onClick() }
    ) {
      Text(
        text = title,
        color = colorResource(R.color.white),
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.weight(1f))
      Icon(
        modifier = Modifier.size(20.dp),
        painter = trailingIcon,
        contentDescription = null,
        tint = colorResource(R.color.white)
      )
    }
  }
}



