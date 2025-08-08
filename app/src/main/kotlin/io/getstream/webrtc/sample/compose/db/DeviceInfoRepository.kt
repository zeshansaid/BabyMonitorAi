package io.getstream.webrtc.sample.compose.db


import kotlinx.coroutines.flow.Flow

class DeviceInfoRepository(private val dao: DeviceInfoDao) {

  suspend fun insertDevices(devices:  DeviceInfoEntity) {
    dao.insertAll(devices)
  }

  fun getAllDevices(): Flow<List<DeviceInfoEntity>> = dao.getAllDevices()

  suspend fun clearDevices() = dao.clearAll()
}
