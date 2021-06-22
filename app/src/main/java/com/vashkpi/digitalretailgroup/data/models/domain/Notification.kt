package com.vashkpi.digitalretailgroup.data.models.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification (
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
): Parcelable