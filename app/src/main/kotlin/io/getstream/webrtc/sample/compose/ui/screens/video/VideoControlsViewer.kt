package io.getstream.webrtc.sample.compose.ui.screens.video


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.ui.screens.MusicBottomSheet

@Composable
fun VideoControlsViewer(
  modifier: Modifier,
  onToggleTorch: () -> Unit,
  onToggleBrightness: () -> Unit,
  onLeaveCall: () -> Unit
) {
  var torchOn by remember { mutableStateOf(false) }
  var brightnessHigh by remember { mutableStateOf(false) }
  var showMusicBottomSheet by remember { mutableStateOf(false) }


  val brightnessIcon = if (brightnessHigh) {
    R.drawable.flip_cam_round
  } else {
    R.drawable.flip_cam_round
  }
  val torchIcon = if (torchOn) {
    R.drawable.torch_on
  } else {
    R.drawable.torch_off
  }


  Row(
    modifier = modifier
      .background(colorResource(R.color.app_color))
      .padding(12.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    // Torch Toggle Button
    Image(
      painter = painterResource(id = torchIcon),
      contentDescription = "Toggle Torch",
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable {
          torchOn = !torchOn
          onToggleTorch()
        }
        .padding(10.dp),
    )

    // Brightness Toggle Button
    Image(
      painter = painterResource(id = brightnessIcon),
      contentDescription = "Toggle Brightness",
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable {
          brightnessHigh = !brightnessHigh
          onToggleBrightness()
        }
        .padding(10.dp)
    )

    // Music List Button
    Image(
      painter = painterResource(id = R.drawable.ic_baby_song),
      contentDescription = "baby music",
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable {
          showMusicBottomSheet = true
        }
        .padding(10.dp),
    )

    // End Call Button
    Image(
      painter = painterResource(id = R.drawable.end_session),
      contentDescription = "End Call",
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .clickable {
          onLeaveCall()
        }
        .padding(10.dp),
    )

    // Music Bottom Sheet
    if (showMusicBottomSheet) {
      MusicBottomSheet(
        onDismiss = { showMusicBottomSheet = false },
        onPlay = {
          // Handle play action
          showMusicBottomSheet = false
        }
      )
    }


  }
}

