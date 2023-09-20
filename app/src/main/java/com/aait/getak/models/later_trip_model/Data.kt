package com.aait.getak.models.later_trip_model

data class Data(
    val avatar: String,
    val can_update: Boolean,
    val car_brand: String,
    val car_number: String,
    val cartype: String,
    val currency: String,
    val date: String,
    val end_address: String,
    val end_lat: String,
    val end_long: String,
    val expected_price: String,
    val id: Int,
    val later: Boolean,
    val later_order_date: String,
    val later_order_time: String,
    val month: String,
    val name: String,
    val payment_type: String,
    val price: String,
    val rate: Double,
    val start_address: String,
    val start_lat: String,
    val start_long: String,
    val time: String
)