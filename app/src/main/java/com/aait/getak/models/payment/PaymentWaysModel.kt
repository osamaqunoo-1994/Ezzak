package com.aait.getak.models.payment


data class PaymentWaysModel (
    var `data`: MutableList<PaymentWaysData>,
    var code: Int?,
    var key: String?,
    var value: String?,
    var msg:String,
)

data class PaymentWaysData(
    val title: String? = null,
    val type: String? = null,
    val image: String? = null
)