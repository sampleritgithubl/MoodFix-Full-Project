package com.innerly.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.innerly.app.R
import com.innerly.app.data.Reflection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReflectionAdapter : RecyclerView.Adapter<ReflectionAdapter.ReflectionViewHolder>() {

    private var reflections: List<Reflection> = emptyList()

    fun setReflections(reflections: List<Reflection>) {
        this.reflections = reflections
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReflectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reflection, parent, false)
        return ReflectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReflectionViewHolder, position: Int) {
        holder.bind(reflections[position])
    }

    override fun getItemCount(): Int = reflections.size

    class ReflectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvReflectionDate)
        private val tvMood: TextView = itemView.findViewById(R.id.tvReflectionMood)
        private val tvPreview: TextView = itemView.findViewById(R.id.tvReflectionPreview)

        fun bind(reflection: Reflection) {
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            tvDate.text = sdf.format(Date(reflection.date))
            tvMood.text = reflection.mood
            tvPreview.text = reflection.text
        }
    }
}