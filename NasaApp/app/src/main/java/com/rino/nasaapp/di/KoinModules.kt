package com.rino.nasaapp.di

import com.rino.nasaapp.datasources.DataSource
import com.rino.nasaapp.datasources.RemoteDataSourceImpl
import com.rino.nasaapp.repositories.NasaRepository
import com.rino.nasaapp.repositories.NasaRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<DataSource> { RemoteDataSourceImpl(get()) }
    single<NasaRepository> { NasaRepositoryImpl(get()) }

    // Network
    single { NetworkModule.getOkHttpClient() }
    single { NetworkModule.getRetrofit(get()) }
    single { NetworkModule.getNasaService(get()) }
}