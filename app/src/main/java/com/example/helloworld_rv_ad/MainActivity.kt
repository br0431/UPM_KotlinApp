package com.example.helloworld_rv_ad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private val TAG = "btaMainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: The activity is being created.")
        val buttonNext: Button = findViewById(R.id.mainToSecondButton)
        val secondButton: Button = findViewById(R.id.mainToThirdButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        secondButton.setOnClickListener {
            val intent2 = Intent(this, ThirdActivity::class.java)
            startActivity(intent2)
        }
    }
}















