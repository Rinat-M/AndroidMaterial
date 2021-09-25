package com.rino.nasaapp.repositories

import com.rino.nasaapp.remote.entities.ApodDTO

interface NasaRepository {

    fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?>

}