package com.example.helloworld_rv_ad.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertLocation(locationEntity: User)
    @Query("SELECT * FROM User")
    fun getAllLocations(): List<User>
    @Query("SELECT COUNT(*) FROM User")
    fun getCount(): Int
    @Query("DELETE FROM User WHERE timestamp = :timestamp")
    fun deleteLocationByTimestamp(timestamp: Long)
}