package com.aait.getak.models

import java.io.Serializable

data class StoreModel(
    val code: Int?,
    val `data`: NearData,
    val key: String?,
    val msg: String?,
    val value: String?
):Serializable

data class NearData(val next_page_token: String?,
                    val page: String?,
                    val name: String?,
                    val places: List<Place>?,
                    val specialPlaces: List<Place>?
                    ):Serializable