package com.aait.getak.models.notification_model


data class NotificationModel(
    var `data`: List<Data>,
    var code: Int?,
    var key: String?,
    var msg:String,
    var value: String?
)