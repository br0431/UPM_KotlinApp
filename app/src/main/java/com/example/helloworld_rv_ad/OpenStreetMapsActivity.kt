package com.example.helloworld_rv_ad

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
class OpenStreetMapsActivity : AppCompatActivity() {
    private val TAG = "openSreetMapTag"
    private lateinit var map: MapView
    private lateinit var startPoint:GeoPoint
    private lateinit var endPoint: GeoPoint
    private val markers = mutableListOf<Marker>() // Lista para almacenar los marcadores
    private var latestLocation: Location? = null

    // Claves para guardar y restaurar el estado

    val gymkhanaCoords = mutableListOf(
        GeoPoint(40.38779608214728, -3.627687914352839), // Tennis
        GeoPoint(40.38788595319803, -3.627048250272035), // Futsal outdoors
        GeoPoint(40.38847548693242, -3.626631851734613), // Polideportivo
        GeoPoint(40.388422047110225, -3.6270194804957954), // Club lucha
        GeoPoint(40.38255074195828, -3.6336136612427956), // Polideportivo Palomeras
        GeoPoint(40.390145278568575, -3.6331844756708445), // campo de futbol
        GeoPoint(40.38927908176764, -3.6399114559463994),// gimnasio olympo
        GeoPoint(40.38371450404913, -3.644601269534232),// rocodromo
        GeoPoint(40.381160768449526, -3.623926618537384) //detroit boxing

    )
    val gymkhanaNames = mutableListOf(
        "Tennis",
        "Futsal outdoors",
        "Sports Center UPM",
        "Fight club",
        "Sports Center Palomeras",
        "Football Pitch",
        "Olympo GYM",
        "Climbing place",
        "Detroit boxing club"
    )
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_street_maps)
        val imageButton = findViewById<ImageButton>(R.id.imageButton2) // Inicializar el botón de imagen
        imageButton.setOnClickListener { addMarker() } // Agregar un OnClickListener al botón de imagen
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            val currentActivity = this::class.java.simpleName
            when (item.itemId) {
                R.id.navigation_home -> if (currentActivity != MainActivity::class.java.simpleName) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                R.id.navigation_map -> if (currentActivity != OpenStreetMapsActivity::class.java.simpleName) {
                    if (latestLocation != null) {
                        val intent = Intent(this, OpenStreetMapsActivity::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable("location", latestLocation)
                        intent.putExtra("locationBundle", bundle)
                        startActivity(intent)
                    }else{
                        Log.e(TAG, "Location not set yet.")
                        startActivity(Intent(this, OpenStreetMapsActivity::class.java))
                    }
                    true
                }
                R.id.navigation_list -> if (currentActivity != SecondActivity::class.java.simpleName) {
                    startActivity(Intent(this, SecondActivity::class.java))
                }
            }
            true
        }
        Log.d(TAG, "onCreate: The activity is being created.")
        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")
        if (location != null) {
            Log.i(
                TAG,
                "onCreate: Location[" + location.altitude + "][" + location.latitude + "][" + location.longitude + "]["
            )

            Configuration.getInstance()
                .load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
            map = findViewById(R.id.mapView)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(15.0)
            startPoint = GeoPoint(40.3893, -3.6298)
            endPoint = GeoPoint(40.381968652661556, -3.6247002562272868)
            addMarkersAndRoute(map, gymkhanaCoords, gymkhanaNames)
            //val startPoint = GeoPoint(40.416775, -3.703790) in case you want to test it mannualy
            map.controller.setCenter(startPoint)
            addMarkersAndRoute(map, gymkhanaCoords, gymkhanaNames)
            map.controller.setCenter(startPoint)
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


    @SuppressLint("ClickableViewAccessibility")

    fun addMarkersAndRoute(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {
        if (locationsCoords.size != locationsNames.size) {
            Log.e("addMarkersAndRoute", "Locations and names lists must have the same number of items.")
            return
        }
        val route = Polyline()
        route.setPoints(locationsCoords)
        route.color = ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_error)
        mapView.overlays.add(route)
        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            val locationIndex = locationsCoords.indexOf(location)
            marker.title = "Marker at ${locationsNames[locationIndex]} ${location.latitude}, ${location.longitude}"
            marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.ic_menu_mylocation)
            mapView.overlays.add(marker)
        }
        mapView.invalidate()

    }
    // Método para agregar un nuevo marcador
    private fun addMarker() {
        val mapCenter = map.mapCenter
        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this)
        dialogBuilder.setMessage("Enter Marker Title:")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                val title = input.text.toString()
                val marker = Marker(map)
                marker.position = GeoPoint(mapCenter.latitude, mapCenter.longitude)
                marker.title = "Marker at " + title + " " + marker.position.latitude + " " + marker.position.longitude
                marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.ic_menu_mylocation)
                map.overlays.add(marker)
                map.invalidate()
                // Guardar el nuevo marcador en las listas
                markers.add(marker)

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.setView(input)
        alertDialog.show()
    }

    private fun addMarkersFromLists() {
        for (i in gymkhanaCoords.indices) {
            val marker = Marker(map)
            marker.position = gymkhanaCoords[i]
            marker.title = gymkhanaNames[i]
            map.overlays.add(marker)
        }
        map.invalidate()
    }


}










