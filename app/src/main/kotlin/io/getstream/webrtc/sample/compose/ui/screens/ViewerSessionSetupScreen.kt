package io.getstream.webrtc.sample.compose.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.Scanner

@Composable
fun ViewerSessionSetupScreen(
  modifier: Modifier = Modifier,
  onSessionReady: (roomId: String) -> Unit,
  onCancelled: () -> Unit
) {
  var startScanner by remember { mutableStateOf(true) }
  var roomId by remember { mutableStateOf("") }

  if (startScanner) {
    BackHandler {
      onCancelled()
    }

    Box(
      modifier = modifier
        .fillMaxSize()
        .background(colorResource(R.color.app_color))
        .padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Scanner(
        onScanned = { result ->
          roomId = result
          startScanner = false
        },
        onCancelled = {
          onCancelled()
        }
      )
    }
  } else {
    ScanSuccessScreen(
      onDoneClick = {
        if (roomId.isNotEmpty()) {
          onSessionReady(roomId)
        }
      }
    )
  }
}
