package com.example.helloworld_rv_ad.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): UserDao
}
