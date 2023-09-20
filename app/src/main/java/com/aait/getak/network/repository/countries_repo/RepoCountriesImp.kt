package com.aait.getak.network.repository.countries_repo

import com.aait.getak.models.countries_model.Country
import io.reactivex.Observable

class RepoCountriesImp(val localRepo: LocalRepo, val remoteRepo: RemoteRepo) :
    RepoCountries {
    override fun getCountries(): Observable<List<Country>> {

        return Observable.concat(localRepo.getCountries(),remoteRepo.getCountries())
            .doOnNext {
                    saveCountries(it)
            }
            .onErrorResumeNext(
                Observable.empty()
            )
    }

    override fun saveCountries(countries: List<Country>) {
        localRepo.saveCountries(countries)
    }

}
