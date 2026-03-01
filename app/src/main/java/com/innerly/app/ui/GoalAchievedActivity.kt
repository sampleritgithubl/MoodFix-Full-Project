package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.data.Reflection
import kotlinx.coroutines.launch

class GoalAchievedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_achieved)

        val goalId = intent.getLongExtra("GOAL_ID", -1)
        if (goalId == -1L) {
            finish()
            return
        }

        val db = AppDatabase.getDatabase(this)
        val etLessons = findViewById<EditText>(R.id.etLessonsLearned)
        val btnComplete = findViewById<Button>(R.id.btnCompleteGoal)

        btnComplete.setOnClickListener {
            val lessons = etLessons.text.toString()

            lifecycleScope.launch {
                val goal = db.goalDao().getGoalById(goalId)
                if (goal != null) {
                    // Mark as completed
                    db.goalDao().update(goal.copy(isCompleted = true))

                    // If they wrote lessons, save it as a final reflection
                    if (lessons.isNotEmpty()) {
                        db.reflectionDao().insert(
                            Reflection(
                                goalId = goalId,
                                text = getString(R.string.reflection_lessons_learned, lessons),
                                mood = "Achieved"
                            )
                        )
                    }
                }
                
                // Return to home or my goals
                val intent = Intent(this@GoalAchievedActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }
    }
}