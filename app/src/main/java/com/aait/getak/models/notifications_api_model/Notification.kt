package com.aait.getak.models.notifications_api_model

data class Notification(
    val captain_status: String?,
    val data: OrderData?,
    val date: String?,
    val id: Int?,
    val image: String?,
    val key: String?,
    val name: String?,
    val order_status: String?,
    val seen: String?,
    val text: String?,
    val title: String?,
    val user_id: Int?
)
data class OrderData(val order_id:String,val user_id: String?)