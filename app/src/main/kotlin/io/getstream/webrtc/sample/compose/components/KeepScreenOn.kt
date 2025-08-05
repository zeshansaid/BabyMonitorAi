package io.getstream.webrtc.sample.compose.components

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext


//-------------------------------
// To Keep your screen on
//-------------------------------
@Composable
fun KeepScreenOn() {
  val context = LocalContext.current
  val activity = context as? Activity

  DisposableEffect(Unit) {
    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    onDispose {
      activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
  }
}
