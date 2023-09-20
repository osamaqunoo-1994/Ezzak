package com.aait.getak.models

import java.io.Serializable

open class BaseResponse : Serializable {

    private val key: String? = null

      val value: String? = null


    // private val value: String? = null

    var msg: String? = null

    var code: Int? = null


    val isSuccessfull: Boolean
        get() = key == "success"

    val isValue : Boolean
        get() = value == "1"

    val isCode : Boolean
        get() = code == 200


    var user_status: String? = null
    var device_exists: Boolean = true
    var uuid: String? = null


}
