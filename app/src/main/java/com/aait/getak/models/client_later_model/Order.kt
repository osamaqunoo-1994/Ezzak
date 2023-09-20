package com.aait.getak.models.client_later_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("can_update")
    var canUpdate: Boolean?,
    var date: String?,
    @SerializedName("end_address")
    var endAddress: String?,
    @SerializedName("end_lat")
    var endLat: Double?,
    @SerializedName("end_long")
    var endLong: Double?,
    var id: Int?,
    @SerializedName("later_order_date")
    var laterOrderDate: String?,
    @SerializedName("later_order_time")
    var laterOrderTime: String?,
    @SerializedName("start_address")
    var startAddress: String?,
    @SerializedName("start_lat")
    var startLat: Double?,
    @SerializedName("start_long")
    var startLong: Double?

):Serializable