package io.getstream.webrtc.sample.compose.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(categories: List<CategoryEntity>)

  @Query("SELECT * FROM categories")
  fun getAllCategories(): Flow<List<CategoryEntity>>
}
