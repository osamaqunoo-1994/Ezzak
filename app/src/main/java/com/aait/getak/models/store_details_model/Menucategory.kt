package com.aait.getak.models.store_details_model

data class Menucategory(
    val id: Int?,
    val name: String?,
    val products: List<Product>?,
    val store_id: Int?
)