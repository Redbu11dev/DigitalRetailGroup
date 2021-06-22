package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Store
import com.vashkpi.digitalretailgroup.data.models.domain.StoreInfo

fun StoreInfoEntity.asDomainModel(): StoreInfo {
    return StoreInfo(
        name,
        image_parth,
        website,
        time_of_work,
        telephone,
        address,
        description
    )
}

@Entity(tableName = "store_info")
data class StoreInfoEntity (
    @PrimaryKey(autoGenerate = true)
    val entity_id: Long,//custom

    val store_id: String,//custom

    val name: String,
    val image_parth: String?, //typo on the server side
    val website: String,
    val time_of_work: String,
    val telephone: String,
    val address: String,
    val description: String
)