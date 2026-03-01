package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.utils.SessionManager
import kotlinx.coroutines.launch

class ArchivedGoalsActivity : AppCompatActivity() {
    private lateinit var adapter: GoalAdapter
    private lateinit var db: AppDatabase
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archived_goals)

        db = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)

        val rvArchived = findViewById<RecyclerView>(R.id.rvArchivedGoals)
        
        rvArchived.layoutManager = LinearLayoutManager(this)
        adapter = GoalAdapter { goal ->
            val intent = Intent(this, GoalDetailActivity::class.java)
            intent.putExtra("GOAL_ID", goal.id)
            startActivity(intent)
        }
        rvArchived.adapter = adapter

        loadArchivedGoals()
    }

    private fun loadArchivedGoals() {
        lifecycleScope.launch {
            val goals = db.goalDao().getArchivedGoals(sessionManager.getUserId())
            if (goals.isEmpty()) {
                findViewById<TextView>(R.id.tvEmptyState).visibility = View.VISIBLE
                findViewById<RecyclerView>(R.id.rvArchivedGoals).visibility = View.GONE
            } else {
                findViewById<TextView>(R.id.tvEmptyState).visibility = View.GONE
                findViewById<RecyclerView>(R.id.rvArchivedGoals).visibility = View.VISIBLE
                adapter.setGoals(goals)
            }
        }
    }
}