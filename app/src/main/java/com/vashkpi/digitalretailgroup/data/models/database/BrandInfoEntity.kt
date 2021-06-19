package com.vashkpi.digitalretailgroup.data.models.database

data class BrandInfoEntity(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegionEntity>
)
