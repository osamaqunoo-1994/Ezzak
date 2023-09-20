package com.aait.getak.models.bids_model


import com.google.gson.annotations.SerializedName

data class Bid(
    @SerializedName("allow_choose_bid")
    var allowChooseBid: Boolean?,
    var avatar: String?,
    @SerializedName("bid_id")
    var bidId: Int?,
    var currency: String?,
    var name: String?,
    var phone: String?,
    var price: String?,
    var rate: Float?
)