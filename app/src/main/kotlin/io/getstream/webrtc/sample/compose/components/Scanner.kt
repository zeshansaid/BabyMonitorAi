package io.getstream.webrtc.sample.compose.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import io.getstream.webrtc.sample.compose.AppUtils


//-------------------------------
// Scanner...
//-------------------------------
@Composable
fun Scanner(
  onScanned: (String) -> Unit
) {
  val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
    if (result.contents != null) {
      onScanned(result.contents)
    }
  }


  val options = ScanOptions()
  options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
  options.setPrompt("Scan QR code")
  options.setCameraId(0) // Use a specific camera of the device
  options.setBeepEnabled(true)
  options.setBarcodeImageEnabled(true)
  options.setOrientationLocked(false)
  //launched safely after composition
  LaunchedEffect(Unit) {
    scanLauncher.launch(options)
  }
}




// To use call this
// Scanner { result ->
//  Log.d(AppUtils.TAG, "Scanned  text: $result")
//}

