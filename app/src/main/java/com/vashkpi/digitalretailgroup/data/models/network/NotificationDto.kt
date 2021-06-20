package com.vashkpi.digitalretailgroup.data.models.network

import com.vashkpi.digitalretailgroup.data.models.database.BrandEntity
import com.vashkpi.digitalretailgroup.data.models.database.NotificationEntity

fun NotificationDto.asDatabaseModel(): NotificationEntity {
    return NotificationEntity(
        notification_id,
        date,
        title,
        text,
        read
    )
}

data class NotificationDto (
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
)