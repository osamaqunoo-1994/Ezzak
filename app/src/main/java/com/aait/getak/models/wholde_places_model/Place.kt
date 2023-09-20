package com.aait.getak.models.wholde_places_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Place(
    var distance: String?,
    var icon: String?,
    var lat: Double?,
    var lng: Double?,
    var name: String?,
    @SerializedName("place_id")
    var placeId: String?,
    var reference: String?,
    var vicinity: String?,
    var infav: Boolean?=false
):Serializable