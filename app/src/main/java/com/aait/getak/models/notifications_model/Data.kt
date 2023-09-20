package com.aait.getak.models.notifications_model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("num_notifications")
    var numNotifications: Int?
)