package com.aait.getak.models

import java.io.Serializable

data class DataXXX(
    val next_page_token: String?,
    val page: String?,
    val name: String?,
    val places: List<Place>?
):Serializable