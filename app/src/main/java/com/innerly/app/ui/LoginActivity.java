package com.innerly.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.innerly.app.R;
import com.innerly.app.data.DatabaseHelper;
import com.innerly.app.utils.SessionManager;
import com.innerly.app.utils.PasswordUtils; // PasswordUtils import කරන්න

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // 1. Basic Input Validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Password එක Hash කිරීම (Guideline 4.1 අනුව)
            // පරිශීලකයා ලොග් වීමේදී ලබාදෙන මුරපදය Hash කර එය DB එකේ ඇති Hash එක සමඟ සැසඳිය යුතුය
            String hashedInput = PasswordUtils.hashPassword(password);

            if (hashedInput == null) {
                Toast.makeText(this, "Error processing login", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. SQLite Database එකෙන් User පරීක්ෂා කිරීම (Hashed Password එක භාවිතා කරමින්)
            int userId = dbHelper.checkUser(email, hashedInput);

            if (userId != -1) {
                // Login සාර්ථකයි නම් Session එක update කර Home එකට යන්න
                sessionManager.loginUser(userId);
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                // දත්ත වැරදි නම් (Email එක හෝ Hashed Password එක වැරදියි)
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });

        tvCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}