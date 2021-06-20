package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfo
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion

fun BrandInfoEntityFull.asDomainModel(): BrandInfo {
    return BrandInfo(
        brandInfoEntity.name,
        brandInfoEntity.website,
        brandInfoEntity.telephone,
        brandInfoEntity.time_of_work,
        regions.map {
            it.asDomainModel()
        }
    )
}

data class BrandInfoEntityFull(
//    val name: String,
//    val website: String,
//    val telephone: String,
//    val time_of_work: String,
//    val regions: List<BrandInfoRegionEntity>
    @Embedded val brandInfoEntity: BrandInfoEntity,
    @Relation(
        parentColumn = "brand_id",
        entityColumn = "brand_id"
    )
    val regions: List<BrandInfoRegionEntity>
)

@Entity(tableName = "brand_info")
data class BrandInfoEntity(
    @PrimaryKey
    val brand_id: String, //custom

    val name: String,
    val website: String,
    val telephone: String,
    val time_of_work: String
)

fun BrandInfoRegionEntity.asDomainModel(): BrandInfoRegion {
    return BrandInfoRegion(
        name,
        region_id,
        order
    )
}

//@Entity(tableName = "brand_info_region", foreignKeys = [ForeignKey(
//    entity = BrandInfoEntity::class,
//    parentColumns = arrayOf("name"),
//    childColumns = arrayOf("parent_name"),
//    onDelete = CASCADE
//)])
@Entity(tableName = "brand_info_region")
data class BrandInfoRegionEntity(
    val brand_id: String,//custom

    val name: String,
    @PrimaryKey
    val region_id: String,
    val order: Int
)
