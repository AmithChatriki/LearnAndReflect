package com.example.flashcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                Boolean check = pref.getBoolean("flag", false);

                Intent iNext;
                if (check) {
                    iNext = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    iNext = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(iNext);
                finish();
            }
        }, 3000);

    }
}