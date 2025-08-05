package io.getstream.webrtc.sample.compose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import io.getstream.log.android.AndroidStreamLogger

class WebRTCApp : Application() {

  override fun onCreate() {
    super.onCreate()

    AndroidStreamLogger.installOnDebuggableApp(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel()
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun createNotificationChannel() {
    val channel = NotificationChannel(
      "channel1",
      "Default Channel",
      NotificationManager.IMPORTANCE_DEFAULT
    )

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
    notificationManager?.createNotificationChannel(channel)
  }
}
