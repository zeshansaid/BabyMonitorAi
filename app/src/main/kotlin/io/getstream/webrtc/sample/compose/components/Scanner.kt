package io.getstream.webrtc.sample.compose.components


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.*
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions



//-------------------------------
// Scanner...
//-------------------------------
@Composable
fun Scanner(
  onScanned: (String) -> Unit,
  onCancelled: () -> Unit
) {
  val scanLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
    if (result.contents != null) {
      onScanned(result.contents)
    } else {
      onCancelled()
    }
  }


  val options = ScanOptions()
  options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
  options.setPrompt("Scan QR code")
  options.setCameraId(0)
  options.setBeepEnabled(true)
  options.setBarcodeImageEnabled(true)
  options.setOrientationLocked(false)

  LaunchedEffect(Unit) {
    scanLauncher.launch(options)
  }




}



// To use call this
// Scanner { result ->
//  Log.d(AppUtils.TAG, "Scanned  text: $result")
//}

