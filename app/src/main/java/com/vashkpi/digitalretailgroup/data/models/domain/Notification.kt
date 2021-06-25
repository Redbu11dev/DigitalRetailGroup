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

fun String.formatDateToUserReadable(): String {
    val year = subSequence(0, 4)
    val month = subSequence(4, 6)
    val day = subSequence(6, 8)

    val hours = subSequence(8, 10)
    val minutes = subSequence(10, 12)
    val seconds = subSequence(12, 14)

    return "$year-$month-$day    $hours:$minutes:$seconds"
}