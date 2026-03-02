package com.innerly.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.innerly.app.R;
import com.innerly.app.data.AppDatabase;
import com.innerly.app.data.User;
import com.innerly.app.utils.SessionManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Database සහ SessionManager සාදා ගැනීම
        final AppDatabase db = AppDatabase.getDatabase(this);
        final SessionManager sessionManager = new SessionManager(this);

        // UI කොටස් සම්බන්ධ කිරීම
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        // Login බොත්තම ක්ලික් කළ විට
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // හිස් දත්ත පරීක්ෂාව
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_fill_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Database එක පරීක්ෂා කිරීමට Background Thread එකක් භාවිතා කිරීම
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                // Email එක මගින් User කෙනෙක් සිටීදැයි බැලීම
                User user = db.userDao().getUserByEmail(email);

                handler.post(() -> {
                    // Password එක නිවැරදිදැයි බැලීම
                    if (user != null && user.getPassword().equals(password)) {
                        // සාර්ථක නම් Session එකේ ID එක තබාගෙන Home Screen එකට යෑම
                        sessionManager.loginUser(user.getId());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // දත්ත වැරදි නම් පණිවිඩයක් පෙන්වීම
                        Toast.makeText(LoginActivity.this, R.string.error_invalid_credentials, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Register Screen එකට යෑම
        tvCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}