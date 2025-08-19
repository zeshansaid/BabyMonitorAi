package io.getstream.webrtc.sample.compose.ui.screens


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.SettingRow
import io.getstream.webrtc.sample.compose.components.WebPageView
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.LightColors



@Composable
fun SettingScreen() {
  val isDark = isSystemInDarkTheme()
  val colors = if (isDark) DarkColors else LightColors

  val logoRes = if (isDark) {
    R.drawable.logocompany // Dark mode logo
  } else {
    R.drawable.companylogo_black // Light mode logo
  }


  val context = LocalContext.current
  val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
  val appVersion = packageInfo.versionName ?: "0.0.0"

  var webUrlToShow by remember { mutableStateOf<String?>(null) }
  var showFeedbackScreen by remember { mutableStateOf(false) }
  var showRatingBottomSheet by remember { mutableStateOf(false) }
  var showFAQScreen by remember { mutableStateOf(false) }

  if (showRatingBottomSheet) {
    RatingBottomSheet(
      onDismiss = { showRatingBottomSheet = false }
    )
  }

  if (showFAQScreen) {
    FAQScreen(onBackPressed = { showFAQScreen = false })
  } else if (showFeedbackScreen) {
    FeedbackScreen(onCancel = { showFeedbackScreen = false })
  } else if (webUrlToShow != null) {
    BackHandler {
      webUrlToShow = null
    }
    WebPageView(url = webUrlToShow!!)
  } else {
    Box(
      modifier = Modifier
        .background(colors.background)
        .fillMaxSize()
    )
    {
      Column(
        modifier = Modifier.fillMaxSize()
        .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Help Center Section
        Text(
          modifier = Modifier.padding(16.dp),
          fontWeight = FontWeight.ExtraBold,
          fontSize = 20.sp,
          text = "Help Center",
          color = colors.onBackground,
          style = MaterialTheme.typography.titleMedium,
          textAlign = TextAlign.Start
        )
        Column {
          SettingRow(
            title = "Frequently Asked Questions",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { showFAQScreen = true }
          )
          SettingRow(
            title = "Feedback & Suggestions",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { showFeedbackScreen = true }

          )
          SettingRow(
            title = "Share App Link",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page" }
          )
        }

        // About Section
        Text(
          modifier = Modifier.padding(16.dp),
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          text = "About",
          color = colors.onBackground,
          style = MaterialTheme.typography.titleMedium,
          textAlign = TextAlign.Start
        )
        Column {
          SettingRow(
            title = "Rate & Review AI Infant Monitor",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { showRatingBottomSheet = true }
          )
          SettingRow(
            title = "Terms of Services",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page" }
          )
          SettingRow(
            title = "Privacy Policy",
            trailingIcon = painterResource(id = R.drawable.plus_ic),
            onClick = { webUrlToShow = "https://en.wikipedia.org/wiki/Main_Page" }
          )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer Section
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(vertical = 16.dp)
        ) {
          // Social Icons Row
          Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(painterResource(id = R.drawable.ic_facebook), contentDescription = "Facebook", tint = colors.onBackground)
            Icon(painterResource(id = R.drawable.ic_instagram), contentDescription = "Instagram", tint = colors.onBackground)
            Icon(painterResource(id = R.drawable.ic_linkedin), contentDescription = "LinkedIn", tint = colors.onBackground)
            Icon(painterResource(id = R.drawable.ic_telegram), contentDescription = "Telegram", tint = colors.onBackground)
            Icon(painterResource(id = R.drawable.ic_youtube), contentDescription = "YouTube", tint = colors.onBackground)
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "© Copyright 2025",
              fontSize = 12.sp,
              color = colors.onBackground,
              style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(4.dp)) // small gap between text and image
            Image(
              painter = painterResource(id = logoRes),
              contentDescription = "My Image",
              modifier = Modifier.height(10.dp),
              contentScale = ContentScale.Crop
            )
          }




          Text(
            text = "v$appVersion",
            fontSize = 12.sp,
            color = colors.onBackground,
            style = MaterialTheme.typography.titleMedium
          )
        }
      }
    }
  }
}