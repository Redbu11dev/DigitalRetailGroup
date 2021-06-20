package com.vashkpi.digitalretailgroup.data.models.network

data class NotificationsResponseDto (
    val notifications: List<NotificationDto>,
    val page_current: Int,
    val page_next: String
)