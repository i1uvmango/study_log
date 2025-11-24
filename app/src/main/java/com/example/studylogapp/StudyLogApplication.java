package com.example.studylogapp;

import android.app.Application;
import android.util.Log;

import com.example.studylogapp.data.network.ApiClient;

/**
 * Application 클래스
 * ApiClient 초기화를 담당합니다.
 */
public class StudyLogApplication extends Application {
    private static final String TAG = "StudyLogApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        // ApiClient 초기화
        ApiClient.init(this);
        Log.d(TAG, "✅ StudyLogApplication 초기화 완료");
    }
}

