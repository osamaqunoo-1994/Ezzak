package com.aait.getak.models.captin_model


import com.google.gson.annotations.SerializedName

data class Data(
    var avatar: String?,
    @SerializedName("car_brand")
    var carBrand: String?,
    @SerializedName("car_image")
    var carImage: String?,
    @SerializedName("car_number")
    var carNumber: String?,
    @SerializedName("car_color")
    var carColor: String?,
    @SerializedName("end_address")
    var endAddress: String?,
    @SerializedName("end_lat")
    var endLat: Double?,
    @SerializedName("end_long")
    var endLong: Double?,
    @SerializedName("expected_period")
    var expectedPeriod: String?,
    var id: Int?,
    var name: String?,
    var phone: String?,
    var rate: String?,
    @SerializedName("start_address")
    var startAddress: String?,
    @SerializedName("start_lat")
    var startLat: Double?,
    @SerializedName("start_long")
    var startLong: Double?,
    @SerializedName("service_type")
    var serviceType:String?,
    @SerializedName("service_in")
    var serviceIn:String?,
    @SerializedName("car_type_id")
    var carTypeId:Int,
    var captain_id:Int,
    @SerializedName("captain_lat")
    var captain_lat:Double?,
    @SerializedName("captain_long")
    var captain_lng:Double?,
    var captain_status:String?,
    var conversation_id:String?,
    var payment_type:String?,
    var order_type:String?="trip",
    var join_order:Boolean?,
    var notes:String?,
    var total_required_price:String?
)