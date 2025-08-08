package io.getstream.webrtc.sample.compose.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.getstream.webrtc.sample.compose.db.DeviceInfoEntity
import io.getstream.webrtc.sample.compose.db.DeviceInfoViewModel
import io.getstream.webrtc.sample.compose.db.DeviceInfoViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
  onBackClick: () -> Unit
) {
//  val deviceInfoList = listOf(
//    DeviceInfoItem(Build.MANUFACTURER, Build.MODEL, Icons.Default.PhoneAndroid, status = true),
//    DeviceInfoItem(Build.MANUFACTURER, Build.MODEL, Icons.Default.PhoneAndroid, status = false),
//    )
  val context = LocalContext.current

  val viewModel: DeviceInfoViewModel = viewModel(
    factory = DeviceInfoViewModelFactory(context)
  )

  val deviceInfoList by viewModel.devices.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Connected Devices") },
        modifier = Modifier.statusBarsPadding()
      )
    },
    bottomBar = {
      BottomAppBar {
        Row(
          modifier = Modifier
            .fillMaxWidth()

            .padding(end = 16.dp),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
          )
          {

            Box(
              modifier = Modifier
                .size(width = 18.dp, height = 10.dp) // rectangular
                .padding(start = 8.dp)
                .background(color = Color(0xFF4CAF50), shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Connected", fontSize = 12.sp)
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
          )
          {

            Box(
              modifier = Modifier
                .size(width = 18.dp, height = 10.dp) // rectangular
                .padding(start = 8.dp)
                .background(color = Color(0xFFF44336), shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Offline", fontSize = 12.sp)
          }
        }
      }
    }

  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)
        .fillMaxSize()
        .safeContentPadding(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(deviceInfoList) { item ->
        DeviceInfoRow(item)
      }
    }
  }
}




@Composable
fun DeviceInfoRow(item: DeviceInfoEntity) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    )
    {
      Icon(
        imageVector = Icons.Default.PhoneAndroid,
        contentDescription = item.manufacture,
        modifier = Modifier.size(32.dp),
        tint = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.width(16.dp))
      Column {
        Text(
          text = item.manufacture,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
          text = item.model,
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
          color = Color.Gray
        )
      }
    }

    if (item.status != null) {
      val color = if (item.status) Color(0xFF4CAF50) else Color(0xFFF44336)
      Box(
        modifier = Modifier
          .size(14.dp)
          .padding(start = 8.dp)
          .background(color = color, shape = MaterialTheme.shapes.small)
      )
    }
  }
}
