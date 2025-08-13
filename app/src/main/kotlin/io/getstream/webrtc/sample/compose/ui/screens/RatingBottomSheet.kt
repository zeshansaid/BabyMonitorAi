package io.getstream.webrtc.sample.compose.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingBottomSheet(
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val isDark = isSystemInDarkTheme()
  val colors = MaterialTheme.colorScheme
  var selectedRating by remember { mutableStateOf(0) }
  val bottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = bottomSheetState,
    // 🔹 Grey for dark theme, default for light
    containerColor = if (isDark) Color(0xFF484646) else colors.surface,
    contentColor = colors.onSurface,
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 16.dp) // 🔹 Added padding
          .width(40.dp)
          .height(4.dp)
          .background(
            color = colors.onSurface.copy(alpha = 0.3f),
            shape = RoundedCornerShape(2.dp)
          )
      )
    }
  ) {
    RatingSheetContent(
      selectedRating = selectedRating,
      onRatingSelected = { rating -> selectedRating = rating },
      onSubmit = {
        handleRatingSubmission(
          context = context,
          rating = selectedRating,
          onDismiss = onDismiss
        )
      },
      onRemindLater = onDismiss,
      colors = colors
    )
  }
}


@Composable
fun RatingSheetContent(
  selectedRating: Int,
  onRatingSelected: (Int) -> Unit,
  onSubmit: () -> Unit,
  onRemindLater: () -> Unit,
  colors: ColorScheme
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Title
    Text(
      text = "How are you liking AI Infant Monitor ?",
      color = colors.onSurface,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Subtitle
    Text(
      text = "Your feedback will help us improve your\nAI Infant Monitor experience.",
      color = colors.onSurface.copy(alpha = 0.7f),
      fontSize = 14.sp,
      textAlign = TextAlign.Center,
      lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(32.dp))

    // Star Rating
    Row(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      repeat(5) { index ->
        val starIndex = index + 1
        Icon(
          imageVector = if (starIndex <= selectedRating) {
            Icons.Filled.Star
          } else {
            Icons.Outlined.Star
          },
          contentDescription = "Star $starIndex",
          tint = if (starIndex <= selectedRating) colors.primary else colors.onSurface.copy(alpha = 0.5f),
          modifier = Modifier
            .size(36.dp)
            .clickable { onRatingSelected(starIndex) }
        )
      }
    }

    Spacer(modifier = Modifier.height(40.dp))

    // Submit Button
    Button(
      onClick = onSubmit,
      enabled = selectedRating > 0,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = colors.primary,
        disabledContainerColor = colors.onSurface.copy(alpha = 0.12f),
        disabledContentColor = colors.onSurface.copy(alpha = 0.38f)
      ),
      shape = RoundedCornerShape(24.dp)
    ) {
      Text(
        text = "Submit",
        color = colors.onPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Remind me later
    TextButton(
      onClick = onRemindLater
    ) {
      Text(
        text = "Remind me Later",
        color = colors.primary,
        fontSize = 14.sp
      )
    }

    Spacer(modifier = Modifier.height(32.dp))
  }
}

private fun handleRatingSubmission(
  context: Context,
  rating: Int,
  onDismiss: () -> Unit
) {
  when {
    rating >= 3 -> {
      // Open Play Store for 3, 4, 5 stars
      openPlayStore(context)
      onDismiss()
    }
    rating in 1..2 -> {
      // Show thank you toast for 1, 2 stars
      Toast.makeText(
        context,
        "Thanks for giving feedback",
        Toast.LENGTH_LONG
      ).show()
      onDismiss()
    }
    else -> {
      Toast.makeText(
        context,
        "Please select a rating",
        Toast.LENGTH_SHORT
      ).show()
    }
  }
}

private fun openPlayStore(context: Context) {
  try {
    // Replace with your actual app package name
    val packageName = context.packageName
    val playStoreIntent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("market://details?id=$packageName")
    )
    context.startActivity(playStoreIntent)
  } catch (e: Exception) {
    // Fallback to web version if Play Store app is not available
    val webIntent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
    )
    context.startActivity(webIntent)
  }
}