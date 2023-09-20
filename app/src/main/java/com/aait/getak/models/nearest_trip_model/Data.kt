package com.aait.getak.models.nearest_trip_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("available_car_persons")
    var availableCarPersons: Int?,
    var avatar: String?,
    @SerializedName("car_image")
    var carImage: String?,
    @SerializedName("car_type")
    var carType: String?,
    @SerializedName("car_type_id")
    var carTypeId: Int?,
    @SerializedName("current_lat")
    var currentLat: Double?,
    @SerializedName("current_long")
    var currentLong: Double?,
    @SerializedName("current_order_persons")
    var currentOrderPersons: Int?,
    var distance: String?,
    @SerializedName("end_address")
    var endAddress: String?,
    @SerializedName("end_lat")
    var endLat: Double?,
    @SerializedName("end_long")
    var endLong: Double?,
    var id: Int?,
    @SerializedName("max_car_persons")
    var maxCarPersons: Int?,
    var name: String?,
    var phone: String?,
    @SerializedName("service_in")
    var serviceIn: String?,
    @SerializedName("service_type")
    var serviceType: String?,
    @SerializedName("start_address")
    var startAddress: String?,
    @SerializedName("start_lat")
    var startLat: Double?,
    @SerializedName("start_long")
    var startLong: Double?,
    @SerializedName("expected_period")
    var time:String?="",
    @SerializedName("car_brand")
    var car_model:String?="",
    var rate:Float=0f,
    @SerializedName("car_number")
    var carPlate:String?="",
    var type: String?
):Serializable