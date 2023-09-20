package com.aait.getak.models


import com.google.gson.annotations.SerializedName

data class DataXX(
    @SerializedName("invite_client_balance")
    var inviteClientBalance: String?,
    @SerializedName("share_code")
    var shareCode: String?,
    val currency:String?
)