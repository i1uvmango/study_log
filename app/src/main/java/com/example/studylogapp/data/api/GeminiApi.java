package com.example.studylogapp.data.api;

import com.example.studylogapp.data.dto.GeminiRequest;
import com.example.studylogapp.data.dto.GeminiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Gemini API 인터페이스
 * 
 * 사용 예시:
 * GeminiApi api = ApiClient.getClient().create(GeminiApi.class);
 * Call<GeminiResponse> call = api.generateContent(apiKey, request);
 */
public interface GeminiApi {
    /**
     * Gemini API를 호출하여 콘텐츠를 생성합니다.
     * 
     * @param apiKey API 키 (쿼리 파라미터)
     * @param request 요청 본문
     * @return Gemini API 응답
     */
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}

