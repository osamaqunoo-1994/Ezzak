package com.aait.getak.models.notification_model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("order_id")
    var orderId: String?
)