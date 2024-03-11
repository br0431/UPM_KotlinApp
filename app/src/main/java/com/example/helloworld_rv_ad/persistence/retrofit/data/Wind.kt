package com.example.helloworld_rv_ad.persistence.retrofit.data

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double? = null,
    @SerializedName("deg") val deg: Double? = null
)
