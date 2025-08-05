package io.getstream.webrtc.sample.compose

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import io.getstream.webrtc.sample.compose.ui.onboard.OnBoardModel
import io.getstream.webrtc.sample.compose.webrtc.sessions.WebRtcSessionManager
import java.security.SecureRandom

class AppUtils {

  enum class Role {
    STREAMER, VIEWER
  }
  companion object {
    const val TAG = "tag"
    var sessionManager: WebRtcSessionManager? = null

    val screen_1 = listOf(
      OnBoardModel(imageRes = R.drawable.screen_1_1),
      OnBoardModel(imageRes = R.drawable.screen_1_2),
      OnBoardModel(imageRes = R.drawable.screen_1_3),
      OnBoardModel(imageRes = R.drawable.screen_1_4),
      OnBoardModel(imageRes = R.drawable.screen_1_5),
      OnBoardModel(imageRes = R.drawable.screen_1_6)
    )
    val screen_2 = listOf(
      OnBoardModel(imageRes = R.drawable.screen_2_1),
      OnBoardModel(imageRes = R.drawable.screen_2_2),
      OnBoardModel(imageRes = R.drawable.screen_2_3),

      )
    val screen_3 = listOf(
      OnBoardModel(imageRes = R.drawable.screen_3_1),
      OnBoardModel(imageRes = R.drawable.screen_3_2),
      OnBoardModel(imageRes = R.drawable.screen_3_3)
    )

    fun generate32BitRandomKey(): String {
      val random = SecureRandom()
      val bytes = ByteArray(16)
      random.nextBytes(bytes)
      return bytes.joinToString("") { "%02x".format(it) }
    }

    fun generateQRCode(text: String): Bitmap {
      val size = 700 // Size of the QR code
      val qrCodeWriter = QRCodeWriter()
      val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
      val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

      for (x in 0 until size) {
        for (y in 0 until size) {
          bitmap.setPixel(
            x,
            y,
            if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
          )
        }
      }
      return bitmap
    }

    fun toggleTorch(context: Context, enable: Boolean, onToggle: (Boolean) -> Unit): Boolean {
      val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
      return try {
        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
          cameraManager.getCameraCharacteristics(id)
            .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }

        cameraId?.let {
          cameraManager.setTorchMode(it, enable)
          onToggle(enable)
          return true
        }
        false
      } catch (e: CameraAccessException) {
        Log.e("Torch", "CameraAccessException: ${e.message}")
        false
      } catch (e: Exception) {
        Log.e("Torch", "Exception: ${e.message}")
        false
      }
    }

    fun toggleBrightness(context: Context, enable: Boolean, onToggle: (Boolean) -> Unit): Boolean {
      if (!Settings.System.canWrite(context)) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
          data = Uri.parse("package:" + context.packageName)
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        return false
      }

      return try {
        val brightness = if (enable) 255 else 30
        Settings.System.putInt(
          context.contentResolver,
          Settings.System.SCREEN_BRIGHTNESS,
          brightness
        )
        onToggle(enable)
        true
      } catch (e: Exception) {
        Log.e("Brightness", "Failed to set brightness: ${e.message}")
        false
      }
    }
  }

}