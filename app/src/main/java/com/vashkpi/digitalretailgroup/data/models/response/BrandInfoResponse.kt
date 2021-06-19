package com.vashkpi.digitalretailgroup.data.models.response

import com.vashkpi.digitalretailgroup.data.models.BrandInfoRegion

data class BrandInfoResponse(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: ArrayList<BrandInfoRegion>
)
