package com.example.helloworld_rv_ad.persistence.retrofit.data

import com.google.gson.annotations.SerializedName

data class WeatherList(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("coord") val coord: Coord? = null,
    @SerializedName("main") val main: Main? = null,
    @SerializedName("dt") val dt: Long? = null,
    @SerializedName("wind") val wind: Wind? = null,
    @SerializedName("sys") val sys: Sys? = null,
    @SerializedName("rain") val rain: Any? = null,  // You might need to adjust this depending on the actual data
    @SerializedName("snow") val snow: Any? = null,  // You might need to adjust this depending on the actual data
    @SerializedName("clouds") val clouds: Clouds? = null,
    @SerializedName("weather") val weather: List<WeatherItem>? = null
)
