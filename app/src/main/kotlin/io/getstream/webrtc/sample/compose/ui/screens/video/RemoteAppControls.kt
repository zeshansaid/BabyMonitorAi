package io.getstream.webrtc.sample.compose.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun RemoteAppControls(
  icons: List<Boolean>, // true = toggled, false = default
  onIconClick: (index: Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    icons.forEachIndexed { index, isToggled ->
      IconToggleButton(
        checked = isToggled,
        onCheckedChange = { onIconClick(index) }
      ) {
        Icon(
          imageVector = if (isToggled) Icons.Default.Favorite
          else Icons.Default.FavoriteBorder,
          contentDescription = "Toggle Icon $index",
          tint = if (isToggled) Color.Red else Color.Gray,
          modifier = Modifier
            .size(48.dp)
            .background(
              color = Color.LightGray,
              shape = CircleShape
            )
            .padding(12.dp)
        )
      }
    }
  }
}






@Preview(showBackground = true)
@Composable
fun PreviewIconRowScreen() {

}
