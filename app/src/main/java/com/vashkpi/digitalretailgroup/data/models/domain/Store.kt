package com.vashkpi.digitalretailgroup.data.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Brand

data class Store (
    val name: String,
    val store_id: String,
    val image_parth: String, //typo on the server side
    val order: Int,
    val address: String
)