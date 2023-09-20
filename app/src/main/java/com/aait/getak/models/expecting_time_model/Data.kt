package com.aait.getak.models.expecting_time_model


import com.google.gson.annotations.SerializedName

data class Data(
    var distance: Float?=0F,
    var time: Int?,
    @SerializedName("captains")
    var captains:List<NearCar>
)