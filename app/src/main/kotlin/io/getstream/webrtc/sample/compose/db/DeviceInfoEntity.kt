package io.getstream.webrtc.sample.compose.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_info")
data class DeviceInfoEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0, // or use something like deviceId
  val manufacture: String,
  val model: String,
  val status: Boolean? = null
)
