package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.innerly.app.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<ImageButton>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, MoodCheckActivity::class.java))
            finish()
        }
    }
}