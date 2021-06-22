package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Store

fun StoreEntity.asDomainModel(): Store {
    return Store(
        name,
        store_id,
        image_parth,
        order,
        address
    )
}

@Entity(tableName = "stores")
data class StoreEntity (
    @PrimaryKey(autoGenerate = true)
    val entity_id: Long,

    val brand_id: String,
    val region_id: String,

    val name: String,
    val store_id: String,
    val image_parth: String?, //typo on the server side
    val order: Int,
    val address: String
)