package com.rino.nasaapp.remote

import com.rino.nasaapp.BuildConfig
import com.rino.nasaapp.remote.entities.ApodDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    @GET("planetary/apod")
    fun getAstronomyPictureOfTheDay(
        @Query("date") date: String = "",
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<ApodDTO>

}