package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Notification

fun NotificationEntity.asDomainModel(): Notification {
    return Notification(
        notification_id,
        date,
        title,
        text,
        read
    )
}

@Entity(tableName = "notifications")
data class NotificationEntity (
    @PrimaryKey
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean,

    val local_user_removed: Boolean, //custom
    val local_user_read: Boolean //custom
)