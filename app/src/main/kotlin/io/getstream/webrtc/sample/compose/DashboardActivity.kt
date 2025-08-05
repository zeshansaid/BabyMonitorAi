package io.getstream.webrtc.sample.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.getstream.webrtc.sample.compose.ui.screens.HomeScreen


import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.SideEffect

import androidx.compose.ui.Alignment

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.getstream.webrtc.sample.compose.ui.screens.CameraScreen
import io.getstream.webrtc.sample.compose.ui.screens.MonitorScreen
import io.getstream.webrtc.sample.compose.ui.screens.SettingScreen
import io.getstream.webrtc.sample.compose.ui.theme.WebrtcSampleComposeTheme


class DashboardActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)

    setContent {
      WebrtcSampleComposeTheme {

        val navController = rememberNavController()
        Scaffold(modifier = Modifier.fillMaxSize(),

          bottomBar = {
            BottomNavigationBar(
              items = listOf(
                BottomNavItem(
                  name = "Home",
                  route = "home",
                  icon = painterResource(id = R.drawable.ic_home)
                ),
                BottomNavItem(
                  name = "Camera",
                  route = "camera",
                  icon = painterResource(id = R.drawable.ic_cam)
                ),
                BottomNavItem(
                  name = "Monitor",
                  route = "monitor",
                  icon = painterResource(id = R.drawable.ic_monitor)
                  ),
                BottomNavItem(
                  name = "Settings",
                  route = "settings",
                  icon = painterResource(id = R.drawable.ic_settings)
                )
              ),
              navController = navController,
              onItemClick = {
                navController.navigate(it.route)
              }
            )
          }
        ) { innerPadding ->
          Column(
            modifier = Modifier
              .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
          ) {
            Navigation(navController = navController)
          }
        }
      }
    }
  }
}
@Composable
fun Navigation(navController: NavHostController){
  NavHost(navController = navController, startDestination = "home"){

    composable("camera") {
      CameraScreen()
    }
    composable("monitor") {
      MonitorScreen()
    }
    composable("settings") {
      SettingScreen()
    }
  }
}
@Composable
fun BottomNavigationBar(items: List<BottomNavItem>,
                        navController: NavHostController,
                        modifier: Modifier = Modifier,
                        onItemClick: (BottomNavItem) -> Unit) {
  val backstackEntry = navController.currentBackStackEntryAsState()

  NavigationBar(
    modifier = modifier,
    containerColor = colorResource(id = R.color.card_color),
    contentColor = Color.White

  ) {
    items.forEach { item->
      val selected = item.route == backstackEntry.value?.destination?.route
      NavigationBarItem(
        selected = selected,
        onClick = { onItemClick(item) },

        icon = {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if(item.badgeCount > 0){
              BadgedBox(
                badge = {
                  Text(text = item.badgeCount.toString())
                }
              ) {
                Icon(
                  painter = item.icon,
                  contentDescription = null,
                )
              }
            } else{
              Icon(
                painter = item.icon,
                modifier = Modifier.padding(24.dp),
                contentDescription = null,
                tint = if (selected) colorResource(id = R.color.app_green) else colorResource(id = R.color.white)
              )
            }
          }
        },
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = Color.Transparent
        )
      )
    }
  }
}