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
import kotlinx.coroutines.launch

class GoalDetailActivity : AppCompatActivity() {
    private var goalId: Long = -1
    private lateinit var db: AppDatabase
    private lateinit var adapter: ReflectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_detail)

        goalId = intent.getLongExtra("GOAL_ID", -1)
        if (goalId == -1L) {
            finish()
            return
        }

        db = AppDatabase.getDatabase(this)
        
        val tvTitle = findViewById<TextView>(R.id.tvGoalTitle)
        val tvDescription = findViewById<TextView>(R.id.tvGoalDescription)
        val rvReflections = findViewById<RecyclerView>(R.id.rvReflections)
        val btnWriteNew = findViewById<Button>(R.id.btnWriteNewReflection)
        val btnMarkAchieved = findViewById<Button>(R.id.btnMarkAchieved)

        rvReflections.layoutManager = LinearLayoutManager(this)
        adapter = ReflectionAdapter()
        rvReflections.adapter = adapter

        lifecycleScope.launch {
            val goal = db.goalDao().getGoalById(goalId)
            if (goal != null) {
                tvTitle.text = goal.title
                tvDescription.text = goal.description
            }
        }

        btnWriteNew.setOnClickListener {
            val intent = Intent(this, WriteReflectionActivity::class.java)
            intent.putExtra("GOAL_ID", goalId)
            startActivity(intent)
        }

        btnMarkAchieved.setOnClickListener {
            val intent = Intent(this, GoalAchievedActivity::class.java)
            intent.putExtra("GOAL_ID", goalId)
            startActivity(intent)
        }

        loadReflections()
    }

    override fun onResume() {
        super.onResume()
        loadReflections()
    }

    private fun loadReflections() {
        lifecycleScope.launch {
            val reflections = db.reflectionDao().getReflectionsForGoal(goalId)
            val tvNoReflections = findViewById<TextView>(R.id.tvNoReflections)
            val rvReflections = findViewById<RecyclerView>(R.id.rvReflections)
            
            if (reflections.isEmpty()) {
                tvNoReflections.visibility = View.VISIBLE
                rvReflections.visibility = View.GONE
            } else {
                tvNoReflections.visibility = View.GONE
                rvReflections.visibility = View.VISIBLE
                adapter.setReflections(reflections)
            }
        }
    }
}