package com.vashkpi.digitalretailgroup.data.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Brand

data class StoreInfo (
    val name: String,
    val image_parth: String, //typo on the server side
    val website: String,
    val time_of_work: String,
    val telephone: String,
    val address: String,
    val description: String
)