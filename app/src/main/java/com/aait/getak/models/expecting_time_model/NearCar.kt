package com.aait.getak.models.expecting_time_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NearCar(var id:Int,
                   @SerializedName("lat")
                   var lat: Double,
                   @SerializedName("long")
                   var lng: Double,
                   var distance:Float
                   ):Serializable
