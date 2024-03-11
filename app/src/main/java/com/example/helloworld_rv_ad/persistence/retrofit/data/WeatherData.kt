package com.example.helloworld_rv_ad.persistence.retrofit.data

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("message") val message: String? = null,
    @SerializedName("cod") val cod: String? = null,
    @SerializedName("count") val count: Int? = null,
    @SerializedName("list") val weatherList: List<WeatherList>? = null
)
