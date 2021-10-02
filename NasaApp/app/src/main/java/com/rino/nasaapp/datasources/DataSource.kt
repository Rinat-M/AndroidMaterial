package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.remote.entities.PhotosDTO
import java.util.*

interface DataSource {

    fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?>

    fun getEpicImageLink(date: Date): Result<String>

    fun getMarsRoverPhotos(date: String, camera: RoverCamera): Result<PhotosDTO>

}