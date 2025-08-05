package io.getstream.webrtc.sample.compose.ui.screens.video

import org.webrtc.VideoFrame
import org.webrtc.VideoSink


class VideoFrameSink(
  private val onFrameReceived: (VideoFrame) -> Unit
) : VideoSink {
  override fun onFrame(frame: VideoFrame) {
    onFrameReceived(frame)
    // You can process the frame here (e.g., convert to Bitmap, analyze, etc.)
  }
}

