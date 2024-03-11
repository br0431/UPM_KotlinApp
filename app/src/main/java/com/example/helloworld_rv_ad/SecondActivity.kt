package com.example.helloworld_rv_ad

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.helloworld_rv_ad.room.AppDatabase
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"
    private var latestLocation: Location? = null
    private lateinit var database: AppDatabase
    private lateinit var adapter: CoordinatesAdapter
    val listView: ListView = findViewById(R.id.lvCoordinates)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.d(TAG, "onCreate: The activity is being created.");
        val buttonNext: Button = findViewById(R.id.SecondToThirdButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        val buttonPrevious: Button = findViewById(R.id.SecondToMainButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val headerView = layoutInflater.inflate(R.layout.listview_header, listView, false)
        listView.addHeaderView(headerView, null, false)
        // Create adapter of coordiantes. See class below
        adapter = CoordinatesAdapter(this, mutableListOf())
        listView.adapter = adapter
        database =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "coordinates").build()

        // ButtomNavigationMenu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_map -> {
                    if (latestLocation != null) {
                        val intent = Intent(this, OpenStreetMapsActivity::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable("location", latestLocation)
                        intent.putExtra("locationBundle", bundle)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "Location not set yet.")
                    }
                    true
                }

                R.id.navigation_list -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // Reutiliza el adaptador si ya está inicializado, en lugar de crear uno nuevo
        if (!::adapter.isInitialized) {
            adapter = CoordinatesAdapter(this, mutableListOf())
            listView.adapter = adapter
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val itemCount = database.locationDao().getCount()
            Log.d(TAG, "Number of items in database $itemCount.")
            loadCoordinatesFromDatabase(adapter)
        }
    }


    private class CoordinatesAdapter(
        context: Context,
        private val coordinatesList: MutableList<List<String>>
    ) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item, coordinatesList) {
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item, parent, false)
            val timestampTextView: TextView = view.findViewById(R.id.tvTimestamp)
            val latitudeTextView: TextView = view.findViewById(R.id.tvLatitude)
            val longitudeTextView: TextView = view.findViewById(R.id.tvLongitude)
            try {
                val item = coordinatesList[position]
                timestampTextView.text = formatTimestamp(item[0].toLong())
                latitudeTextView.text = formatCoordinate(item[1].toDouble())
                longitudeTextView.text = formatCoordinate(item[2].toDouble())
                // move to next activity
                view.setOnClickListener {
                    val intent = Intent(context, ThirdActivity::class.java).apply {
                        putExtra("timestamp", item[0].toLong())
                        putExtra("latitude", item[1].toDouble())
                        putExtra("longitude", item[2].toDouble())
                    }
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CoordinatesAdapter", "getView: Exception parsing coordinates.")
            }
            return view
        }

        private fun formatTimestamp(timestamp: Long): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            return formatter.format(Date(timestamp))
        }

        private fun formatCoordinate(value: Double): String {
            return String.format("%.6f", value)
        }

        fun updateData(newData: List<List<String>>) {
            this.coordinatesList.clear()
            this.coordinatesList.addAll(newData)
            notifyDataSetChanged()
        }
    }

    private fun loadCoordinatesFromDatabase(adapter: CoordinatesAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val coordinatesList = database.locationDao().getAllLocations()
                val formattedList = coordinatesList.map {
                    listOf(
                        it.timestamp.toString(),
                        it.latitude.toString(),
                        it.longitude.toString()
                    )
                }

                withContext(Dispatchers.Main) {
                    adapter.updateData(formattedList)
                }

                Log.d(
                    "CoordinatesAdapter",
                    "Number of items in database " + database.locationDao().getCount() + "."
                )
            } catch (e: Exception) {
                Log.e("Error", "Error loading coordinates from database", e)
            }
        }
    }


    fun readFileContents(): List<List<String>> {
        val fileName = "gps_coordinates.csv"
        return try {
            openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.map { it.split(";").map(String::trim) }.toList()
            }
        } catch (e: IOException) {
            listOf(listOf("Error reading file: ${e.message}"))
        }
    }
}

