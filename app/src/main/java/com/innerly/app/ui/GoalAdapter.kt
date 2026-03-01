package com.innerly.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innerly.app.R
import com.innerly.app.data.Goal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GoalAdapter(private val onGoalClick: (Goal) -> Unit) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var goals: List<Goal> = emptyList()

    fun setGoals(newGoals: List<Goal>) {
        this.goals = newGoals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.bind(goal)
        holder.itemView.setOnClickListener { onGoalClick(goal) }
    }

    override fun getItemCount(): Int = goals.size

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvGoalTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvGoalDescription)
        private val tvMood: TextView = itemView.findViewById(R.id.tvMoodLabel)
        private val tvDate: TextView = itemView.findViewById(R.id.tvUpdatedDate)

        fun bind(goal: Goal) {
            tvTitle.text = goal.title
            tvDescription.text = goal.description
            tvMood.text = goal.currentMood
            
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val formattedDate = sdf.format(Date(goal.createdDate))
            tvDate.text = itemView.context.getString(R.string.goal_created_date, formattedDate)
        }
    }
}