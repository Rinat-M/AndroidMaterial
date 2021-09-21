package com.rino.nasaapp.repositories

import com.rino.nasaapp.datasources.DataSource
import com.rino.nasaapp.remote.entities.ApodDTO

class NasaRepositoryImpl(
    private val dataSource: DataSource
) : NasaRepository {

    override fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?> =
        dataSource.getAstronomyPictureOfTheDay(date)

}