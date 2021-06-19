package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.network.dto.BrandInfoRegionDto

data class BrandInfoResponse(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: ArrayList<BrandInfoRegionDto>
)
