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

    @Query("SELECT * FROM notifications WHERE local_user_removed or local_user_read")
    fun getAllUserModified(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(brands: List<NotificationEntity>)

    @Query("SELECT * FROM notifications WHERE not local_user_removed")
    fun pagingSourceOfNotRemoved(): PagingSource<Int, NotificationEntity>

    @Query("DELETE FROM notifications WHERE not local_user_removed or not local_user_read")
    suspend fun clearAll()

    @Query("UPDATE notifications SET read = :read, local_user_read = :local_user_read WHERE notification_id == :notificationId")
    suspend fun setRead(notificationId: String, read: Boolean, local_user_read: Boolean)

    @Query("UPDATE notifications SET local_user_read = :local_user_read WHERE notification_id == :notificationId")
    suspend fun setLocalUserRead(notificationId: String, local_user_read: Boolean)

    @Query("UPDATE notifications SET local_user_removed = :removed WHERE notification_id == :notificationId")
    suspend fun markUserRemoved(notificationId: String, removed: Boolean)

    /**
     * This will completely delete the entry
     */
    @Query("DELETE FROM notifications WHERE notification_id == :notificationId")
    suspend fun deleteOne(notificationId: String)

}