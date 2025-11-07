package com.example.studylogapp.ui.settings;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studylogapp.R;
import com.example.studylogapp.alarm.AlarmManagerHelper;
import com.example.studylogapp.database.AppDatabase;

public class SettingsActivity extends AppCompatActivity {
    private TextView tvAlarmTime;
    private Switch switchAlarm;
    private Button btnSetAlarmTime, btnClearData;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        database = new AppDatabase(this);
        database.open();

        tvAlarmTime = findViewById(R.id.tv_alarm_time);
        switchAlarm = findViewById(R.id.switch_alarm);
        btnSetAlarmTime = findViewById(R.id.btn_set_alarm_time);
        btnClearData = findViewById(R.id.btn_clear_data);

        updateAlarmTimeDisplay();
        switchAlarm.setChecked(AlarmManagerHelper.isAlarmEnabled(this));

        btnSetAlarmTime.setOnClickListener(v -> showTimePicker());
        switchAlarm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AlarmManagerHelper.setAlarmEnabled(SettingsActivity.this, isChecked);
            Toast.makeText(SettingsActivity.this, 
                isChecked ? "알람이 켜졌습니다" : "알람이 꺼졌습니다", 
                Toast.LENGTH_SHORT).show();
        });

        btnClearData.setOnClickListener(v -> showClearDataDialog());
    }

    private void updateAlarmTimeDisplay() {
        int hour = AlarmManagerHelper.getAlarmHour(this);
        int minute = AlarmManagerHelper.getAlarmMinute(this);
        String timeStr = String.format("%02d:%02d", hour, minute);
        tvAlarmTime.setText(timeStr);
    }

    private void showTimePicker() {
        int currentHour = AlarmManagerHelper.getAlarmHour(this);
        int currentMinute = AlarmManagerHelper.getAlarmMinute(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            (view, hourOfDay, minute) -> {
                AlarmManagerHelper.setAlarmTime(SettingsActivity.this, hourOfDay, minute);
                updateAlarmTimeDisplay();
                Toast.makeText(SettingsActivity.this, "알람 시간이 설정되었습니다", Toast.LENGTH_SHORT).show();
            },
            currentHour, currentMinute, true);
        timePickerDialog.show();
    }

    private void showClearDataDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.clear_data)
            .setMessage(R.string.clear_data_confirm)
            .setPositiveButton(R.string.yes, (dialog, which) -> {
                database.clearAllData();
                Toast.makeText(SettingsActivity.this, "모든 데이터가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton(R.string.no, null)
            .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}

