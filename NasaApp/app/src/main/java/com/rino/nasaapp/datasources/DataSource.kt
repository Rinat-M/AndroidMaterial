package com.rino.nasaapp.datasources

import com.rino.nasaapp.remote.entities.ApodDTO

interface DataSource {

    fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?>

}