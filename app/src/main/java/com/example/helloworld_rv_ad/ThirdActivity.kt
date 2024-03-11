package com.example.helloworld_rv_ad

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.helloworld_rv_ad.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThirdActivity : AppCompatActivity() {
    lateinit var database: AppDatabase
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val buttonNext: Button = findViewById(R.id.ThirdToMainButton)
        val secondButton: Button = findViewById(R.id.ThirdToSecondActivity)
        val timestamp = intent.getLongExtra("timestamp", 0)
        val latitude = intent.getDoubleExtra("latitude", 40.475172)
        val longitude = intent.getDoubleExtra("longitude", -3.461757)

        buttonNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        secondButton.setOnClickListener {
            val intent2 = Intent(this, SecondActivity::class.java)
            startActivity(intent2)
        }
        val userIdentifier = getUserIdentifier()
        if (userIdentifier == null) {
            Toast.makeText(this, "User ID not set set. Request will not work", Toast.LENGTH_LONG).show()
        }
        Log.d(TAG, "Latitude: $latitude, Longitude: $longitude, Timestamp: $timestamp")

        val buttonPrevious: Button = findViewById(R.id.ThirdToSecondActivity)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        val deleteButton: Button = findViewById(R.id.DeleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { dialog, which ->
                    database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "coordinates").build()
                    lifecycleScope.launch(Dispatchers.IO) {
                        Log.d(TAG, "Number of items in database before delete "+database.locationDao().getCount()+".");
                        database.locationDao().deleteLocationByTimestamp(timestamp)
                        Log.d(TAG, "Number of items in database after delete "+database.locationDao().getCount()+".");
                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }


    }
    private fun getUserIdentifier(): String? {
        val sharedPreferences = this.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userIdentifier", null)
    }

}