package com.rino.nasaapp.datasources

import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.providers.StringsProvider
import com.rino.nasaapp.remote.NasaService
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.remote.entities.PhotosDTO
import java.text.SimpleDateFormat
import java.util.*

class RemoteDataSourceImpl(
    private val nasaService: NasaService,
    private val stringsProvider: StringsProvider
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

    override fun getEpicImageLink(date: Date): Result<String> {
        val calendar = Calendar.getInstance()
        calendar.time = date

        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = formatter.format(date)

        return try {
            val response = nasaService.getEpicMetadataByDate(dateString).execute()

            if (!response.isSuccessful) {
                val msg =
                    "Response code: ${response.code()}. Response message: ${response.errorBody()}"
                return Result.failure(Exception(msg))
            }

            val epicsMetadata = response.body()

            val imageLink = if (epicsMetadata.isNullOrEmpty()) {
                ""
            } else {
                val firstEpic = epicsMetadata.firstOrNull()
                val filename = firstEpic?.image

                if (filename.isNullOrEmpty()) {
                    ""
                } else {
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH) + 1
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val epicNaturalImageRequest = nasaService.getEpicNaturalImage(
                        year.toString(),
                        String.format("%02d", month),
                        String.format("%02d", day),
                        filename
                    ).request()

                    epicNaturalImageRequest.url.toString()
                }
            }

            if (imageLink.isEmpty()) {
                Result.failure(Exception(stringsProvider.noImageForSelectedDayMsg))
            } else {
                Result.success(imageLink)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override fun getMarsRoverPhotos(date: String, camera: RoverCamera): Result<PhotosDTO> {
        return try {
            val response = nasaService.getMarsRoverPhotos(date, camera).execute()

            if (!response.isSuccessful) {
                val msg =
                    "Response code: ${response.code()}. Response message: ${response.errorBody()}"
                return Result.failure(Exception(msg))
            }

            val photosDTO = response.body() ?: PhotosDTO(emptyList())

            if (photosDTO.photos.isEmpty()) {
                Result.failure(Exception(stringsProvider.noPhotosFromCurrentCameraMsg))
            } else {
                Result.success(photosDTO)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}