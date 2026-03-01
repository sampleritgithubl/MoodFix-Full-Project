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
import com.innerly.app.data.Reflection
import kotlinx.coroutines.launch

class WriteReflectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_reflection)

        val goalId = intent.getLongExtra("GOAL_ID", -1)
        if (goalId == -1L) {
            finish()
            return
        }

        val db = AppDatabase.getDatabase(this)

        val etReflection = findViewById<EditText>(R.id.etReflectionText)
        val rgMoods = findViewById<RadioGroup>(R.id.rgMoods)
        val btnSave = findViewById<Button>(R.id.btnSaveReflection)

        btnSave.setOnClickListener {
            val text = etReflection.text.toString()
            val selectedMoodId = rgMoods.checkedRadioButtonId

            if (text.isEmpty() || selectedMoodId == -1) {
                Toast.makeText(this, R.string.error_write_something_mood, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mood = findViewById<RadioButton>(selectedMoodId).text.toString()

            lifecycleScope.launch {
                val reflection = Reflection(
                    goalId = goalId,
                    text = text,
                    mood = mood
                )
                db.reflectionDao().insert(reflection)
                
                // Also update the goal's current mood to the latest reflection mood
                val goal = db.goalDao().getGoalById(goalId)
                if (goal != null) {
                    db.goalDao().update(goal.copy(currentMood = mood))
                }
                
                finish()
            }
        }
    }
}