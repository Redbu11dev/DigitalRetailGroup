package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Brand

fun BrandEntity.asDomainModel(): Brand {
    return Brand(
        name,
        brand_id,
        image_parth,
        order
    )
}

@Entity(tableName = "brands")
data class BrandEntity (
    val name: String,
    @PrimaryKey
    val brand_id: String,
    val image_parth: String, //typo on the server side
    val order: Int
)