package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.BrandInfoEntity
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity

@Database(
    entities = [BrandEntity::class, BrandInfoEntity::class, NotificationEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun brandDao(): BrandDao

    abstract fun brandInfoDao(): BrandInfoDao

    abstract fun notificationDao(): NotificationDao

}