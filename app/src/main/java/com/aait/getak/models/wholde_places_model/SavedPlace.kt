package com.aait.getak.models.wholde_places_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SavedPlace(
    @SerializedName(value = "address", alternate = ["start_address","end_address"])

    var address: String?,
    var distance: String?,
    var id: Int?,
    @SerializedName(value = "lat", alternate = ["start_lat","end_lat"])
    var lat: Double?,
    @SerializedName(value = "long", alternate = ["start_long","end_long"])
    var long: Double?,
    var name: String?,
    @SerializedName("place_id")
    var placeId:String?,
    var infav: Boolean?=false
):Serializable