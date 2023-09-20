package com.aait.getak.models.car_filter_model


import com.google.gson.annotations.SerializedName

data class Data(
    var id: Int?,
    var image: String?,
    @SerializedName("max_weight")
    var maxWeight: String?,
    var name: String?,
    var expected_price: String?,
    var currency: String?,
    var price_id: Int,
    @SerializedName("num_persons")
    var numPersons: Int?,
    var type: String?
)