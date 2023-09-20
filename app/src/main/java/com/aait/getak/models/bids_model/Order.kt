package com.aait.getak.models.bids_model


import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("end_address")
    var endAddress: String?,
    @SerializedName("end_lat")
    var endLat: Double?,
    @SerializedName("end_long")
    var endLong: Double?,
    var id: Int?,
    @SerializedName("start_address")
    var startAddress: String?,
    @SerializedName("start_lat")
    var startLat: Double?,
    @SerializedName("start_long")
    var startLong: Double?
)