package com.rino.nasaapp.remote

import com.rino.nasaapp.BuildConfig
import com.rino.nasaapp.entities.RoverCamera
import com.rino.nasaapp.remote.entities.ApodDTO
import com.rino.nasaapp.remote.entities.EpicMetadataDTO
import com.rino.nasaapp.remote.entities.PhotosDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaService {

    /**
     * One of the most popular websites at NASA is the Astronomy Picture of the Day. In fact, this website is one of the most popular websites across all federal agencies.
     * This endpoint structures the APOD imagery and associated metadata so that it can be repurposed for other applications.
     * @param date the date of the APOD image to retrieve.
     * @param apiKey api.nasa.gov key for expanded usage.
     * @return the APOD imagery and associated metadata.
     */
    @GET("planetary/apod")
    fun getAstronomyPictureOfTheDay(
        @Query("date") date: String = "",
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<ApodDTO>

    /**
     * The EPIC API provides information on the daily imagery collected by DSCOVR's Earth Polychromatic Imaging Camera (EPIC) instrument.
     * Uniquely positioned at the Earth-Sun Lagrange point, EPIC provides full disc imagery of the Earth and captures unique perspectives
     * of certain astronomical events such as lunar transits using a 2048x2048 pixel CCD (Charge Coupled Device) detector coupled
     * to a 30-cm aperture Cassegrain telescope.
     * @param date the date in format 'yyyy-MM-dd'.
     * @param apiKey api.nasa.gov key for expanded usage.
     * @return the list of metadata for natural color imagery available for a given date.
     */
    @GET("EPIC/api/natural/date/{date}")
    fun getEpicMetadataByDate(
        @Path("date") date: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<List<EpicMetadataDTO>>

    /**
     * The EPIC API provides information on the daily imagery collected by DSCOVR's Earth Polychromatic Imaging Camera (EPIC) instrument.
     * Uniquely positioned at the Earth-Sun Lagrange point, EPIC provides full disc imagery of the Earth and captures unique perspectives
     * of certain astronomical events such as lunar transits using a 2048x2048 pixel CCD (Charge Coupled Device) detector coupled
     * to a 30-cm aperture Cassegrain telescope.
     * @param year the year.
     * @param month the month.
     * @param day the day.
     * @param filename the image filename that given with getEpicMetadataByDate query.
     * @param apiKey api.nasa.gov key for expanded usage.
     * @return the image available for a given date.
     */
    @GET("EPIC/archive/natural/{year}/{month}/{day}/png/{filename}.png")
    fun getEpicNaturalImage(
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("filename") filename: String,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<ResponseBody>


    /**
     * Each rover has its own set of photos stored in the database, which can be queried separately.
     * There are several possible queries that can be made against the API. Photos are organized by the sol (Martian rotation or day)
     * on which they were taken, counting up from the rover's landing date. A photo taken on Curiosity's 1000th Martian sol
     * exploring Mars, for example, will have a sol attribute of 1000. If instead you prefer to search by the Earth date on
     * which a photo was taken, you can do that too.
     * @param earthDate the corresponding date on earth for the given sol in format 'yyyy-MM-dd'.
     * @param camera each camera has a unique function and perspective, and they are named as follows: FHAZ, RHAZ, MAST, CHEMCAM and etc.
     */
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsRoverPhotos(
        @Query("earth_date") earthDate: String,
        @Query("camera") camera: RoverCamera,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): Call<PhotosDTO>
}