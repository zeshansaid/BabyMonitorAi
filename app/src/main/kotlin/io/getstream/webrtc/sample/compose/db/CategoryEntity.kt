package io.getstream.webrtc.sample.compose.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
  val score: Float,
  @PrimaryKey(autoGenerate = false)
  val index: Int,
  val categoryName: String,
  val displayName: String
)
