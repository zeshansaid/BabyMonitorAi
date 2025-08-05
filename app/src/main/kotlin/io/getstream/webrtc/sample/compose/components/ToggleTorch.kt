package io.getstream.webrtc.sample.compose.components

import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@Composable
fun ToggleTorch(isTorchOn: Boolean) {
  val context = LocalContext.current
  val torchMode = remember { mutableStateOf("Off") }

  LaunchedEffect(isTorchOn) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraID = cameraManager.cameraIdList.firstOrNull { id ->
      cameraManager.getCameraCharacteristics(id)
        .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    } ?: run {
      Toast.makeText(context, "No camera with flash found", Toast.LENGTH_SHORT).show()
      return@LaunchedEffect
    }

    try {
      cameraManager.setTorchMode(cameraID, isTorchOn)
      torchMode.value = if (isTorchOn) "On" else "Off"
      Toast.makeText(context, "Torch turned ${torchMode.value}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
      e.printStackTrace()
      Toast.makeText(context, "Failed to toggle torch", Toast.LENGTH_SHORT).show()
    }
  }
}
