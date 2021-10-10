package com.rino.nasaapp.di

import com.rino.nasaapp.datasources.DataSource
import com.rino.nasaapp.datasources.RemoteDataSourceImpl
import com.rino.nasaapp.datasources.TodoSource
import com.rino.nasaapp.datasources.TodoSourceImpl
import com.rino.nasaapp.providers.StringsProvider
import com.rino.nasaapp.repositories.NasaRepository
import com.rino.nasaapp.repositories.NasaRepositoryImpl
import com.rino.nasaapp.repositories.TodoRepository
import com.rino.nasaapp.repositories.TodoRepositoryImpl
import com.rino.nasaapp.ui.earth.EarthViewModel
import com.rino.nasaapp.ui.home.HomeViewModel
import com.rino.nasaapp.ui.main.MainViewModel
import com.rino.nasaapp.ui.mars.MarsViewModel
import com.rino.nasaapp.ui.settings.SettingsViewModel
import com.rino.nasaapp.ui.todo.TodoListViewModel
import com.rino.nasaapp.wrappers.ThemeSharedPreferencesWrapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Providers
    single { StringsProvider(get()) }

    // Data sources
    single<DataSource> { RemoteDataSourceImpl(get(), get()) }
    single<TodoSource> { TodoSourceImpl() }

    // Repositories
    single<NasaRepository> { NasaRepositoryImpl(get()) }
    single<TodoRepository> { TodoRepositoryImpl(get()) }

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
    viewModel { MarsViewModel(get()) }
    viewModel { TodoListViewModel(get()) }
}