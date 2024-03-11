package com.example.helloworld_rv_ad.persistence.retrofit.data


import com.google.gson.annotations.SerializedName

data class WeatherItem(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("main") val main: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("icon") val icon: String? = null
)
