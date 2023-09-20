package com.aait.getak.models.bill_model


import com.google.gson.annotations.SerializedName

data class Data(
    var avatar: String?,
    @SerializedName("payment_type")
    var paymentType: String?,
    var price: String?,
    @SerializedName("captain_id")
    var user_id:Int
)