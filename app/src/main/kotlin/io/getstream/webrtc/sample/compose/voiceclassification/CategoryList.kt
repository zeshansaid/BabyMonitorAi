package io.getstream.webrtc.sample.compose.voiceclassification


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.mediapipe.tasks.components.containers.Category

@Composable
fun CategoryList(categories: List<CategoryDTO>) {
    val listState = rememberLazyListState()

    // Scroll to "top" because list is reversed
    LaunchedEffect(categories.size) {
        if (categories.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = Modifier.fillMaxSize()
    ) {
        items(categories) { category ->
            CategoryItem(category)
        }
    }

}




@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    val categories = listOf(
      CategoryDTO(0.7f, 0, "Speech", "Human Voice"),
      CategoryDTO(0.5f, 1, "Music", "Instrumental"),
      CategoryDTO(0.3f, 2, "Noise", "Background Noise")
    )
    CategoryList(categories)
}