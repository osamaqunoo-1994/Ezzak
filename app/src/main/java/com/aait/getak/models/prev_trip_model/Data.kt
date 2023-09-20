package com.aait.getak.models.prev_trip_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    var avatar: String?,
    var currency: String?,
    var date: String?,
    @SerializedName("end_address")
    var endAddress: String?,
    @SerializedName("end_lat")
    var endLat: String?,
    @SerializedName("end_long")
    var endLong: String?,
    var id: Int?,
    var month: String?,
    var name: String?,
    var price: String?,
    @SerializedName("start_address")
    var startAddress: String?,
    var later:Boolean,
    @SerializedName("start_lat")
    var startLat: Double?,
    @SerializedName("start_long")
    var startLong: Double?,
    var time: String?,
    var can_update:Boolean?,
    var later_order_date:String?,
    var later_order_time:String?,
    var cartype:String?

):Serializable