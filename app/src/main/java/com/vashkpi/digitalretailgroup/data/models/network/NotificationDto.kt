package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity
import com.vashkpi.digitalretailgroup.data.models.domain.Notification

fun NotificationDto.asDatabaseModel(): NotificationEntity {
    return NotificationEntity(
        notification_id = notification_id,
        date = date,
        title = title,
        text = text,
        read = read,

        local_user_removed = false,
        local_user_read = false
    )
}

fun NotificationDto.asDomainModel(): Notification {
    return Notification(
        notification_id = notification_id,
        date = date,
        title = title,
        text = text,
        read = read
    )
}

data class NotificationDto (
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
)