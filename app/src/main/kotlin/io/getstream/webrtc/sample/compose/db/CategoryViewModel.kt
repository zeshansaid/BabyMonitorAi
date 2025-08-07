package io.getstream.webrtc.sample.compose.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.getstream.webrtc.sample.compose.voiceclassification.CategoryDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

  private val _isLoading = MutableStateFlow(true)
  val isLoading: StateFlow<Boolean> = _isLoading

  private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
  val categories: StateFlow<List<CategoryEntity>> = _categories


  init {
    loadCategories()
  }

  private fun loadCategories() {
    viewModelScope.launch {
      _isLoading.value = true
      repository.getAllCategories().collect { result ->
        _categories.value = result
        _isLoading.value = false
      }
    }
  }

  fun insertCategoryDTOs(dtoList: List<CategoryDTO>) {
    val entities = dtoList.map {
      CategoryEntity(
        score = it.score,
        index = it.index,
        categoryName = it.categoryName,
        displayName = it.displayName,
        timestamp = it.timestamp
      )
    }
    viewModelScope.launch {
      repository.insertCategories(entities)
    }
  }
}
