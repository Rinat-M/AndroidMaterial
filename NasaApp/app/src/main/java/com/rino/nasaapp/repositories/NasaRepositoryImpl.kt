package com.rino.nasaapp.repositories

import com.rino.nasaapp.datasources.DataSource
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.remote.entities.PhotosDTO
import java.util.*

class NasaRepositoryImpl(
    private val dataSource: DataSource
) : NasaRepository {

    override fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?> =
        dataSource.getAstronomyPictureOfTheDay(date)

    override fun getEpicImageLink(date: Date): Result<String> =
        dataSource.getEpicImageLink(date)

    override fun getMarsRoverPhotos(date: String, camera: RoverCamera): Result<PhotosDTO> =
        dataSource.getMarsRoverPhotos(date, camera)

}