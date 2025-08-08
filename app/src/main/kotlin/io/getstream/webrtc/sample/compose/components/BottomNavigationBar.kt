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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.webrtc.sample.compose.R
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
    NavItem("Monitoring", R.drawable.ic_home),
    NavItem("Activity",R.drawable.ic_activity  ),
    NavItem("Devices", R.drawable.ic_device ),
    NavItem("Premium", R.drawable.ic_start ),
    NavItem("Setting", R.drawable.ic_settings ),
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
            "Devices" -> onExploreClick()
            "Premium" -> onPremiumClick()
            "Activity" -> onActivityClick()
            else -> onItemSelected(index)
          }
        },
        icon = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              painter = painterResource(id = item.iconResId),
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

data class NavItem(val label: String, val iconResId: Int)