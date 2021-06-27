package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntityFull
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoRegionEntity
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfo
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion

fun BrandInfoResponseDto.asDatabaseModel(brand_id: String): BrandInfoEntityFull {
    return BrandInfoEntityFull(
        BrandInfoEntity(
            brand_id,//custom

            name,
            website,
            telephone,
            time_of_work
        ),
        regions.mapIndexed { index, regionDto ->
            BrandInfoRegionEntity(
                "${index}_${brand_id}",//custom

                brand_id,//custom

                regionDto.name,
                regionDto.region_id,
                regionDto.order,
            )
        }
    )
}

fun BrandInfoResponseDto.asDomainModel(): BrandInfo {
    return BrandInfo(
        name,
        website,
        telephone,
        time_of_work,
        regions.map {regionDto ->
            BrandInfoRegion(
                regionDto.name,
                regionDto.region_id,
                regionDto.order,
            )
        }
    )
}

data class BrandInfoResponseDto(
    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String,
    val regions: List<BrandInfoRegionDto>
)
