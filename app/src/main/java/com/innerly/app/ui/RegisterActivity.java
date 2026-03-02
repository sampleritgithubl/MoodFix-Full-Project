package com.innerly.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.innerly.app.R;
import com.innerly.app.data.AppDatabase;
import com.innerly.app.data.User;
import com.innerly.app.utils.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Database සහ SessionManager සාදා ගැනීම
        final AppDatabase db = AppDatabase.getDatabase(this);
        final SessionManager sessionManager = new SessionManager(this);

        // UI කොටස් සම්බන්ධ කිරීම
        final EditText etName = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // "Create Account" බොත්තම ක්ලික් කළ විට
        btnCreateAccount.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // හිස් දත්ත පරීක්ෂාව
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Database වැඩ සඳහා Background Thread එකක් භාවිතා කිරීම
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                // 1. දැනටමත් මෙම Email එකෙන් User කෙනෙක් සිටීදැයි බැලීම
                User existingUser = db.userDao().getUserByEmail(email);

                handler.post(() -> {
                    if (existingUser != null) {
                        // Email එක දැනටමත් තිබේ නම්
                        Toast.makeText(RegisterActivity.this, R.string.error_email_exists, Toast.LENGTH_SHORT).show();
                    } else {
                        // 2. අලුත් User කෙනෙක් සාදා Database එකට ඇතුළත් කිරීම
                        executor.execute(() -> {
                            User newUser = new User();
                            newUser.setName(name);
                            newUser.setEmail(email);
                            newUser.setPassword(password);

                            long userId = db.userDao().insert(newUser);

                            handler.post(() -> {
                                // 3. සාර්ථකව ඇතුළත් කළ පසු Session එක Update කර Home Screen එකට යෑම
                                sessionManager.loginUser(userId);
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        });
                    }
                });
            });
        });
    }
}