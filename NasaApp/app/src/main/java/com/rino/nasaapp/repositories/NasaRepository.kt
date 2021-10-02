package com.rino.nasaapp.repositories

import com.rino.nasaapp.remote.entities.ApodDTO
import java.util.*

interface NasaRepository {

    fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?>

    fun getEpicImageLink(date: Date): Result<String>

}