package io.getstream.webrtc.sample.compose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

data class MusicCategoryDrawable(
  val title: String,
  val subtitle: String,
  val iconRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
  onDismiss: () -> Unit,
  onPlay: () -> Unit
) {
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = Color.Transparent,
    dragHandle = null
  ) {
    MusicBottomSheetContent(
      onClose = onDismiss,
      onPlay = onPlay
    )
  }
}

@Composable
fun MusicBottomSheetContent(
  onClose: () -> Unit,
  onPlay: () -> Unit
) {
  // Using your existing drawable or a single music icon for all categories
  val musicCategories = listOf(
    MusicCategoryDrawable("Peaceful Piano Music", "Relaxing Piano Music", R.drawable.ic_music_list),
    MusicCategoryDrawable("Pop Music", "Arash", R.drawable.ic_music_list),
    MusicCategoryDrawable("Instrumental Guitar", "Ed Sheeran", R.drawable.ic_music_list),
    MusicCategoryDrawable("Party Music", "Miley Cyrus", R.drawable.ic_music_list),
    MusicCategoryDrawable("Meditation Music", "DJ Snake", R.drawable.ic_music_list)
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            Color(0xFF4A148C), // Deep Purple
            Color(0xFF7B1FA2), // Medium Purple
            Color(0xFF8E24AA)  // Light Purple
          )
        )
      )
      .padding(20.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      // Top indicator bar
      Box(
        modifier = Modifier
          .width(40.dp)
          .height(4.dp)
          .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
          .align(Alignment.CenterHorizontally)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Title and subtitle
      Text(
        text = "Soothing Sounds For Baby",
        color = Color.White,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "A Calming Mix Of Lullabies And Gentle Melodies To Help Your Baby Fall Asleep Peacefully.",
        color = Color.White.copy(alpha = 0.8f),
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        lineHeight = 20.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Music categories list
      musicCategories.forEach { category ->
        MusicCategoryItemDrawable(
          category = category,
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // Bottom section
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Play on child's device",
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          // Play button
          Button(
            onClick = onPlay,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFE91E63) // Pink color
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
              .width(120.dp)
              .height(50.dp)
          ) {
            Text(
              text = "Play",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium
            )
          }

          // Close button
          Button(
            onClick = onClose,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.White.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier
              .width(120.dp)
              .height(50.dp)
          ) {
            Text(
              text = "Close",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
fun MusicCategoryItemDrawable(
  category: MusicCategoryDrawable,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .clickable { /* Handle category selection */ }
      .padding(horizontal = 4.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Category icon
    Box(
      modifier = Modifier
        .size(50.dp)
        .background(
          Color.White.copy(alpha = 0.15f),
          RoundedCornerShape(12.dp)
        )
        .padding(12.dp),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        painter = painterResource(id = category.iconRes),
        contentDescription = category.title,
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.width(16.dp))

    // Category text
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = category.title,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = category.subtitle,
        color = Color.White.copy(alpha = 0.7f),
        fontSize = 14.sp
      )
    }

    // More options indicator
    Row {
      repeat(3) {
        Box(
          modifier = Modifier
            .size(4.dp)
            .background(Color.White.copy(alpha = 0.6f), CircleShape)
        )
        if (it < 2) Spacer(modifier = Modifier.width(4.dp))
      }
    }
  }
}