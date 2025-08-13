package io.getstream.webrtc.sample.compose.ui.screens

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.AnimatedPreloader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSheet(
  message: String,
  onDismiss: () -> Unit,
  onSubscribeClick: () -> Unit
) {
  val modalBottomSheetState = rememberModalBottomSheetState()
  val isDarkTheme = isSystemInDarkTheme()
  val mContext = LocalContext.current

  // Map song names to raw resources
  val songMap = mapOf(
    "peaceful piano music" to R.raw.musci1,
    "pop music" to R.raw.music2
  )

// Get resource ID from message (nullable if not found)
  val songResId = songMap[message.lowercase()]

// Only create MediaPlayer if songResId is found
  val mediaPlayer = remember(songResId) {
    songResId?.let { resId ->
      MediaPlayer.create(mContext, resId).apply {
        start() // auto-play

        setOnCompletionListener {
          it.release()
          Handler(Looper.getMainLooper()).post {
            onDismiss()
          }
        }


      }

    }
  }

  DisposableEffect(mediaPlayer) {
    onDispose {
      mediaPlayer?.run {
        try {
          stop()
        } catch (_: IllegalStateException) {
          // Ignore stop() if player not in valid state
        }
        release()
      }
    }
  }



  ModalBottomSheet(
    onDismissRequest = {
      mediaPlayer?.stop()
      mediaPlayer?.release()
      onDismiss()
    },
    sheetState = modalBottomSheetState,
    dragHandle = { BottomSheetDefaults.HiddenShape },

    ) {

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFF9C50FF), // Deep Purple
              Color(0xFF7B1FA2), // Medium Purple
              Color(0xFF8E24AA)  // Light Purple
            )
          )
        )
        .padding(20.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))

          .padding(top = 6.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Optional: center vertically
      )
      {
        Spacer(modifier = Modifier.height(20.dp))
        // Indicator bar
        Box(
          modifier = Modifier
            .width(40.dp)
            .height(4.dp)
            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
            .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedPreloader(
          animationResId = R.raw.music_animation,
          modifier = Modifier.size(300.dp)
        )


        Text(
          text = message,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,

            )
        )
        Row {


          // IconButton for Pause Action
          IconButton(onClick = { mediaPlayer?.pause() }) {
            Icon(
              painter = painterResource(id = android.R.drawable.ic_media_pause),
              contentDescription = "",
              Modifier.size(100.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(32.dp))
      }

    }


  }
}
