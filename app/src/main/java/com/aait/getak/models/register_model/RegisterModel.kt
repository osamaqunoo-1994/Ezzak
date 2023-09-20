package com.aait.getak.models.register_model


data class RegisterModel(
    var `data`: UserModel?,
    var code: Int?,
    var key: String?,
    var value: String?,
    var msg:String?
)