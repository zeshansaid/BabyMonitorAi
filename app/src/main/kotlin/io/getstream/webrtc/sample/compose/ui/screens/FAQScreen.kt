package io.getstream.webrtc.sample.compose.ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.ui.theme.DarkColors
import io.getstream.webrtc.sample.compose.ui.theme.LightColors

data class FAQItem(
  val question: String,
  val answer: String,
  val category: String = ""
)

data class FAQCategory(
  val categoryName: String,
  val questions: List<FAQItem>
)

@Composable
fun FAQScreen(onBackPressed: () -> Unit) {
  val isDark = isSystemInDarkTheme()
  val colors = if (isDark) DarkColors else LightColors

  val faqCategories = getFAQCategories()
  var expandedItems by remember { mutableStateOf(setOf<String>()) }
  val bgColor = if (isDark) Color(0xFF20242A) else Color(0xFFF1F2F5)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(colors.background)
  ) {
    Spacer(modifier = Modifier.height(48.dp))

    Text(
      modifier = Modifier.padding(16.dp),
      fontWeight = FontWeight.ExtraBold,
      fontSize = 20.sp,
      text = "Frequently Asked Questions",
      color = colors.onBackground
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      faqCategories.forEachIndexed { categoryIndex, category ->
        // Add extra spacing before category (except for first one)
        if (categoryIndex > 0) {
          item {
            Spacer(modifier = Modifier.height(32.dp))
          }
        } else {
          item {
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

        // Category header
        item {
          Text(
            text = category.categoryName,
            color = colors.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
          )
        }

        // Questions in this category
        category.questions.forEachIndexed { index, faq ->
          item {
            val itemKey = "${category.categoryName}_$index"
            val isExpanded = expandedItems.contains(itemKey)

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp) // Increased vertical padding
                .background(
                  if (isDark) Color(0xFF20242A) else Color(0xFFF1F2F5),
                  shape = RoundedCornerShape(8.dp) // Slightly more rounded corners
                )
                .clickable {
                  expandedItems = if (isExpanded) {
                    expandedItems - itemKey
                  } else {
                    expandedItems + itemKey
                  }
                }
                .padding(horizontal = 16.dp, vertical = 14.dp) // Increased padding
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = faq.question,
                  color = colors.onBackground,
                  style = MaterialTheme.typography.titleMedium,
                  textAlign = TextAlign.Start,
                  modifier = Modifier.weight(1f) // Added weight for better text wrapping
                )
                Spacer(modifier = Modifier.width(12.dp)) // Added horizontal space
                Icon(
                  painter = painterResource(id = R.drawable.plus_ic),
                  contentDescription = if (isExpanded) "Collapse" else "Expand",
                  tint = colors.onBackground,
                  modifier = Modifier.size(20.dp)
                )
              }

              AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
              ) {
                Column {
                  Spacer(modifier = Modifier.height(16.dp)) // Increased spacing
                  Divider(
                    color = colors.onBackground.copy(alpha = 0.2f),
                    thickness = 1.dp
                  )
                  Spacer(modifier = Modifier.height(16.dp)) // Increased spacing
                  Text(
                    text = faq.answer,
                    color = colors.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp, // Increased line height for better readability
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start

                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

private fun getFAQCategories(): List<FAQCategory> {
  return listOf(
    FAQCategory(
      categoryName = "General App Questions",
      questions = listOf(
        FAQItem(
          question = "What is this app and how does it work?",
          answer = "This app is a smart baby monitor that uses your phone to stream live audio and video. It uses AI to detect baby cries and alerts parents in real-time over Wi-Fi."
        ),
        FAQItem(
          question = "Do I need two devices to use this app?",
          answer = "Yes, you need two devices: one as the baby station (near the baby) and one as the parent monitor."
        ),
        FAQItem(
          question = "Does the app work without the internet?",
          answer = "The app works over a local Wi-Fi network, so no internet is needed unless you're using it remotely (future feature)."
        ),
        FAQItem(
          question = "Is the app safe and secure for my baby?",
          answer = "Yes, the app uses secure peer-to-peer WebRTC connections, and no data is stored or uploaded online."
        ),
        FAQItem(
          question = "Can I use this app on both Android and iOS?",
          answer = "Currently, the app is available for Android (iOS coming soon)."
        )
      )
    ),
    FAQCategory(
      categoryName = "Voice Classification / Cry Detection",
      questions = listOf(
        FAQItem(
          question = "How does the app detect if my baby is crying?",
          answer = "The app uses an on-device AI model that classifies sounds (like crying, silence, or noise) in real-time and sends alerts if crying is detected."
        ),
        FAQItem(
          question = "Is the AI model accurate?",
          answer = "The model is trained on real baby sounds and offers high accuracy in typical home environments. However, false positives may occur with loud noises."
        ),
        FAQItem(
          question = "Does it work when the baby is not crying loudly?",
          answer = "It detects even soft cries, depending on microphone sensitivity and background noise."
        ),
        FAQItem(
          question = "Can I customize what sounds trigger alerts?",
          answer = "In the current version, only baby crying triggers alerts. Customization may be added in future updates."
        )
      )
    ),
    FAQCategory(
      categoryName = "Connectivity & Devices",
      questions = listOf(
        FAQItem(
          question = "Do both devices need to be on the same Wi-Fi?",
          answer = "Yes. Both devices must be connected to the same local network for real-time streaming and detection to work reliably."
        ),
        FAQItem(
          question = "Can I monitor my baby from another room or floor?",
          answer = "Yes, as long as both devices are on the same Wi-Fi, you can monitor from anywhere within the range."
        ),
        FAQItem(
          question = "Does the app keep working in the background?",
          answer = "Yes. The app continues to stream and detect audio even if the parent device screen is off."
        ),
        FAQItem(
          question = "Can I connect more than one parent device?",
          answer = "Not in the current version. Multi-parent streaming support may be added in the future."
        )
      )
    ),
    FAQCategory(
      categoryName = "Alerts and Notifications",
      questions = listOf(
        FAQItem(
          question = "How will I be alerted if my baby is crying?",
          answer = "The parent device receives a notification, vibration, or optional alarm sound when crying is detected."
        ),
        FAQItem(
          question = "Can I adjust the alert sensitivity?",
          answer = "This feature is under development. In the current version, detection uses a fixed threshold optimized for typical scenarios."
        ),
        FAQItem(
          question = "Do alerts work if my phone is locked?",
          answer = "Yes, alerts work even when your screen is off or app is in the background."
        )
      )
    ),
    FAQCategory(
      categoryName = "Performance and Battery",
      questions = listOf(
        FAQItem(
          question = "Does the app use a lot of battery?",
          answer = "It is optimized for performance, but streaming and AI processing do consume battery. Keep devices plugged in for long-term use."
        ),
        FAQItem(
          question = "Can I turn off video and just use audio detection?",
          answer = "Yes, you can disable video streaming and use only audio monitoring to save battery."
        )
      )
    ),
    FAQCategory(
      categoryName = "Setup and Usage",
      questions = listOf(
        FAQItem(
          question = "How do I set up the baby and parent devices?",
          answer = "Simply open the app on both devices, scan the QR code shown on the baby device, and they will connect automatically over Wi-Fi."
        ),
        FAQItem(
          question = "Can I record audio or video?",
          answer = "Currently, the app does not store any recordings. Future versions may support secure local recording."
        )
      )
    )
  )
}