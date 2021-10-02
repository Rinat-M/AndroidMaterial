package com.rino.nasaapp.di

import com.rino.nasaapp.datasources.DataSource
import com.rino.nasaapp.datasources.RemoteDataSourceImpl
import com.rino.nasaapp.providers.StringProvider
import com.rino.nasaapp.repositories.NasaRepository
import com.rino.nasaapp.repositories.NasaRepositoryImpl
import com.rino.nasaapp.ui.earth.EarthViewModel
import com.rino.nasaapp.ui.home.HomeViewModel
import com.rino.nasaapp.ui.main.MainViewModel
import com.rino.nasaapp.ui.settings.SettingsViewModel
import com.rino.nasaapp.wrappers.ThemeSharedPreferencesWrapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Providers
    single { StringProvider(get()) }

    single<DataSource> { RemoteDataSourceImpl(get(), get()) }
    single<NasaRepository> { NasaRepositoryImpl(get()) }

    // Network
    single { NetworkModule.getOkHttpClient() }
    single { NetworkModule.getRetrofit(get()) }
    single { NetworkModule.getNasaService(get()) }

    // Wrappers
    single { ThemeSharedPreferencesWrapper(get()) }

    // View models
    viewModel { HomeViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { EarthViewModel(get()) }
}