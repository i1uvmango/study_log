package com.example.studylogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studylogapp.alarm.AlarmManagerHelper;
import com.example.studylogapp.ui.calendar.CalendarActivity;
import com.example.studylogapp.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 알람 설정
        AlarmManagerHelper.scheduleDailyAlarm(this);

        Button btnCalendar = findViewById(R.id.btn_calendar);
        Button btnSettings = findViewById(R.id.btn_settings);

        btnCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}

