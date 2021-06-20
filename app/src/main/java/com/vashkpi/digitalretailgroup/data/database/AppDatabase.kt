package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vashkpi.digitalretailgroup.data.models.database.*

@Database(
    entities = [
        BrandEntity::class,
        BrandInfoEntity::class,
        BrandInfoRegionEntity::class,
        StoreEntity::class,
        StoreInfoEntity::class,
        NotificationEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun brandDao(): BrandDao

    abstract fun brandInfoDao(): BrandInfoDao

    abstract fun storeDao(): StoreDao

    abstract fun storeInfoDao(): StoreInfoDao

    abstract fun notificationDao(): NotificationDao

}