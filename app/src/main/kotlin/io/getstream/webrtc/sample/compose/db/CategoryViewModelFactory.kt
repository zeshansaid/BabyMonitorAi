package io.getstream.webrtc.sample.compose.db

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategoryViewModelFactory(context: Context) : ViewModelProvider.Factory {
  private val database = AppDatabase.getDatabase(context)
  private val repository = CategoryRepository(database.categoryDao())

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return CategoryViewModel(repository) as T
  }
}
