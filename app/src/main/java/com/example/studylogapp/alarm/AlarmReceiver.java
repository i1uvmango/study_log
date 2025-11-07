package com.example.studylogapp.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.studylogapp.ui.posting.PostingActivity;
import com.example.studylogapp.utils.DateUtils;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                // 부팅 완료 시 알람 재설정
                AlarmManagerHelper.scheduleDailyAlarm(context);
            } else if (intent.getAction().equals(AlarmManagerHelper.ALARM_ACTION)) {
                // 알람 수신 시 게시물 작성 화면으로 이동
                Intent postingIntent = new Intent(context, PostingActivity.class);
                postingIntent.putExtra("date", DateUtils.formatDate(Calendar.getInstance()));
                postingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(postingIntent);
            }
        }
    }
}

