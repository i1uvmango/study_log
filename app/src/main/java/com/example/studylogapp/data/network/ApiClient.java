package com.example.studylogapp.data.network;

import android.content.Context;
import android.util.Log;

import com.example.studylogapp.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String TAG = "ApiClient";
    
    // Gemini API Base URL (고정)
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    
    private static Retrofit retrofit;
    
    /**
     * Application에서 초기화해야 합니다.
     */
    public static void init(Context context) {
        if (retrofit != null) {
            Log.w(TAG, "ApiClient가 이미 초기화되었습니다.");
            return;
        }
        
        // 로깅 인터셉터 설정
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // OkHttpClient 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        
        // Retrofit 인스턴스 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        Log.d(TAG, "✅ ApiClient 초기화 완료 - Base URL: " + BASE_URL);
    }
    
    /**
     * Retrofit 인스턴스를 반환합니다.
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            throw new IllegalStateException("ApiClient가 초기화되지 않았습니다. Application에서 ApiClient.init()을 호출하세요.");
        }
        return retrofit;
    }
    
    /**
     * API 키를 반환합니다 (BuildConfig에서)
     */
    public static String getApiKey() {
        return BuildConfig.GEMINI_API_KEY;
    }
}

