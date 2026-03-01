package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.utils.SessionManager
import kotlinx.coroutines.launch

class MyGoalsActivity : AppCompatActivity() {
    private lateinit var adapter: GoalAdapter
    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_goals)

        db = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)

        val rvGoals = findViewById<RecyclerView>(R.id.rvGoals)
        val tvEmptyState = findViewById<TextView>(R.id.tvEmptyState)
        val btnNewGoal = findViewById<Button>(R.id.btnNewGoal)

        rvGoals.layoutManager = LinearLayoutManager(this)
        adapter = GoalAdapter { goal ->
            val intent = Intent(this, GoalDetailActivity::class.java)
            intent.putExtra("GOAL_ID", goal.id)
            startActivity(intent)
        }
        rvGoals.adapter = adapter

        btnNewGoal.setOnClickListener {
            startActivity(Intent(this, CreateGoalActivity::class.java))
        }

        loadGoals()
    }

    override fun onResume() {
        super.onResume()
        loadGoals()
    }

    private fun loadGoals() {
        lifecycleScope.launch {
            val goals = db.goalDao().getActiveGoals(sessionManager.getUserId())
            if (goals.isEmpty()) {
                findViewById<TextView>(R.id.tvEmptyState).visibility = View.VISIBLE
                findViewById<RecyclerView>(R.id.rvGoals).visibility = View.GONE
            } else {
                findViewById<TextView>(R.id.tvEmptyState).visibility = View.GONE
                findViewById<RecyclerView>(R.id.rvGoals).visibility = View.VISIBLE
                adapter.setGoals(goals)
            }
        }
    }
}