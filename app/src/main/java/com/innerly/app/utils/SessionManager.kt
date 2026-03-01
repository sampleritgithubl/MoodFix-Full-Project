package com.innerly.app.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("innerly_prefs", Context.MODE_PRIVATE)

    fun loginUser(userId: Long) {
        prefs.edit().putLong("user_id", userId).apply()
    }

    fun logoutUser() {
        prefs.edit().remove("user_id").apply()
    }

    fun getUserId(): Long {
        return prefs.getLong("user_id", -1L)
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1L
    }
}