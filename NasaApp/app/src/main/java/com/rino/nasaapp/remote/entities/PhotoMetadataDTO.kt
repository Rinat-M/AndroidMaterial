package com.rino.nasaapp.remote.entities

import com.google.gson.annotations.SerializedName

data class PhotoMetadataDTO(
    val id: Int?,
    val sol: Int?,
    @SerializedName("img_src")
    val imgSrc: String,
    @SerializedName("earth_date")
    val earthDate: String
)