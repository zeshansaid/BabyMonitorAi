package io.getstream.webrtc.sample.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import io.getstream.webrtc.sample.compose.ui.screens.ActivityScreen
import io.getstream.webrtc.sample.compose.ui.screens.ExploreScreen
import io.getstream.webrtc.sample.compose.ui.screens.HomeScreen
import io.getstream.webrtc.sample.compose.ui.screens.PremiumScreen
import io.getstream.webrtc.sample.compose.ui.screens.SettingScreen
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.getstream.webrtc.sample.compose.db.CategoryViewModel
import io.getstream.webrtc.sample.compose.db.CategoryViewModelFactory


@Composable
fun MainNavigationWithBottomBar(
  navController: NavHostController = rememberNavController(),
  onBabyStationClick: () -> Unit = {},
  onParentStationClick: () -> Unit = {}
) {
  val items = listOf(
    Screen.Monitoring,
    Screen.Activity,
    Screen.Explore,
    Screen.Premium,
    Screen.Settings
  )

  val currentBackStackEntry by navController.currentBackStackEntryAsState()
  val currentDestination = currentBackStackEntry?.destination?.route

  Scaffold(
    bottomBar = {
      BottomNavigationBar(
        selectedIndex = items.indexOfFirst { it.route == currentDestination },
        onItemSelected = { index ->
          navController.navigate(items[index].route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.startDestinationId) {
              saveState = true
            }
          }
        },
        onSettingsClick = { navController.navigate(Screen.Settings.route) },
        onExploreClick = { navController.navigate(Screen.Explore.route) },
        onPremiumClick = { navController.navigate(Screen.Premium.route) },
        onActivityClick = { navController.navigate(Screen.Activity.route) },
        onMonitoringClick = { navController.navigate(Screen.Monitoring.route) }
      )
    }
  ) { padding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Monitoring.route,
      modifier = Modifier.padding(padding)
    ) {
      composable(Screen.Monitoring.route) {
        HomeScreen(
          onBackClick = { /* optional */ },
          onBabyStationClick = onBabyStationClick,
          onParentStationClick = onParentStationClick
        )
      }

      composable(Screen.Activity.route) {


        val context = LocalContext.current
        val factory = CategoryViewModelFactory(context)
        val categoryViewModel: CategoryViewModel = viewModel(factory = factory)

        ActivityScreen(
          onBackClick = { navController.popBackStack() },
          categoryViewModel = categoryViewModel
        )
      }
      composable(Screen.Explore.route) {
        ExploreScreen(onBackClick = { navController.popBackStack() })
      }
      composable(Screen.Premium.route) {
        PremiumScreen(onBackClick = { navController.popBackStack() })
      }
      composable(Screen.Settings.route) {
        SettingScreen()
      }
    }
  }
}

