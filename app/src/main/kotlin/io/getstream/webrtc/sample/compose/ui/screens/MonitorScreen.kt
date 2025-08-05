package io.getstream.webrtc.sample.compose.ui.screens


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.MainActivity
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.QrCode
import io.getstream.webrtc.sample.compose.components.Scanner
import io.getstream.webrtc.sample.compose.ui.onboard.OnboardingScreen
import io.getstream.webrtc.sample.compose.ui.theme.robotoFont


@Composable
fun MonitorScreen() {

  val context = LocalContext.current
  var showScanner by remember { mutableStateOf(false) }
  var scannedText by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFF20242A)) // 👈 full screen background
  ) {
    Icon(
      painter = painterResource(R.drawable.ic_menu),
      contentDescription = "menu",
      modifier = Modifier.padding(16.dp),
      tint = Color.White
    )
    androidx.compose.material.Text(
      text = "Tips from AI Infant Monitor",
      style = MaterialTheme.typography.bodyLarge,
      color = Color.White,
      textAlign = TextAlign.Start,
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 8.dp)
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .weight(1.2f)
    ) {
      OnboardingScreen(
        modifier = Modifier
          .fillMaxSize(),
        AppUtils.screen_3
      )
    }


    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(0.8f)
        .background(Color(0xFF1B1F24)) // 👈 different color for bottom half
    ) {

      Image(
        painter = painterResource(id = R.drawable.bg_svg), // replace with your image resource
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit // fill and crop to cover the entire box
      )

      Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        // Content on top of the background
        androidx.compose.material.Text(
          text = "Pair AI Infant Monitor camera to this \n" +
            "account to view the live feed.",
          color = Color.White,
          textAlign = TextAlign.Center
        )

        Button(
          onClick = {
            val intent = Intent(context, MainActivity::class.java).apply {
              putExtra("ROLE", "VIEWER") // or Role.VIEWER.name based on logic
            }
            context.startActivity(intent)
          },
          colors = ButtonDefaults.buttonColors(Color.Transparent),

          modifier = Modifier
            .width(200.dp)
            .height(50.dp)
            .padding(top = 16.dp)
            .background(
              colorResource(id = R.color.white),
              shape = RoundedCornerShape(12.dp)
            )

        ) {
          androidx.compose.material.Text(
            "Start Monitoring",
            color = colorResource(id = R.color.app_color),
            fontWeight = FontWeight.Bold
          )
        }
      }


    }


  }



//  if (showScanner) {
//    Box(
//      modifier = Modifier
//        .fillMaxWidth()
//        .background(Color.Black)
//        .padding(16.dp),
//      contentAlignment = Alignment.Center
//    ) {
//
//      Scanner { result ->
//        scannedText = result
//      }
//
//      Spacer(modifier = Modifier.height(16.dp))
//      Text("Scanned Result: $scannedText")
//
//    }
//  }


}


//
//  var scannedText by remember { mutableStateOf("") }
//  var startScanner by remember { mutableStateOf(false) }
//
//  Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//    Button(onClick = { startScanner = true }) {
//      Text("Start Scanner")
//    }
//
//    if (startScanner) {
//      Scanner { result ->
//        scannedText = result
//        startScanner = false
//      }
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//    Text("Scanned Result: $scannedText")
//  }


