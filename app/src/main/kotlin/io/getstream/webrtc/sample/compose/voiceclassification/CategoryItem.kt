package io.getstream.webrtc.sample.compose.voiceclassification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mediapipe.tasks.components.containers.Category
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun CategoryItem(category: CategoryDTO) {
  val primaryColors = listOf(Color.Blue, Color.Cyan, Color.Red)
  val backgroundColors = listOf(Color.LightGray, Color.Gray, Color.Black)

  val index = category.index % primaryColors.size
  val score = category.score

  val primaryColor = primaryColors[index]
  val backgroundColor = backgroundColors[index]

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.Transparent // transparent background
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(
      modifier = Modifier
        .padding(12.dp)
    ) {
      Text(
        text = category.categoryName,
        color = primaryColor
      )
      LinearProgressIndicator(
        progress = { score },
        modifier = Modifier
          .fillMaxWidth()
          .height(32.dp)
          .padding(top = 8.dp),
        color = primaryColor,
        trackColor = backgroundColor,
        strokeCap =  StrokeCap.Butt
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryItem() {
  val sampleCategoryDTO = CategoryDTO(
    score = 0.5f,
    index = 9,
    categoryName = "Speech",
    displayName = "Human Voice"
  )
  CategoryItem(sampleCategoryDTO)
}
