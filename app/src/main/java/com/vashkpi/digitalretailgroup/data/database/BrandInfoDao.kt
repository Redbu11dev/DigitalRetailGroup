package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandInfoDao {

    @Query("SELECT * FROM brand_info")
    fun getOne(): Flow<BrandInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(item: BrandInfoEntity)
}