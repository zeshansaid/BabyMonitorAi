package io.getstream.webrtc.sample.compose.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.db.CategoryEntity
import io.getstream.webrtc.sample.compose.db.CategoryViewModel


@Composable
fun ActivityScreen(
  onBackClick: () -> Unit,
  categoryViewModel: CategoryViewModel
) {

  val categories by categoryViewModel.categories.collectAsState()

  var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }


  LaunchedEffect(categories) {
    Log.d( AppUtils.TAG, "Loaded ${categories.size} categories")
    categories.forEach {
      Log.d(AppUtils.TAG, "Category:from db name=${it.categoryName}, score=${it.score}, displayName=${it.displayName}")
    }
  }

//  val timelineEvents = listOf(
//    TimelineEvent("8:32", "Jackob Eating","20 min", icon = Icons.Default.PlayArrow, iconColor = Color.Gray, iconBgColor = Color(0xFFF5F5F5),imageUrl = "https://t3.ftcdn.net/jpg/06/69/94/84/360_F_669948452_Ew9QwGAnVvhZTjLgqECytuoRSallQt95.jpg", popupMessage = "Monitoring session started at 8:32 AM"),
//    TimelineEvent("9:51", "Jacob was awake", "8 min", Icons.Default.RemoveRedEye, Color.Red, Color(0xFFFFEBEE), popupMessage = "Jacob was awake for 8 minutes"),
//   )

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
      itemsIndexed(categories) { index, item ->
        Column {
          CategoryItem(
            event = item,
            onClick = { selectedCategory = item },
            showLine = index < categories.lastIndex
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
    selectedCategory?.let { event ->
//      FloatingPopupCard(event = event, onDismiss = { selectedCategory = null })
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

@SuppressLint("DefaultLocale")
@Composable
fun CategoryItem(
  event: CategoryEntity,
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
        text = String.format("%.2f", event.score),
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
            .background(Color.Gray, CircleShape)
            .zIndex(1f),
          contentAlignment = Alignment.Center
        ) {
          if (event.categoryName.contains("sleeping")) {
            Text("zZ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
          } else {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
          }
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(event.categoryName, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)

//        event.duration?.let {
//          Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(4.dp)
//          ) {
//            Icon(
//              Icons.Default.PlayArrow,
//              contentDescription = null,
//              tint = Color.Gray,
//              modifier = Modifier.size(14.dp)
//            )
//            Text(it, fontSize = 14.sp, color = Color.Gray)
//          }
//        }

        // ⬇ Image now under title/duration
//        event.imageUrl?.let { url ->
//          Spacer(modifier = Modifier.height(8.dp))
//          AsyncImage(
//            model = url,
//            contentDescription = null,
//            modifier = Modifier
//              .fillMaxWidth(0.9f)
//              .height(120.dp)
//              .clip(RoundedCornerShape(10.dp)),
//            contentScale = ContentScale.Crop
//          )
//        }
//



      }
    }
  }
}


