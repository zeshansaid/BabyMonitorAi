package io.getstream.webrtc.sample.compose.db


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DeviceInfoViewModel(private val repository: DeviceInfoRepository) : ViewModel() {

  private val _devices = MutableStateFlow<List<DeviceInfoEntity>>(emptyList())
  val devices: StateFlow<List<DeviceInfoEntity>> = _devices

  init {
    loadDevices()
  }

  private fun loadDevices() {
    viewModelScope.launch {
      repository.getAllDevices().collect {
        _devices.value = it
      }
    }
  }

  fun insert(devices: DeviceInfoEntity) {
    viewModelScope.launch {
      repository.insertDevices(devices)
    }
  }

  fun clear() {
    viewModelScope.launch {
      repository.clearDevices()
    }
  }
}
