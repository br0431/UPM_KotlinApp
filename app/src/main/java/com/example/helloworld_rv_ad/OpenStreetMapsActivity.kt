package com.example.helloworld_rv_ad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
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
    val gymkhanaCoords = listOf(
        GeoPoint(40.38779608214728, -3.627687914352839), // Tennis
        GeoPoint(40.38788595319803, -3.627048250272035), // Futsal outdoors
        GeoPoint(40.38847548693242, -3.626631851734613), // Polideportivo
        GeoPoint(40.388422047110225, -3.6270194804957954), // Club lucha
        GeoPoint(40.38255074195828, -3.6336136612427956) // Polideportivo Palomeras

    )
    val gymkhanaNames = listOf(
        "Tennis",
        "Futsal outdoors",
        "Sports Center UPM",
        "Fight club",
        "Sports Center Palomeras"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_street_maps)
        Log.d(TAG, "onCreate: The activity is being created.")
        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")
        if (location != null) {
            Log.i(TAG, "onCreate: Location["+location.altitude+"]["+location.latitude+"]["+location.longitude+"][")

            Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
            map = findViewById(R.id.mapView)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(15.0)
            startPoint = GeoPoint(40.3893, -3.6298)
            addMarker(startPoint, "My current location")
            addMarkersAndRoute(map,gymkhanaCoords,gymkhanaNames)
            //val startPoint = GeoPoint(40.416775, -3.703790) in case you want to test it mannualy
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
    private fun addMarker(point: GeoPoint, title: String) {
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        map.overlays.add(marker)
        map.invalidate() // Reload map
    }
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




}