package com.aait.getak.models.years_model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("current_month")
    var currentMonth: String?,
    @SerializedName("current_year")
    var currentYear: String?,
    var years: List<Int?>?
)