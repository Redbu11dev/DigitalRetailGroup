package com.vashkpi.digitalretailgroup.data.models.network

import android.os.Parcelable
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.StoreEntity
import com.vashkpi.digitalretailgroup.data.models.domain.Store
import kotlinx.parcelize.Parcelize

fun StoreDto.asDatabaseModel(brand_id: String,
                             region_id: String): StoreEntity {
    return StoreEntity(
        brand_id,
        region_id,

        name,
        store_id,
        image_parth,
        order,
        address
    )
}

fun StoreDto.asDomainModel(): Store {
    return Store(
        name,
        store_id,
        image_parth,
        order,
        address
    )
}

@Parcelize
data class StoreDto (
    val name: String,
    val store_id: String,
    val image_parth: String?, //typo on the server side
    val order: Int,
    val address: String
) : Parcelable