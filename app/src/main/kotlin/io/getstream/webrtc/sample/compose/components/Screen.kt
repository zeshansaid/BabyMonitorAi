package io.getstream.webrtc.sample.compose.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
  object Monitoring : Screen("monitoring", "Monitoring", Icons.Default.Home)
  object Activity : Screen("activity", "Activity", Icons.Default.History)
  object Explore : Screen("explore", "Explore", Icons.Default.Explore)
  object Premium : Screen("premium", "Premium", Icons.Default.Star)
  object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}
