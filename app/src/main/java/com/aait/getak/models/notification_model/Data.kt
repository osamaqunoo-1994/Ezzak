package com.aait.getak.models.notification_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    var id:Int,
    @SerializedName("image")
    var img:String,
    var title:String,
    @SerializedName("notes")
    var desc:String,
    var expired:Boolean,
    var date:String
):Serializable