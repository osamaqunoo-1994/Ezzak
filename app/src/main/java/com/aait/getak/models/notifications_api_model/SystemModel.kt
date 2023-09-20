package com.aait.getak.models.notifications_api_model

data class SystemModel(
    val code: Int?,
    val `data`: MutableList<Notification>?,
    val key: String?,
    val value: String?
)