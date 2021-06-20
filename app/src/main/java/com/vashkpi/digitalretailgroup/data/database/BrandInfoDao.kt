package com.vashkpi.digitalretailgroup.data.database

import androidx.room.*
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntityFull
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoRegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandInfoDao {

    @Transaction
    @Query("SELECT * FROM brand_info WHERE brand_id = :brand_id")
    fun getOne(brand_id: String): Flow<BrandInfoEntityFull>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertBrandInfoEntity(entity: BrandInfoEntity, regions: List<BrandInfoRegionEntity>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertBrandInfoRegionEntities()

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertOne(item: BrandInfoEntity)

}