package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.StoreEntity
import com.vashkpi.digitalretailgroup.data.models.database.StoreInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreInfoDao {

    @Query("SELECT * FROM store_info WHERE store_id = :storeId")
    fun getOne(storeId: String): Flow<StoreInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(item: StoreInfoEntity)
}