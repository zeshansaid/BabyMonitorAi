package io.getstream.webrtc.sample.compose.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
  onBackClick: () -> Unit
) {

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("PREMIUM") },
        modifier = Modifier.statusBarsPadding()
      )
    },
    bottomBar = {
      BottomAppBar(
        modifier = Modifier.navigationBarsPadding()
      ) {
        Text(
          "Navigation",
          modifier = Modifier.padding(16.dp),
          style = MaterialTheme.typography.labelLarge
        )
      }
    }
  ) { innerPadding ->
    Column(
      Modifier
        .padding(innerPadding)
        .safeContentPadding()
        .padding(16.dp)
    ) {

    }

  }

}
