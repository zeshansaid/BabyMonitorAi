package io.getstream.webrtc.sample.compose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage


@Composable
fun ActivityScreen(
  onBackClick: () -> Unit
) {
  var selectedEvent by remember { mutableStateOf<TimelineEvent?>(null) }

  val timelineEvents = listOf(
    TimelineEvent("8:32", "Jackob Eating","20 min", icon = Icons.Default.PlayArrow, iconColor = Color.Gray, iconBgColor = Color(0xFFF5F5F5),imageUrl = "https://t3.ftcdn.net/jpg/06/69/94/84/360_F_669948452_Ew9QwGAnVvhZTjLgqECytuoRSallQt95.jpg", popupMessage = "Monitoring session started at 8:32 AM"),
    TimelineEvent("9:51", "Jacob was awake", "8 min", Icons.Default.RemoveRedEye, Color.Red, Color(0xFFFFEBEE), popupMessage = "Jacob was awake for 8 minutes"),
    TimelineEvent("9:56", "Jackon Awake", "15 min", Icons.Default.PhotoCamera, Color.Blue, Color(0xFFE3F2FD), imageUrl = "https://t3.ftcdn.net/jpg/06/69/94/84/360_F_669948452_Ew9QwGAnVvhZTjLgqECytuoRSallQt95.jpg", popupMessage = "Add photo at 9:56 AM"),
    TimelineEvent("9:59", "Jacob was sleeping", "37 min", Icons.Default.Bedtime, Color.Blue, Color(0xFFE3F2FD), popupMessage = "Jacob was sleeping for 37 minutes"),
    TimelineEvent("10:36", "Jacob was awake", "42 min", Icons.Default.RemoveRedEye, Color.Red, Color(0xFFFFEBEE),imageUrl = "https://t3.ftcdn.net/jpg/06/69/94/84/360_F_669948452_Ew9QwGAnVvhZTjLgqECytuoRSallQt95.jpg", popupMessage = "Jacob was awake for 42 minutes")
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(vertical = 8.dp) // optional top/bottom padding
    ) {
      // Header
      item {
        HeaderSection(onBackClick = onBackClick)
      }

      // Timeline items with spacing
      itemsIndexed(timelineEvents) { index, event ->
        Column {
          TimelineItem(
            event = event,
            onClick = { selectedEvent = event },
            showLine = index < timelineEvents.lastIndex
          )

          // Spacer between items
          Spacer(modifier = Modifier.height(12.dp))
        }
      }

      // Bottom padding spacer
      item {
        Spacer(modifier = Modifier.height(100.dp))
      }
    }


    // Floating popup
    selectedEvent?.let { event ->
      FloatingPopupCard(event = event, onDismiss = { selectedEvent = null })
    }
  }
}

@Composable
fun HeaderSection(onBackClick: () -> Unit) {
  Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp)) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Today", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color.Black)
      }
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(onClick = {}) {
          Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Gray)
        }
        IconButton(onClick = {}) {
          Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Column {
        Text("JOHNS IPHONE 15", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text("Total 6 hr, 13 sec", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier.size(32.dp).background(Color(0xFFFFB74D), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("J", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Jacob", fontSize = 14.sp, color = Color.Gray)
      }
    }
  }
}

@Composable
fun TimelineItem(
  event: TimelineEvent,
  onClick: () -> Unit,
  showLine: Boolean
) {
  Box {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Text(
        text = event.time,
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.width(50.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.TopCenter) {
        if (showLine) {
          Box(
            modifier = Modifier
              .width(2.dp)
              .height(60.dp)
              .offset(y = 20.dp)
              .background(Color(0xFFE0E0E0))
          )
        }

        Box(
          modifier = Modifier
            .size(32.dp)
            .background(event.iconBgColor, CircleShape)
            .zIndex(1f),
          contentAlignment = Alignment.Center
        ) {
          if (event.title.contains("sleeping")) {
            Text("zZ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = event.iconColor)
          } else {
            Icon(event.icon, contentDescription = null, tint = event.iconColor, modifier = Modifier.size(16.dp))
          }
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(event.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)

        event.duration?.let {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.Gray,
              modifier = Modifier.size(14.dp)
            )
            Text(it, fontSize = 14.sp, color = Color.Gray)
          }
        }

        // ⬇ Image now under title/duration
        event.imageUrl?.let { url ->
          Spacer(modifier = Modifier.height(8.dp))
          AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
              .fillMaxWidth(0.9f)
              .height(120.dp)
              .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
          )
        }
      }
    }
  }
}


@Composable
fun FloatingPopupCard(
  event: TimelineEvent,
  onDismiss: () -> Unit
) {
  Box(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier.padding(horizontal = 32.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(8.dp)
    ) {
      Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (event.title.contains("sleeping")) {
            Text("zZ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = event.iconColor)
          } else {
            Icon(event.icon, contentDescription = null, tint = event.iconColor, modifier = Modifier.size(20.dp))
          }
          Text(
            event.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (event.title.contains("Jacob was awake")) Color(0xFF7B68EE) else Color.Black
          )
        }

        event.duration?.let {
          Spacer(modifier = Modifier.height(8.dp))
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Text(
              text = if (event.title.contains("Jacob was awake") && event.time == "9:51") "48 sec" else it,
              fontSize = 16.sp,
              color = Color.Gray
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onDismiss) {
          Text("Close")
        }
      }
    }
  }
}

data class TimelineEvent(
  val time: String,
  val title: String,
  val duration: String? = null,
  val icon: ImageVector,
  val iconColor: Color,
  val iconBgColor: Color,
  val imageUrl: String? = null,
  val popupMessage: String
)
