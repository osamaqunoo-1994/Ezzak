package com.aait.getak.models

import java.io.Serializable

data class Place(
    val distance: String?,
    val icon: String?,
    val lat: Double?,
    val lng: Double?,
    val name: String?,
    val opening_hours: List<String>?,
    val place_id: String?,
    val reference: String?,
    val cover: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val rate: String?,
    val num_rating: String?,
    val num_comments: String?,
    val website: String?,
    val open_from: String?,
    val open_to: String?,
    val place_open: String?,
    val id: Int,
    val vicinity: String?
):Serializable