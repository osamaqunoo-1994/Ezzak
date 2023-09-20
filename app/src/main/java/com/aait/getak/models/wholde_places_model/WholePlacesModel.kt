package com.aait.getak.models.wholde_places_model


import com.google.gson.annotations.SerializedName

data class WholePlacesModel(
    @SerializedName("data")
    var wholePlaces: Data?,
    var code: Int?,
    var key: String?,
    var value: String?,
    var msg:String?
)