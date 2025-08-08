package io.getstream.webrtc.sample.compose.db


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeviceInfoViewModelFactory(context: Context) : ViewModelProvider.Factory {
  private val database = AppDatabase.getDatabase(context)
  private val repository = DeviceInfoRepository(database.deviceInfoDao())

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return DeviceInfoViewModel(repository) as T
  }
}
