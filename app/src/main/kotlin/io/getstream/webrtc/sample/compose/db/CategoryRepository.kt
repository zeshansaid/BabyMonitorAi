package io.getstream.webrtc.sample.compose.db

import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val dao: CategoryDao) {

  suspend fun insertCategories(categories: List<CategoryEntity>) {
    dao.insertAll(categories)
  }

  fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()
}
