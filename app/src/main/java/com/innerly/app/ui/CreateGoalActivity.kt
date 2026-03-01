package com.innerly.app.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.data.Goal
import com.innerly.app.utils.SessionManager
import kotlinx.coroutines.launch

class CreateGoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)

        val etGoalTitle = findViewById<EditText>(R.id.etGoalTitle)
        val etGoalWhy = findViewById<EditText>(R.id.etGoalWhy)
        val rgMoods = findViewById<RadioGroup>(R.id.rgMoods)
        val btnSaveGoal = findViewById<Button>(R.id.btnSaveGoal)

        btnSaveGoal.setOnClickListener {
            val title = etGoalTitle.text.toString()
            val description = etGoalWhy.text.toString()
            val selectedMoodId = rgMoods.checkedRadioButtonId

            if (title.isEmpty() || description.isEmpty() || selectedMoodId == -1) {
                Toast.makeText(this, "Please fill all fields and select a mood", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mood = findViewById<RadioButton>(selectedMoodId).text.toString()

            lifecycleScope.launch {
                val goal = Goal(
                    userId = sessionManager.getUserId(),
                    title = title,
                    description = description,
                    currentMood = mood
                )
                db.goalDao().insert(goal)
                finish()
            }
        }
    }
}