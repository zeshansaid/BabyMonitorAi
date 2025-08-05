package io.getstream.webrtc.sample.compose.voiceclassification

data class CategoryDTO(
  val score: Float,
  val index: Int,
  val categoryName: String,
  val displayName: String
)
