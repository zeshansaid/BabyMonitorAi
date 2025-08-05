package io.getstream.webrtc.sample.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.R


//-------------------------------
// This will generate a QR from  a random string
//-------------------------------
@Composable
fun QrCode(roomId: String) {

   val qrCodeBitmap = remember {
    AppUtils.generateQRCode(roomId)

  }

  Column(
    modifier = Modifier
      .background(colorResource(id = R.color.app_color))
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Display the QR code
    Image(
      bitmap = qrCodeBitmap.asImageBitmap(),
      contentDescription = "QR Code",
      modifier = Modifier
        .size(220.dp)  // Adjust the size as needed
    )
  }
}
