package com.vashkpi.digitalretailgroup.data.models.domain

data class BrandInfo(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegion>
)
