package io.getstream.webrtc.sample.compose.webrtc.sessions

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.content.getSystemService
import io.getstream.log.taggedLogger
import io.getstream.webrtc.sample.compose.AppUtils

import io.getstream.webrtc.sample.compose.webrtc.SignalingClient
import io.getstream.webrtc.sample.compose.webrtc.SignalingCommand
import io.getstream.webrtc.sample.compose.webrtc.audio.AudioHandler
import io.getstream.webrtc.sample.compose.webrtc.audio.AudioSwitchHandler
import io.getstream.webrtc.sample.compose.webrtc.peer.StreamPeerConnection
import io.getstream.webrtc.sample.compose.webrtc.peer.StreamPeerConnectionFactory
import io.getstream.webrtc.sample.compose.webrtc.peer.StreamPeerType
import io.getstream.webrtc.sample.compose.webrtc.utils.stringify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.Camera2Capturer
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerationAndroid
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStreamTrack
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoTrack
import java.util.UUID

private const val ICE_SEPARATOR = '$'

val LocalWebRtcSessionManager: ProvidableCompositionLocal<WebRtcSessionManager> =
  staticCompositionLocalOf { error("WebRtcSessionManager was not initialized!") }

class WebRtcSessionManagerImpl(
  private val context: Context,
  override val signalingClient: SignalingClient,
  override val peerConnectionFactory: StreamPeerConnectionFactory,
  private val role: AppUtils.Role

) : WebRtcSessionManager {

  private val logger by taggedLogger("Call:`LocalWebRtcSessionManager`")
  private val sessionManagerScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  // used to send local video track to the fragment
  private val _localVideoTrackFlow = MutableSharedFlow<VideoTrack>(replay = 1)
  override val localVideoTrackFlow: SharedFlow<VideoTrack> = _localVideoTrackFlow

  // used to send remote video track to the sender
  private val _remoteVideoTrackFlow = MutableSharedFlow<VideoTrack>()
  override val remoteVideoTrackFlow: SharedFlow<VideoTrack> = _remoteVideoTrackFlow

  // declaring video constraints and setting OfferToReceiveVideo to true
  // this step is mandatory to create valid offer and answer
  private val mediaConstraints = MediaConstraints().apply {
    mandatory.addAll(
      listOf(
        MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"),
        MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true")
      )
    )
  }

  // getting front camera
  private val videoCapturer: VideoCapturer by lazy { buildCameraCapturer() }
  private val cameraManager by lazy { context.getSystemService<CameraManager>() }

  private val cameraEnumerator: Camera2Enumerator by lazy {
    Camera2Enumerator(context)
  }



  private val resolution: CameraEnumerationAndroid.CaptureFormat
    get() {
      val preferredWidths = listOf(720, 480, 360)

      val frontCamera = cameraEnumerator.deviceNames.firstOrNull { cameraEnumerator.isFrontFacing(it) }
      if (frontCamera == null) {
        logger.w { "No front-facing camera found. Attempting to use default camera." }
        val fallbackCamera = cameraEnumerator.deviceNames.firstOrNull()
          ?: return CameraEnumerationAndroid.CaptureFormat(640, 480, 30, 30) // absolute fallback
        return getBestAvailableResolution(fallbackCamera, preferredWidths)
      }

      return getBestAvailableResolution(frontCamera, preferredWidths)
    }

  private fun getBestAvailableResolution(
    cameraName: String,
    preferredWidths: List<Int>
  ): CameraEnumerationAndroid.CaptureFormat {
    val supportedFormats = cameraEnumerator.getSupportedFormats(cameraName)
    if (supportedFormats.isNullOrEmpty()) {
      logger.e { "No supported formats found for camera: $cameraName" }
      return CameraEnumerationAndroid.CaptureFormat(640, 480, 30, 30)
    }

    val matched = supportedFormats.firstOrNull { it.width in preferredWidths }
    if (matched != null) {
      logger.d { "Matched resolution: ${matched.width}x${matched.height}" }
      return matched
    }

    val fallback = supportedFormats.first()
    logger.w { "No preferred resolution found. Using fallback: ${fallback.width}x${fallback.height}" }
    return fallback
  }



  // we need it to initialize video capturer
  private val surfaceTextureHelper = SurfaceTextureHelper.create(
    "SurfaceTextureHelperThread",
    peerConnectionFactory.eglBaseContext
  )

  private val videoSource by lazy {
    if (role == AppUtils.Role.VIEWER) return@lazy null
    peerConnectionFactory.makeVideoSource(videoCapturer.isScreencast).apply {
      videoCapturer.initialize(surfaceTextureHelper, context, this.capturerObserver)
      videoCapturer.startCapture(resolution.width, resolution.height, 30)
    }
  }

  private val localVideoTrack: VideoTrack by lazy {
    videoSource?.let {
      peerConnectionFactory.makeVideoTrack(
        source = it,
        trackId = "Video${UUID.randomUUID()}"
      )
    }!!
  }
  /** Audio properties */
  private val audioHandler: AudioHandler by lazy {
    AudioSwitchHandler(context)
  }
  private val audioManager by lazy {
    context.getSystemService<AudioManager>()
  }

  private val audioConstraints: MediaConstraints by lazy {
    buildAudioConstraints()
  }
  private val audioSource by lazy {
    peerConnectionFactory.makeAudioSource(audioConstraints)
  }
  private val localAudioTrack: AudioTrack by lazy {
    peerConnectionFactory.makeAudioTrack(
      source = audioSource,
      trackId = "Audio${UUID.randomUUID()}"
    )
  }
  private var offer: String? = null

  private val peerConnection: StreamPeerConnection by lazy {
    peerConnectionFactory.makePeerConnection(
      coroutineScope = sessionManagerScope,
      configuration = peerConnectionFactory.rtcConfig,
      type = StreamPeerType.SUBSCRIBER,
      mediaConstraints = mediaConstraints,
      onIceCandidateRequest = { iceCandidate, _ ->
        signalingClient.sendCommand(
          SignalingCommand.ICE,
          "${iceCandidate.sdpMid}$ICE_SEPARATOR${iceCandidate.sdpMLineIndex}$ICE_SEPARATOR${iceCandidate.sdp}"
        )
      },

      onVideoTrack = { rtpTransceiver ->
        val track = rtpTransceiver?.receiver?.track() ?: return@makePeerConnection
        if (track.kind() == MediaStreamTrack.VIDEO_TRACK_KIND) {
          val videoTrack = track as VideoTrack
          sessionManagerScope.launch {
            _remoteVideoTrackFlow.emit(videoTrack)
          }
        }

        when (track.kind()) {
          MediaStreamTrack.VIDEO_TRACK_KIND -> {
            val videoTrack = track as VideoTrack
            sessionManagerScope.launch {
              _remoteVideoTrackFlow.emit(videoTrack)
            }
          }

          MediaStreamTrack.AUDIO_TRACK_KIND -> {
            val audioTrack = track as AudioTrack
            // Optional: store it or log it
            // Audio will automatically play if routed to speaker
            // You could also attach it to an audio sink (if needed)
            logger.d { "Remote audio track received: ${audioTrack.id()}" }
          }
        }

      }


    )
  }

  init {
    sessionManagerScope.launch {
      signalingClient.signalingCommandFlow
        .collect { commandToValue ->
          when (commandToValue.first) {
            SignalingCommand.OFFER -> handleOffer(commandToValue.second)
            SignalingCommand.ANSWER -> handleAnswer(commandToValue.second)
            SignalingCommand.ICE -> handleIce(commandToValue.second)
            else -> Unit
          }
        }
    }
  }

  override fun onSessionScreenReady() {
    setupAudio()
    if (role == AppUtils.Role.STREAMER) {

      peerConnection.connection.addTrack(localVideoTrack)
      peerConnection.connection.addTrack(localAudioTrack)

      sessionManagerScope.launch {
        _localVideoTrackFlow.emit(localVideoTrack)
        if (offer != null) {
          sendAnswer()
        } else {
          sendOffer()
        }
      }
    } else {
      // viewer waits for offer and only sends answer
      sessionManagerScope.launch {
        while (offer == null) {
          delay(100) // wait for offer
        }
        sendAnswer()
      }
    }
  }

  override fun flipCamera() {
    (videoCapturer as? Camera2Capturer)?.switchCamera(null)
  }

  override fun enableMicrophone(enabled: Boolean) {
    audioManager?.isMicrophoneMute = !enabled
  }

  override fun enableCamera(enabled: Boolean) {
    if (enabled) {
      videoCapturer.startCapture(resolution.width, resolution.height, 30)
    } else {
      videoCapturer.stopCapture()
    }
  }

  //for streamer
  override fun disconnect() {
    // dispose audio & video tracks.

    localAudioTrack.dispose()
    localVideoTrack.dispose()
    // Dispose audio source
    audioSource.dispose()
    // dispose audio handler and video capturer.
    audioHandler.stop()
    videoCapturer.stopCapture()
    videoCapturer.dispose()
    // Reset AudioManager mode
    audioManager?.mode = AudioManager.MODE_NORMAL
    audioManager?.isMicrophoneMute = false


    // dispose signaling clients and socket.
    signalingClient.dispose()
//       Stop WebRTC's network monitoring to prevent receiver leaks
    org.webrtc.NetworkMonitor.getInstance().stopMonitoring()
  }

  //for viewer
  override fun disconnectRemoteConnection() {
    try {
      // Dispose remote tracks only once (or clear replayCache and track disposal manually)
      remoteVideoTrackFlow.replayCache.forEach {
        try {
          it.dispose()
        } catch (e: IllegalStateException) {
          // Already disposed
        }
      }

      // Stop audio handler (optional for viewer, but still safe)
      audioHandler.stop()

      // Reset AudioManager mode
      audioManager?.mode = AudioManager.MODE_NORMAL
      audioManager?.isMicrophoneMute = false

      signalingClient.dispose()
      org.webrtc.NetworkMonitor.getInstance().stopMonitoring()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private suspend fun sendOffer() {
    val offer = peerConnection.createOffer().getOrThrow()
    val result = peerConnection.setLocalDescription(offer)
    result.onSuccess {
      signalingClient.sendCommand(SignalingCommand.OFFER, offer.description)
    }
    logger.d { "[SDP] send offer: ${offer.stringify()}" }
  }

  private suspend fun sendAnswer() {
    peerConnection.setRemoteDescription(
      SessionDescription(SessionDescription.Type.OFFER, offer)
    )
    val answer = peerConnection.createAnswer().getOrThrow()
    val result = peerConnection.setLocalDescription(answer)
    result.onSuccess {
      signalingClient.sendCommand(SignalingCommand.ANSWER, answer.description)
    }
    logger.d { "[SDP] send answer: ${answer.stringify()}" }
  }

  private fun handleOffer(sdp: String) {
    logger.d { "[SDP] handle offer: $sdp" }
    offer = sdp
  }

  private suspend fun handleAnswer(sdp: String) {
    logger.d { "[SDP] handle answer: $sdp" }
    peerConnection.setRemoteDescription(
      SessionDescription(SessionDescription.Type.ANSWER, sdp)
    )
  }

  private suspend fun handleIce(iceMessage: String) {
    val iceArray = iceMessage.split(ICE_SEPARATOR)
    peerConnection.addIceCandidate(
      IceCandidate(
        iceArray[0],
        iceArray[1].toInt(),
        iceArray[2]
      )
    )
  }

  private fun buildCameraCapturer(): VideoCapturer {
    val manager = cameraManager ?: throw RuntimeException("CameraManager was not initialized!")

    val ids = manager.cameraIdList
    var foundCamera = false
    var cameraId = ""

    for (id in ids) {
      val characteristics = manager.getCameraCharacteristics(id)
      val cameraLensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

      if (cameraLensFacing == CameraMetadata.LENS_FACING_FRONT) {
        foundCamera = true
        cameraId = id
      }
    }

    if (!foundCamera && ids.isNotEmpty()) {
      cameraId = ids.first()
    }

    val camera2Capturer = Camera2Capturer(context, cameraId, null)
    return camera2Capturer
  }

  private fun buildAudioConstraints(): MediaConstraints {
    val mediaConstraints = MediaConstraints()
    val items = listOf(
      MediaConstraints.KeyValuePair(
        "googEchoCancellation",
        true.toString()
      ),
      MediaConstraints.KeyValuePair(
        "googAutoGainControl",
        true.toString()
      ),
      MediaConstraints.KeyValuePair(
        "googHighpassFilter",
        true.toString()
      ),
      MediaConstraints.KeyValuePair(
        "googNoiseSuppression",
        true.toString()
      ),
      MediaConstraints.KeyValuePair(
        "googTypingNoiseDetection",
        true.toString()
      )
    )

    return mediaConstraints.apply {
      with(optional) {
        add(MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"))
        addAll(items)
      }
    }
  }

  private fun setupAudio() {
    logger.d { "[setupAudio] #sfu; no args" }
    audioHandler.start()
    audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val devices = audioManager?.availableCommunicationDevices ?: return
      val speakerDevice = devices.firstOrNull {
        it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER
      }
      speakerDevice?.let {
        val result = audioManager?.setCommunicationDevice(it)
      }
    } else {
      @Suppress("DEPRECATION")
      audioManager?.isSpeakerphoneOn = true
    }
  }


}
