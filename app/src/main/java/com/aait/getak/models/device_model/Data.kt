package com.aait.getak.models.device_model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("device_id")
    var deviceId: String?,
    var lang: String?,
    @SerializedName("orders_notify")
    var ordersNotify: String?,
    @SerializedName("show_ads")
    var showAds: String?
)