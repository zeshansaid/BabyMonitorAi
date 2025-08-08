package io.getstream.webrtc.sample.compose.ui.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.getstream.webrtc.sample.compose.AppUtils
import io.getstream.webrtc.sample.compose.db.CategoryEntity
import io.getstream.webrtc.sample.compose.db.CategoryViewModel
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
  onBackClick: () -> Unit,
  categoryViewModel: CategoryViewModel
) {

  val categories by categoryViewModel.categories.collectAsState()
  val isLoading by categoryViewModel.isLoading.collectAsState()


  val colors = MaterialTheme.colorScheme
  val typography = MaterialTheme.typography
  val isDark = isSystemInDarkTheme()


  var selectedCategory by remember { mutableStateOf<CategoryEntity?>(null) }




  Scaffold(
    containerColor = colors.background,
    topBar = {
      TopAppBar(
        title = { Text("BABY ACTIVITY TIMELINE") },
        navigationIcon = {

        }
      )
    }
  ) { innerPadding ->

    when {
      isLoading -> {
        // Show shimmer placeholders
        LazyColumn(
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          items(6) { index ->
            ShimmerCategoryItem(showLine = index < 5)
          }
        }
      }

      categories.isEmpty() -> {
        // Show "No activity" message
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text("No activity found.")
        }
      }

      else -> {


        /*show category list*/

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight(0.1f)
              .padding(horizontal = 16.dp)
          ) {
            HeaderDeviceInfo()
          }


          // LazyColumn (remaining space)
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
          ) {
            itemsIndexed(categories) { index, item ->
              Column {
                CategoryItem(
                  event = item,
                  onClick = { selectedCategory = item },

                  )
                Spacer(modifier = Modifier.height(12.dp))
              }
            }

            item {
              Spacer(modifier = Modifier.height(100.dp))
            }
          }

          // Optional popup
          selectedCategory?.let {
            // FloatingPopupCard(event = it, onDismiss = { selectedCategory = null })
          }
        }

      }



    }


  }
}


@Composable
fun ShimmerEffect(
  modifier: Modifier = Modifier,
  shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(4.dp)
) {
  val transition = rememberInfiniteTransition(label = "shimmer")
  val translateAnim = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1000f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "shimmer_translate"
  )

  val brush = Brush.linearGradient(
    colors = listOf(
      Color(0xFFE1E1E1),
      Color(0xFFF5F5F5),
      Color(0xFFE1E1E1)
    ),
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value)
  )

  Box(
    modifier = modifier
      .clip(shape)
      .background(brush)
  )
}

@Composable
fun HeaderDeviceInfo() {
  val colors = MaterialTheme.colorScheme
  val typography = MaterialTheme.typography

  Column(modifier = Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Column {
        Text(
          "JOHNS IPHONE 15",
          style = typography.labelSmall.copy(color = colors.outline)
        )
        Text(
          "Total 6 hr, 13 sec",
          style = typography.titleMedium.copy(color = colors.onBackground)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .background(colors.primary, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text("J", color = colors.onPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text("Jacob", style = typography.bodyMedium.copy(color = colors.outline))
      }
    }
  }
}

@Composable
fun ShimmerCategoryItem(showLine: Boolean) {
  Box {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.Top
    ) {
      // Shimmer for score
      ShimmerEffect(
        modifier = Modifier
          .width(40.dp)
          .height(16.dp),
        shape = RoundedCornerShape(4.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      // Icon area with line
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

        ShimmerEffect(
          modifier = Modifier
            .size(32.dp)
            .zIndex(1f),
          shape = CircleShape
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      // Content area
      Column(modifier = Modifier.weight(1f)) {
        ShimmerEffect(
          modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(18.dp),
          shape = RoundedCornerShape(4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ShimmerEffect(
          modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(14.dp),
          shape = RoundedCornerShape(4.dp)
        )
      }
    }
  }
}

@SuppressLint("DefaultLocale")
@Composable
fun CategoryItem(
  event: CategoryEntity,
  onClick: () -> Unit,

  ) {
  val colors = MaterialTheme.colorScheme
  val typography = MaterialTheme.typography
  val isDarkTheme = isSystemInDarkTheme()
  val percentage = String.format("%.1f", event.score * 100)
  Box {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(horizontal = 24.dp),
      verticalAlignment = Alignment.Top
    ) {

      Text(
        text = event.timestamp,
        style = typography.bodySmall.copy(
          color = if (isDarkTheme) Color.White else Color.Black
        ),
        modifier = Modifier.padding(top = 4.dp)
      )
      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.width(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        val randomColor = remember {
          Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
          )
        }

        Box(
          modifier = Modifier
            .size(32.dp)
            .background(randomColor, CircleShape)
            .zIndex(1f),
          contentAlignment = Alignment.Center
        ) {
          Text(
            "zZ",
            style = typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = colors.onSecondary
            )
          )
        }

        Box(
          modifier = Modifier
            .width(2.dp)
            .height(45.dp)
            .padding(top = 4.dp)
            .background(colors.surfaceVariant)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          event.categoryName,
          style = typography.bodyLarge.copy(
            color = colors.onBackground,
            fontSize = 16.sp
          ),
          modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "NaniBot is $percentage% confident",
          fontSize = 12.sp,
          style = typography.bodyMedium.copy(
            color = if (isDarkTheme) Color.LightGray else Color.DarkGray
          ),
          modifier = Modifier.padding(top = 4.dp)
        )


      }
    }
  }
}