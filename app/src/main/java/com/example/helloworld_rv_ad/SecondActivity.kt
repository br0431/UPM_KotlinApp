package com.example.helloworld_rv_ad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val buttonNext: Button = findViewById(R.id.SecondToMainButton)
        val secondButton: Button = findViewById(R.id.SecondToThirdButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        secondButton.setOnClickListener {
            val intent2 = Intent(this, ThirdActivity::class.java)
            startActivity(intent2)
        }
    }
}