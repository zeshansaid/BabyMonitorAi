package io.getstream.webrtc.sample.compose.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.SettingRow
import io.getstream.webrtc.sample.compose.components.WebPageView


@Composable
fun SettingScreen() {

  var webUrlToShow by remember { mutableStateOf<String?>(null) }

  if (webUrlToShow != null) {
    BackHandler {
      webUrlToShow = null // Go back to settings
    }
    // Fullscreen WebView
    WebPageView(url = webUrlToShow!!)
  } else {
    Box(
      modifier = Modifier
        .background(Color(0xFF20242A))
        .fillMaxSize(),
    ) {

      Column {
        Spacer(
          modifier = Modifier.height(48.dp)
        )

//----------------------------------------------------------//
//----------------------------------------------------------//
//----------------------------------------------------------/
        Text(
          modifier = Modifier.padding(
            16.dp
          ),
          fontWeight = FontWeight.ExtraBold,
          fontSize = 20.sp,
          text = "Help Center",
          color = colorResource(R.color.white),
          style = MaterialTheme.typography.titleMedium
        )
        Column {

          SettingRow(
            title = "Frequently Asked Questions",
            trailingIcon = painterResource(id = R.drawable.svg_right),
            onClick = {
              webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page"
            }
          )
          SettingRow(
            title = "Chat with Us",
            trailingIcon = painterResource(id = R.drawable.svg_right),
            onClick = {
              webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page"
            }
          )

        }

//----------------------------------------------------------//
//----------------------------------------------------------//
//----------------------------------------------------------//

        Text(
          modifier = Modifier.padding(
            16.dp
          ),
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          text = "About",
          color = colorResource(R.color.white),
          style = MaterialTheme.typography.titleMedium
        )
        Column {

          SettingRow(
            title = "Rate & Review AI Infant Monitor",
            trailingIcon = painterResource(id = R.drawable.svg_right),
            onClick = {
              webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page"
            }
          )
          SettingRow(
            title = "Terms of Services",
            trailingIcon = painterResource(id = R.drawable.svg_right),
            onClick = {
              webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page"
            }
          )
          SettingRow(
            title = "Privacy Policy",
            trailingIcon = painterResource(id = R.drawable.svg_right),
            onClick = {
              webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page"
            }
          )

        }

      }

    }


  }


}

