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

    @Query("SELECT * FROM notifications WHERE not local_user_removed")
    fun getAllNotRemoved(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(brands: List<NotificationEntity>)

    @Query("SELECT * FROM notifications WHERE not local_user_removed")
    fun pagingSourceOfNotRemoved(): PagingSource<Int, NotificationEntity>

    @Query("DELETE FROM notifications WHERE not local_user_removed")
    suspend fun clearAll()

    @Query("UPDATE notifications SET read = :read, local_user_read = :read WHERE notification_id == :notificationId")
    suspend fun setRead(notificationId: String, read: Boolean)

    @Query("UPDATE notifications SET local_user_removed = :removed WHERE notification_id == :notificationId")
    suspend fun markUserRemoved(notificationId: String, removed: Boolean)

}