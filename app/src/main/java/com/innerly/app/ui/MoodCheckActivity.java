package com.innerly.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.innerly.app.R;
import com.innerly.app.utils.SessionManager;

public class MoodCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_check);

        // SessionManager එක සාදා ගැනීම
        final SessionManager sessionManager = new SessionManager(this);

        // "Start Reflecting" බොත්තම ක්ලික් කළ විට සිදුවන දේ
        findViewById(R.id.btnStartReflecting).setOnClickListener(v -> {

            // පරිශීලකයා දැනටමත් ලොගින් වී ඇත්නම් HomeActivity වෙත යැවීම
            if (sessionManager.isLoggedIn()) {
                Intent intent = new Intent(MoodCheckActivity.this, HomeActivity.class);
                startActivity(intent);
            }
            // ලොගින් වී නොමැති නම් LoginActivity වෙත යැවීම
            else {
                Intent intent = new Intent(MoodCheckActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            // මෙම Screen එක වසා දැමීම
            finish();
        });
    }
}