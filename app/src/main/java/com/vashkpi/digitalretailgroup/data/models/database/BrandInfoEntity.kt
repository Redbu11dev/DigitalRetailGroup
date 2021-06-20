package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brand_info")
data class BrandInfoEntity(
    @PrimaryKey
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegionEntity>
)
