package com.innerly.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String,
    val currentMood: String,
    val isCompleted: Boolean = false,
    val createdDate: Long = System.currentTimeMillis()
)