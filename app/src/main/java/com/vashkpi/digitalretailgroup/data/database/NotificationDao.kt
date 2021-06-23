package com.vashkpi.digitalretailgroup.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications")
    fun getAll(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(brands: List<NotificationEntity>)

    @Query("SELECT * FROM notifications")
    fun pagingSource(): PagingSource<Int, NotificationEntity>

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Query("UPDATE notifications set read = :read WHERE notification_id == :notificationId")
    suspend fun setRead(notificationId: String, read: Boolean)
}