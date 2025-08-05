package io.getstream.webrtc.sample.compose.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.ui.theme.appColorScheme

@Composable
fun BottomNavigationBar(
  modifier: Modifier = Modifier,
  selectedIndex: Int,
  onMonitoringClick: () -> Unit,
  onItemSelected: (Int) -> Unit,
  onSettingsClick: () -> Unit,
  onActivityClick: () -> Unit,
  onPremiumClick: () -> Unit,
  onExploreClick: () -> Unit
) {
  val colors = appColorScheme()

  val items = listOf(
    NavItem("Monitoring", Icons.Default.Home),
    NavItem("Activity", Icons.Default.Home),
    NavItem("Explore", Icons.Default.Home),
    NavItem("Premium", Icons.Default.Star),
    NavItem("Setting", Icons.Default.Settings),
  )

  NavigationBar(
    modifier = modifier,
    containerColor = colors.background
  ) {
    items.forEachIndexed { index, item ->
      NavigationBarItem(
        selected = selectedIndex == index,
        onClick = {
          when (item.label) {
            "Monitoring" -> onMonitoringClick()

            "Setting" -> onSettingsClick()
            "Explore" -> onExploreClick()
            "Premium" -> onPremiumClick()
            "Activity" -> onActivityClick()
            else -> onItemSelected(index)
          }
        },
        icon = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = item.icon,
              contentDescription = item.label,
              tint = if (selectedIndex == index)
                colors.primary else colors.onSurface.copy(alpha = 0.5f),
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = item.label,
              fontSize = 12.sp,
              color = if (selectedIndex == index)
                colors.primary else colors.onSurface.copy(alpha = 0.5f)
            )
          }
        },
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent
        )
      )
    }
  }
}

data class NavItem(val label: String, val icon: ImageVector)