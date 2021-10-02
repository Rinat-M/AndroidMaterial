package com.rino.nasaapp.datasources

import com.rino.nasaapp.remote.entities.ApodDTO
import java.util.*

interface DataSource {

    fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?>

    fun getEpicImageLink(date: Date): Result<String>

}