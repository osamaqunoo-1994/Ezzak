package com.aait.getak.models.user_locale_model


import com.google.gson.annotations.SerializedName

data class Data(
    var active: String?,
    var available: String?,
    var captain: String?,
    @SerializedName("country_id")
    var countryId: Int?,
    var currency: String?,
    var lat: Double?,
    var long: Double?,
    var month: String?
)