package com.example.helloworld_rv_ad.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
