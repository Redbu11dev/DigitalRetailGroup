package com.vashkpi.digitalretailgroup.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notifications")
data class NotificationEntity (
    @PrimaryKey
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
)