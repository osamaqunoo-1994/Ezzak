package com.aait.getak.models.notifications_api_model

import com.aait.getak.models.notifications_api_model.Notification


data class Data(
    val all_pages: Int?,
    val current_page: String?,
    val next_page: String?,
    val notifications: List<Notification>?,
    val per_page: String?
)