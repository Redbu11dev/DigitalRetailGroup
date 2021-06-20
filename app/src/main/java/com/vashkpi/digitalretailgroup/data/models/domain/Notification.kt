package com.vashkpi.digitalretailgroup.data.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Notification (
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
)