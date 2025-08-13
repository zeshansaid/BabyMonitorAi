package io.getstream.webrtc.sample.compose.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onCancel: () -> Unit) {
  val colors = MaterialTheme.colorScheme
  val isDark = isSystemInDarkTheme()
  val context = LocalContext.current

  var selectedEmoji by remember { mutableStateOf<String?>(null) }
  var feedbackText by remember { mutableStateOf("") }
  var allowFollowUp by remember { mutableStateOf(true) }

  val emojis = listOf("😡", "😢", "😐", "😂", "😍")

  Scaffold(
    containerColor = colors.background,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Give Feedback",
            color = colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = colors.background
        )
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(colors.background)
        .padding(paddingValues)
        .padding(24.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      // Question about editing tool
      Text(
        text = "What do you think of the editing tool?",
        color = colors.onBackground,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )

      // Emoji Selection
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        emojis.forEach { emoji ->
          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(
                if (selectedEmoji == emoji) colors.primary.copy(alpha = 0.3f)
                else Color.Transparent
              )
              .border(
                width = if (selectedEmoji == emoji) 2.dp else 0.dp,
                color = colors.primary,
                shape = CircleShape
              )
              .clickable {
                selectedEmoji = emoji
                // Add emoji to the message text
                feedbackText = if (feedbackText.isEmpty()) {
                  emoji
                } else {
                  "$feedbackText $emoji"
                }
              },
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = emoji,
              fontSize = 24.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Feedback text input
      Text(
        text = "Do you have any thoughts you'd like to share?",
        color = colors.onBackground,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )

      OutlinedTextField(
        value = feedbackText,
        onValueChange = { feedbackText = it },
        modifier = Modifier
          .fillMaxWidth()
          .height(120.dp),
        placeholder = {
          Text(
            text = "Type your feedback here...",
            color = if (isDark) Color.Gray else Color.DarkGray
          )
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = colors.primary,
          unfocusedBorderColor = colors.onBackground.copy(alpha = 0.5f),
          focusedTextColor = colors.onBackground,
          unfocusedTextColor = colors.onBackground,
          cursorColor = colors.primary
        ),
        shape = RoundedCornerShape(8.dp),
        maxLines = 5,
        textStyle = LocalTextStyle.current.copy(
          textAlign = TextAlign.Start,
          color = colors.onBackground
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Follow up question
      Text(
        text = "May we follow you up on your feedback?",
        color = colors.onBackground,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { allowFollowUp = true }
        ) {
          RadioButton(
            selected = allowFollowUp,
            onClick = { allowFollowUp = true },
            colors = RadioButtonDefaults.colors(
              selectedColor = colors.primary,
              unselectedColor = colors.onBackground.copy(alpha = 0.5f)
            )
          )
          Text(
            text = "Yes",
            color = colors.onBackground,
            fontSize = 14.sp
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { allowFollowUp = false }
        ) {
          RadioButton(
            selected = !allowFollowUp,
            onClick = { allowFollowUp = false },
            colors = RadioButtonDefaults.colors(
              selectedColor = colors.primary,
              unselectedColor = colors.onBackground.copy(alpha = 0.5f)
            )
          )
          Text(
            text = "No",
            color = colors.onBackground,
            fontSize = 14.sp
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // Action buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Button(
          onClick = {
            sendFeedbackEmail(
              context = context,
              selectedEmoji = selectedEmoji,
              feedbackText = feedbackText,
              allowFollowUp = allowFollowUp
            )
          },
          modifier = Modifier.weight(1f),
          colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "Send",
            color = colors.onPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        }

        OutlinedButton(
          onClick = onCancel,
          modifier = Modifier.weight(1f),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp
          ),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = colors.onBackground
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "Cancel",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

private fun sendFeedbackEmail(
  context: Context,
  selectedEmoji: String?,
  feedbackText: String,
  allowFollowUp: Boolean
) {
  val emailBody = buildString {
    appendLine("User Feedback for AI Infant Monitor")
    appendLine("=====================================")
    appendLine()

    if (selectedEmoji != null) {
      appendLine("Rating: $selectedEmoji")
      appendLine()
    }

    if (feedbackText.isNotEmpty()) {
      appendLine("Feedback:")
      appendLine(feedbackText)
      appendLine()
    }

    appendLine("Follow-up allowed: ${if (allowFollowUp) "Yes" else "No"}")
    appendLine()
    appendLine("Sent from AI Infant Monitor App")
  }

  // Try to open Gmail directly first
  val gmailIntent = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    setPackage("com.google.android.gm") // Gmail package name
    putExtra(Intent.EXTRA_EMAIL, arrayOf("zeshan.z33.z33@gmail.com"))
    putExtra(Intent.EXTRA_SUBJECT, "AI Infant Monitor - User Feedback")
    putExtra(Intent.EXTRA_TEXT, emailBody)
  }

  try {
    // Try Gmail first
    context.startActivity(gmailIntent)
  } catch (e: Exception) {
    // If Gmail is not available, fall back to any email app
    val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_EMAIL, arrayOf("alishaakhtarr888@gmail.com"))
      putExtra(Intent.EXTRA_SUBJECT, "AI Infant Monitor - User Feedback")
      putExtra(Intent.EXTRA_TEXT, emailBody)
    }

    try {
      context.startActivity(Intent.createChooser(fallbackIntent, "Send feedback via..."))
    } catch (ex: Exception) {
      // Handle case where no email app is available
      // You might want to show a toast or dialog here
    }
  }
}