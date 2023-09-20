package com.aait.getak.models.categories_model

data class Data(
    var cars: List<Car>,
    var distance: String?,
    var time: String?,
    var use_balance_first: Boolean,
    var have_visa: Boolean?,
    var max_car_persons: Int?,
    var current_order_persons: Int?,
    var available_car_persons: Int?,
    var cheaper_way: String?,
    var balance:String?



)