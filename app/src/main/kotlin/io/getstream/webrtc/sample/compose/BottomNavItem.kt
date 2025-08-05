package io.getstream.webrtc.sample.compose

import androidx.compose.ui.graphics.painter.Painter

data class BottomNavItem(
  val name: String,
  val route: String,
  val icon: Painter,
  val badgeCount: Int = 0
)
