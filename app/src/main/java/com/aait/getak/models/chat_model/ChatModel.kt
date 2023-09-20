package com.aait.getak.models.chat_model

data class ChatModel(
    val `data`: Data,
    val key: String? = null,
    val value: String? = null

)
data class Data(
    val messages: List<Message>,
    val order: Order?,
    val reasons: List<Reason>?,
    val seconduser: Seconduser?,
    val yourinfo: Yourinfo?
)

data class Message(
    val avatar: String?="",
    val content: String?="",
    val date: String?="",
    val seen: String?="",
    val sender: String?="",
    val type: String?="",
    val user_id: Int?=0,
    val username: String?=""
)
data class Order(
    val end_address: String?,
    val end_lat: String?,
    val end_long: String?,
    val id: Int?,
    val start_address: String?,
    val start_lat: String?,
    val start_long: String?,
    val time_zone: String?
)

data class Reason(
    val id: Int?,
    val reason: String?
)
data class Seconduser(
    val avatar: String?,
    val id: Int?,
    val lat: Double?,
    val long: Double?,
    val name: String?,
    val phone: String?,
    val rate: Float?,
    val type: String?
)


data class Yourinfo(
    val id: Int?,
    val is_captain: Boolean?,
    val lat: Double?,
    val long: Double?,
    val name: String?
)