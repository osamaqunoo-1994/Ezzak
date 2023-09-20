package com.aait.getak.models.countries_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(
    var currency: String?,
    @PrimaryKey
    var id: Int?,
    var iso: String?,
    var key: String?,
    var name: String?
)
