package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.utils.SessionManager
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val btnWriteReflection = findViewById<Button>(R.id.btnWriteReflection)
        val btnMyGoals = findViewById<Button>(R.id.btnMyGoals)
        val btnPastReflections = findViewById<Button>(R.id.btnPastReflections)

        lifecycleScope.launch {
            val user = db.userDao().getUserById(userId)
            if (user != null) {
                tvUserName.text = getString(R.string.hi_user, user.name)
            }
        }

        btnMyGoals.setOnClickListener {
            startActivity(Intent(this, MyGoalsActivity::class.java))
        }

        btnWriteReflection.setOnClickListener {
            startActivity(Intent(this, MyGoalsActivity::class.java))
        }
        
        btnPastReflections.setOnClickListener {
             startActivity(Intent(this, ArchivedGoalsActivity::class.java))
        }
    }
}