package com.rino.nasaapp.remote.entities

import com.google.gson.annotations.SerializedName

data class ApodDTO(
    val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    @SerializedName("hdurl")
    val hdUrl: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("service_version")
    val serviceVersion: String? = null,
    val copyright: String? = null
)