package com.vashkpi.digitalretailgroup.data.models.database

data class BrandEntity (
    val name: String,
    val brand_id: String,
    val image_parth: String, //typo on the server side
    val order: Int
)