package com.aait.getak.network.repository.countries_repo

import com.aait.getak.models.countries_model.Country
import io.reactivex.Observable

interface RepoCountries {
    fun getCountries(): Observable<List<Country>>
    fun saveCountries(countries:List<Country>)

}
