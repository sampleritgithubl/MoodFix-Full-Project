package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.innerly.app.R
import com.innerly.app.utils.SessionManager

class MoodCheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_check)

        val sessionManager = SessionManager(this)

        findViewById<Button>(R.id.btnStartReflecting).setOnClickListener {
            if (sessionManager.isLoggedIn()) {
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }
    }
}