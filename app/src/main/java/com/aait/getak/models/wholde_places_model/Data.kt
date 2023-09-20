package com.aait.getak.models.wholde_places_model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("next_page_token")
    var nextPageToken: String?,
    @SerializedName("places")
    var nearestPlaces: List<Place>,
    @SerializedName("saved_places")
    var savedPlaces: List<SavedPlace>,
  @SerializedName("start_addresses")
    var start_addresses: List<SavedPlace>,
    @SerializedName("end_addresses")
    var end_addresses: List<SavedPlace>

)