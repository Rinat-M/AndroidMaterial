package com.rino.nasaapp.datasources

import com.rino.nasaapp.remote.NasaService
import com.rino.nasaapp.remote.entities.ApodDTO
import java.lang.Exception

class RemoteDataSourceImpl(
    private val nasaService: NasaService
) : DataSource {

    override fun getAstronomyPictureOfTheDay(date: String): Result<ApodDTO?> {
        return try {
            val response = nasaService.getAstronomyPictureOfTheDay(date).execute()

            if (!response.isSuccessful) {
                val msg =
                    "Response code: ${response.code()}. Response message: ${response.errorBody()}"
                return Result.failure(Exception(msg))
            }

            Result.success(response.body())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}