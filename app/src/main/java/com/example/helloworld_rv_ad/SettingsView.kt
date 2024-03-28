package com.example.helloworld_rv_ad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view)

        // ButtomNavigationMenu
        val navView: BottomNavigationView = findViewById(R.id.nav_viewer)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_map -> {
                    val intent = Intent(this, OpenStreetMapsActivity::class.java)
                    startActivity(intent)
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



        val listView: ListView = findViewById(R.id.lvPreferences)
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        val listItems = ArrayList<String>()
        for ((key, value) in allEntries) {
            listItems.add("$key: $value")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
    }
}
