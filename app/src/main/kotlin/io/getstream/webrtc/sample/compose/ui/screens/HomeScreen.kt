package io.getstream.webrtc.sample.compose.ui.screens


import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.getstream.webrtc.sample.compose.R
import io.getstream.webrtc.sample.compose.components.BottomNavigationBar
import io.getstream.webrtc.sample.compose.db.DeviceInfoEntity
import io.getstream.webrtc.sample.compose.db.DeviceInfoViewModel
import io.getstream.webrtc.sample.compose.db.DeviceInfoViewModelFactory
import io.getstream.webrtc.sample.compose.ui.theme.appColorScheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onBackClick: () -> Unit,
  onBabyStationClick: () -> Unit,
  onParentStationClick: () -> Unit
) {
  val colors = appColorScheme()
  val scrollState = rememberScrollState()
  val context = LocalContext.current

  val deviceInfoViewModel: DeviceInfoViewModel = viewModel(
    factory = DeviceInfoViewModelFactory(context)
  )

  LaunchedEffect(Unit) {
    val deviceInfo = DeviceInfoEntity(
      manufacture = Build.MANUFACTURER,
      model = Build.MODEL,
      status = true
    )
    deviceInfoViewModel.insert(deviceInfo)
  }


  Scaffold(
    topBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
//          IconButton(onClick = onBackClick) {
//            Icon(
//              imageVector = Icons.Default.ArrowBack,
//              contentDescription = "Back",
//              tint = colors.primary
//            )
//          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background),
        modifier = Modifier.fillMaxWidth()
      )
    },
    containerColor = colors.background
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(scrollState)
    ) {
      // Top 60%
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.6f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.img),
          contentDescription = "Baby Logo",
          modifier = Modifier.size(280.dp)
        )

        Text(
          text = "maibaby",
          color = colors.primary,
          fontSize = 36.sp,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          style = MaterialTheme.typography.titleLarge
        )
      }

      // Bottom 40%
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.4f)
          .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Use this device as:",
          color = colors.onSurface.copy(alpha = 0.7f),
          fontSize = 16.sp,
          style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Baby Station Button
        Button(
          onClick = onBabyStationClick,
          colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(30.dp),
          elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_babyzone),
            contentDescription = "Baby Icon",
            tint = colors.onPrimary,
            modifier = Modifier
              .size(20.dp)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text("Baby Station", color = colors.onPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Parent Station Button
        Button(
          onClick = onParentStationClick,
          colors = ButtonDefaults.buttonColors(containerColor = colors.background),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
          shape = RoundedCornerShape(30.dp),
          elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(colors.primary)
          )
        ) {

          Icon(
            painter = painterResource(id = R.drawable.ic_parentzone),
            contentDescription = "Baby Icon",
            tint = colors.primary,
            modifier = Modifier
              .size(20.dp)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text("Parent Station", color = colors.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
        }
      }
    }
  }
}
