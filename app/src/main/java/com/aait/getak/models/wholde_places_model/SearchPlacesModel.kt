package com.aait.getak.models.wholde_places_model


import com.google.gson.annotations.SerializedName

data class SearchPlacesModel(
    @SerializedName("data")
    var `data`: DataX?,
    var code: Int?,
    var key: String?,
    var value: String?,
    var msg:String?
)