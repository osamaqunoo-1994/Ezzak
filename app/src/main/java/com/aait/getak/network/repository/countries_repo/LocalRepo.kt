package com.aait.getak.network.repository.countries_repo

import android.annotation.SuppressLint
import com.aait.getak.app.AppController.Companion.App
import com.aait.getak.models.countries_model.Country
import com.aait.getak.network.local_db.AppDB
import io.reactivex.Observable

class LocalRepo : RepoCountries {
    @SuppressLint("CheckResult")
    override fun getCountries(): Observable<List<Country>> {
      return  Observable.fromCallable {
            AppDB.getInstance(App).appDao().getCountries()
        }
    }

    override fun saveCountries(countries: List<Country>) {
        AppDB.getInstance(App).appDao().addCountries(countries)
    }

}
