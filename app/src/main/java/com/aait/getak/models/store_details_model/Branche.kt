package com.aait.getak.models.store_details_model

data class Branche(
    val address: String?,
    val distance: String?,
    val id: Int?,
    val lat: Double?,
    val lng: Double?,
    val menucategories: List<Menucategory>?,
    val open_from: String?,
    val open_to: String?,
    val place_open: Boolean?
)