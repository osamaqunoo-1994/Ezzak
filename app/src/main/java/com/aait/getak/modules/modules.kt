package com.aait.getak.modules

import com.aait.getak.network.repository.countries_repo.LocalRepo
import com.aait.getak.network.repository.countries_repo.RepoCountriesImp
import com.aait.getak.ui.view_model.RegisterViewModel
import com.aait.getak.network.repository.countries_repo.RemoteRepo
import com.aait.getak.network.repository.other_repos.RemoteRepos
import com.aait.getak.utils.GlobalPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
  viewModel { RegisterViewModel(get(named("both")),get(named("repo")),androidContext()) }
    single(named("both")) {
        RepoCountriesImp(
            get(named("local")),
            get(named("remote"))
        )
    }
    single(named("local")) { LocalRepo() }
    single(named("remote")) { RemoteRepo() }
    single(named("repo")) { RemoteRepos(GlobalPreferences(androidContext())) }
    single { RemoteRepos(GlobalPreferences(androidContext())) }

}
