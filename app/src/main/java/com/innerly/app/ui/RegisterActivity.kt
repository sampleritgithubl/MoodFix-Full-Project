package com.innerly.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.innerly.app.R
import com.innerly.app.data.AppDatabase
import com.innerly.app.data.User
import com.innerly.app.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = SessionManager(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val existingUser = db.userDao().getUserByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, R.string.error_email_exists, Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(name = name, email = email, password = password)
                    val userId = db.userDao().insert(newUser)
                    sessionManager.loginUser(userId)
                    startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                    finish()
                }
            }
        }
    }
}