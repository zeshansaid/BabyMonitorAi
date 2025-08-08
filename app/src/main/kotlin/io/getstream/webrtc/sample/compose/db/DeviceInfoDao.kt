package io.getstream.webrtc.sample.compose.db


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceInfoDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(devices: DeviceInfoEntity)

  @Query("SELECT * FROM device_info")
  fun getAllDevices(): Flow<List<DeviceInfoEntity>>

  @Query("DELETE FROM device_info")
  suspend fun clearAll()
}
