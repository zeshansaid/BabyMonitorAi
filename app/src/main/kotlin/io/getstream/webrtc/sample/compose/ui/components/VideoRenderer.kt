package io.getstream.webrtc.sample.compose.ui.components


import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.getstream.webrtc.sample.compose.ui.screens.video.VideoFrameSink
import io.getstream.webrtc.sample.compose.webrtc.sessions.LocalWebRtcSessionManager
import org.webrtc.RendererCommon
import org.webrtc.VideoFrame
import org.webrtc.VideoTrack

/**
 * Renders a single video track based on the call state.
 *
 * @param videoTrack The track containing the video stream for a given participant.
 * @param modifier Modifier for styling.
 */

@Composable
fun VideoRenderer(
  videoTrack: VideoTrack,
  modifier: Modifier = Modifier,
  onFrameReceived: (VideoFrame) -> Unit = {}, // Callback for frames 1.
) {

  val trackState: MutableState<VideoTrack?> = remember { mutableStateOf(null) }
  var view: VideoTextureViewRenderer? by remember { mutableStateOf(null) }
  val frameCapturingSink = remember { VideoFrameSink(onFrameReceived) }

  DisposableEffect(videoTrack) {
    onDispose {
      cleanTrack(view, trackState)
      videoTrack.removeSink(frameCapturingSink) // Clean up the frame sink //3.
    }
  }

  val sessionManager = LocalWebRtcSessionManager.current
  AndroidView(
    factory = { context ->
    VideoTextureViewRenderer(context).apply {
      init(
        sessionManager.peerConnectionFactory.eglBaseContext,
        object : RendererCommon.RendererEvents {
          override fun onFirstFrameRendered() = Unit
          override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) = Unit
        })
      setupVideo(trackState, videoTrack, this)
      view = this
      videoTrack.addSink(frameCapturingSink) // Add the frame sink 4.
    }
  }, update = { v -> setupVideo(trackState, videoTrack, v) }, modifier = modifier
  )
}

private fun cleanTrack(
  view: VideoTextureViewRenderer?, trackState: MutableState<VideoTrack?>
) {
  view?.let { trackState.value?.removeSink(it) }
  trackState.value = null
}

private fun setupVideo(
  trackState: MutableState<VideoTrack?>, track: VideoTrack, renderer: VideoTextureViewRenderer
) {
  if (trackState.value == track) {
    return
  }

  cleanTrack(renderer, trackState)
  trackState.value = track
  track.addSink(renderer)
}
