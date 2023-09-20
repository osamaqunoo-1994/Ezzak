package com.aait.getak.models.store_details_model

data class Store(
    val address: String?,
    val cover: String?,
    val distance: String?,
    val email: String?,
    val icon: String?,
    val id: Int?,
    val lat: Double?,
    val lng: Double?,
    val menucategories: List<MenucategoryX>?,
    val name: String?,
    val num_comments: Int?,
    val num_rating: Int?,
    val open_from: String?,
    val open_to: String?,
    val parent_id: Int?,
    val phone: String?,
    val place_open: Boolean?,
    val rate: String?,
    val website: String?
)