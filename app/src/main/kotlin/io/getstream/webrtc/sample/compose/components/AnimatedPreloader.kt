package io.getstream.webrtc.sample.compose.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AnimatedPreloader(
  @RawRes animationResId: Int,
  modifier: Modifier = Modifier
) {
  val composition by rememberLottieComposition(
    LottieCompositionSpec.RawRes(animationResId)
  )

  val progress by animateLottieCompositionAsState(
    composition,
    iterations = LottieConstants.IterateForever,
    isPlaying = true
  )

  LottieAnimation(
    composition = composition,
    progress = progress,
    modifier = modifier
  )
}
