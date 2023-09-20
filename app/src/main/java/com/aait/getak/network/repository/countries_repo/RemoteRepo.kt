package com.aait.getak.network.repository.countries_repo

import android.annotation.SuppressLint
import com.aait.getak.models.countries_model.Country
import com.aait.getak.network.remote_db.RetroWeb
import com.aait.getak.utils.Util
import io.reactivex.Observable

class RemoteRepo : RepoCountries {



    @SuppressLint("CheckResult")
    override fun getCountries(): Observable<List<Country>> {

        val map = RetroWeb.serviceApi.getCountries(Util.language).map {
            val countries = it.data!!.countries
            countries
        }
        return map
    }

    override fun saveCountries(countries: List<Country>) {

    }


}

