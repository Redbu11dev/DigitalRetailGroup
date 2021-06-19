package com.vashkpi.digitalretailgroup.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity

@Database(
    entities = [BrandEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun brandDao(): BrandDao

}