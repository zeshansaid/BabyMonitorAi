package io.getstream.webrtc.sample.compose.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.webrtc.sample.compose.voiceclassification.CategoryDTO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

  val categories = repository.getAllCategories()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun insertCategoryDTOs(dtoList: List<CategoryDTO>) {
    val entities = dtoList.map {
      CategoryEntity(
        score = it.score,
        index = it.index,
        categoryName = it.categoryName,
        displayName = it.displayName
      )
    }
    viewModelScope.launch {
      repository.insertCategories(entities)
    }
  }
}
