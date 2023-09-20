package com.aait.getak.models.order_details_model


data class OrderDetailsModel(
    var `data`: Data?,
    var code: Int?,
    var key: String?,
    var value: String?,
    var msg:String,

    var user_status: String?,
    var device_exists: Boolean
)