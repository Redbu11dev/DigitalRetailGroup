package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.StoreEntity
import com.vashkpi.digitalretailgroup.data.models.database.StoreInfoEntity

fun StoreInfoResponseDto.asDatabaseModel(store_id: String): StoreInfoEntity {
    return StoreInfoEntity(
        store_id,

        name,
        image_parth,
        website,
        time_of_work,
        telephone,
        address,
        description,
    )
}

data class StoreInfoResponseDto (
    val name: String,
    val image_parth: String?, //typo on the server side
    val website: String,
    val time_of_work: String,
    val telephone: String,
    val address: String,
    val description: String
)