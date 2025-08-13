package io.getstream.webrtc.sample.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyActivityScreen(
  modifier: Modifier = Modifier,
  title: String = "Whoops!",
  description: String = "We couldn't find any baby activity. Start monitoring to see sleep, cries and other events here.",

) {
  val colors = MaterialTheme.colorScheme

  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 32.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      // Large subtle icon
      Icon(
        imageVector = Icons.Default.HourglassEmpty,
        contentDescription = null,
        modifier = Modifier.size(96.dp),
        tint = colors.primary
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Title
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp
        ),
        color = colors.onBackground,
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Description
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = colors.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 8.dp),
        lineHeight = 20.sp,
        textAlign = TextAlign.Center
      )


    }
  }
}




@Preview(showBackground = true)
@Composable
fun EmptyActivityScreenPreview() {
  MaterialTheme {
    EmptyActivityScreen()
  }
}