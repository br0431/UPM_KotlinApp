package com.example.helloworld_rv_ad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class OpenStreetMapsActivity : AppCompatActivity() {
    private val TAG = "openSreetMapTag"
    private lateinit var map: MapView
    private lateinit var startPoint:GeoPoint
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


}