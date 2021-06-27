package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {

    @Query("SELECT * FROM brands")
    fun getAll(): Flow<List<BrandEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(items: List<BrandEntity>)

    @Query("DELETE FROM brands")
    fun clearAll()
}