package com.aait.getak.models.balance_model


import com.aait.getak.models.countries_model.Country

data class Data(
    var balance: String?,
    var use_balance_first: String?,
    var countries: List<Country>,
    var currentCountry: String?
)