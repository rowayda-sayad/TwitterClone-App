package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceManager.getDefaultSharedPreferences(LauncherActivity.this).getBoolean("isLogged", false)) {
                    Intent intent = new Intent(LauncherActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1200);
    }
}