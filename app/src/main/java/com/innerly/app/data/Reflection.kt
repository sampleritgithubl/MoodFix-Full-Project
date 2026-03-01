package com.innerly.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflections")
data class Reflection(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Long,
    val text: String,
    val mood: String,
    val date: Long = System.currentTimeMillis()
)