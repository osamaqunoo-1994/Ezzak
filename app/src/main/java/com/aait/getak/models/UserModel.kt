package com.aait.getak.models


import com.google.gson.annotations.SerializedName

data class  UserModel(
    var active: String,
    var available: String,
    var avatar: String,
    var balance: String,
    @SerializedName("birth_date")
    var birthDate: String,
    var captain: String,
    var code: String,
    var currency: String,
    @SerializedName("device_id")
    var deviceId: String,
    var email: String,
    var gender: String,
    var googlekey: String,
    var id: Int,
    var name: String,
    var phone: String,
    var phonekey: String,
    @SerializedName("pin_code")
    var pinCode: String,
    var plan: String,
    var rate: String,
    @SerializedName("time_zone")
    var timeZone: String,
    @SerializedName("share_code")
    var shareCode: String,
    var token: String,
    @SerializedName("elm_status")
    val elmStatus:String?
)