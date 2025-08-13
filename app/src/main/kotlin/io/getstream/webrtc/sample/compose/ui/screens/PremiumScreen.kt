package io.getstream.webrtc.sample.compose.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R

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
    }
  ) { innerPadding ->
    Column(
      Modifier
        .padding(innerPadding)
        .safeContentPadding()
        .padding(16.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(12.dp))
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
            )
          )
          .padding(24.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {

          // Premium Icon
          Icon(
            painter = painterResource(id = R.drawable.ic_start), // replace with your icon
            contentDescription = "Premium",
            tint = Color.Yellow,
            modifier = Modifier.size(80.dp)
          )

          Spacer(modifier = Modifier.height(24.dp))

          // Title
          Text(
            text = "Premium Features Coming Soon",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White
            ),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Description
          Text(
            text = "Get ready for exclusive features, enhanced experience, and more perks.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f)),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(32.dp))

          // Fancy Placeholder Button
          Button(
            onClick = { /* Later: navigate to purchase flow */ },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.Yellow
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(50.dp)
          ) {
            Text(
              text = "Stay Tuned",
              color = Color(0xFF2575FC),
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

    }

  }

}
