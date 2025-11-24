package com.example.studylogapp.ai;

import android.content.Context;
import android.util.Log;

import com.example.studylogapp.data.api.GeminiApi;
import com.example.studylogapp.data.dto.GeminiRequest;
import com.example.studylogapp.data.dto.GeminiResponse;
import com.example.studylogapp.data.network.ApiClient;
import com.example.studylogapp.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Gemini API를 사용하여 퀴즈를 생성하는 클래스
 * Retrofit 기반으로 재구성됨
 */
public class GeminiQuizGenerator {
    private static final String TAG = "GeminiQuizGenerator";
    
    private Context context;
    private GeminiApi geminiApi;
    
    public GeminiQuizGenerator(Context context) {
        this.context = context;
        // Retrofit을 통해 API 인스턴스 생성
        this.geminiApi = ApiClient.getClient().create(GeminiApi.class);
    }
    
    /**
     * 게시물 정보를 기반으로 퀴즈를 생성합니다.
     * 성능 최적화를 위해 텍스트(요약, 키워드)만 사용합니다.
     * 
     * @param imagePath 이미지 파일 경로 (현재는 사용하지 않음 - 성능 최적화)
     * @param summary 게시물 요약
     * @param keyword 게시물 키워드
     * @return 생성된 퀴즈 객체, 실패 시 null
     */
    public Quiz generateQuiz(String imagePath, String summary, String keyword) {
        Log.d(TAG, "퀴즈 생성 시작 - 요약: " + summary + ", 키워드: " + keyword);
        Log.d(TAG, "⚠️ 이미지는 성능 최적화를 위해 사용하지 않습니다. 텍스트만으로 퀴즈 생성합니다.");
        
        String apiKey = ApiClient.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            Log.e(TAG, "❌ Gemini API 키가 설정되지 않았습니다.");
            Log.e(TAG, "확인 사항:");
            Log.e(TAG, "1. local.properties 파일에 GEMINI_API_KEY=YOUR_KEY 형식으로 설정되어 있는지");
            Log.e(TAG, "2. Android Studio에서 File > Sync Project with Gradle Files 실행");
            Log.e(TAG, "3. 프로젝트를 Clean & Rebuild");
            return null;
        }
        
        // API 키 마스킹 (로그용)
        String maskedKey = apiKey.length() > 10 
            ? apiKey.substring(0, 5) + "..." + apiKey.substring(apiKey.length() - 5)
            : "***";
        Log.d(TAG, "✅ API 키 확인 완료 (길이: " + apiKey.length() + ", 시작: " + maskedKey + ")");

        try {
            // summary나 keyword가 없으면 퀴즈 생성 불가
            if ((summary == null || summary.trim().isEmpty()) && 
                (keyword == null || keyword.trim().isEmpty())) {
                Log.e(TAG, "❌ 퀴즈 생성 불가 - 요약과 키워드가 모두 없습니다.");
                return null;
            }

            // 프롬프트 생성
            String prompt = createPrompt(summary, keyword);
            Log.d(TAG, "✅ 프롬프트 생성 완료");

            // Gemini API 요청 객체 생성
            GeminiRequest request = createRequest(prompt);
            Log.d(TAG, "✅ 요청 객체 생성 완료");

            // 동기 호출 (백그라운드 스레드에서 실행되어야 함)
            Log.d(TAG, "🚀 Gemini API 호출 시작... (Retrofit 기반)");
            Response<GeminiResponse> response = geminiApi.generateContent(apiKey, request).execute();
            
            if (!response.isSuccessful()) {
                Log.e(TAG, "❌ API 호출 실패: HTTP " + response.code());
                if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        // 에러 메시지만 간단히 추출
                        if (errorBody.contains("\"message\"")) {
                            int msgStart = errorBody.indexOf("\"message\"");
                            int msgValueStart = errorBody.indexOf("\"", msgStart + 10) + 1;
                            int msgValueEnd = errorBody.indexOf("\"", msgValueStart);
                            if (msgValueEnd > msgValueStart) {
                                String errorMsg = errorBody.substring(msgValueStart, msgValueEnd);
                                Log.e(TAG, "에러: " + errorMsg);
                            } else {
                                Log.e(TAG, "에러 응답 (요약): " + (errorBody.length() > 200 ? errorBody.substring(0, 200) + "..." : errorBody));
                            }
                        } else {
                            Log.e(TAG, "에러 응답 (요약): " + (errorBody.length() > 200 ? errorBody.substring(0, 200) + "..." : errorBody));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "에러 응답 읽기 실패");
                    }
                }
                return null;
            }

            GeminiResponse geminiResponse = response.body();
            if (geminiResponse == null) {
                Log.e(TAG, "❌ 응답 본문이 null입니다.");
                return null;
            }

            // 에러 체크
            if (geminiResponse.getError() != null) {
                GeminiResponse.Error error = geminiResponse.getError();
                Log.e(TAG, "❌ API 에러: " + error.getCode() + " - " + error.getMessage());
                return null;
            }

            // 응답에서 텍스트 추출
            String responseText = extractTextFromResponse(geminiResponse);
            if (responseText == null || responseText.isEmpty()) {
                Log.e(TAG, "❌ 응답에서 텍스트를 추출할 수 없습니다.");
                return null;
            }
            
            Log.d(TAG, "✅ API 응답 받음, 길이: " + responseText.length() + " 문자");
            // 디버깅이 필요할 때만 주석 해제
            // Log.d(TAG, "응답 텍스트 (처음 200자): " + (responseText.length() > 200 ? responseText.substring(0, 200) + "..." : responseText));

            // 응답 파싱하여 Quiz 객체 생성
            Quiz quiz = parseQuizResponse(responseText);
            if (quiz != null) {
                Log.d(TAG, "✅ 퀴즈 파싱 성공!");
            } else {
                Log.e(TAG, "❌ 퀴즈 파싱 실패");
            }
            return quiz;

        } catch (Exception e) {
            Log.e(TAG, "❌ 퀴즈 생성 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            // 상세 디버깅이 필요할 때만 주석 해제
            // Log.e(TAG, "상세 오류:", e);
            return null;
        }
    }
    
    /**
     * Gemini API 요청 객체를 생성합니다.
     */
    private GeminiRequest createRequest(String prompt) {
        // Part 생성
        GeminiRequest.Part part = new GeminiRequest.Part(prompt);
        List<GeminiRequest.Part> parts = new ArrayList<>();
        parts.add(part);
        
        // Content 생성
        GeminiRequest.Content content = new GeminiRequest.Content(parts);
        List<GeminiRequest.Content> contents = new ArrayList<>();
        contents.add(content);
        
        // GenerationConfig 생성
        GeminiRequest.GenerationConfig generationConfig = new GeminiRequest.GenerationConfig(
            0.7,    // temperature
            40,     // topK
            0.95,   // topP
            1024    // maxOutputTokens
        );
        
        // Request 생성
        return new GeminiRequest(contents, generationConfig);
    }
    
    /**
     * Gemini API 응답에서 텍스트를 추출합니다.
     */
    private String extractTextFromResponse(GeminiResponse response) {
        if (response.getCandidates() == null || response.getCandidates().isEmpty()) {
            Log.e(TAG, "❌ 응답에 'candidates' 배열이 없거나 비어있습니다.");
            return null;
        }
        
        GeminiResponse.Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null) {
            Log.e(TAG, "❌ candidate에 'content'가 없습니다.");
            return null;
        }
        
        if (candidate.getContent().getParts() == null || candidate.getContent().getParts().isEmpty()) {
            Log.e(TAG, "❌ content에 'parts' 배열이 없거나 비어있습니다.");
            return null;
        }
        
        GeminiResponse.Part part = candidate.getContent().getParts().get(0);
        if (part.getText() == null || part.getText().isEmpty()) {
            Log.e(TAG, "❌ part에 'text' 필드가 없거나 비어있습니다.");
            return null;
        }
        
        return part.getText();
    }

    /**
     * 프롬프트를 생성합니다.
     */
    private String createPrompt(String summary, String keyword) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 학습 내용을 기반으로 간단한 복습용 객관식 퀴즈를 만들어주세요.\n\n");
        
        if (summary != null && !summary.trim().isEmpty()) {
            prompt.append("요약: ").append(summary).append("\n");
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            prompt.append("키워드: ").append(keyword).append("\n");
        }
        
        if ((summary == null || summary.trim().isEmpty()) && 
            (keyword == null || keyword.trim().isEmpty())) {
            prompt.append("제공된 학습 내용을 바탕으로 퀴즈를 만들어주세요.\n");
        }
        
        prompt.append("\n다음 형식으로 JSON 형태로 응답해주세요:\n");
        prompt.append("{\n");
        prompt.append("  \"question\": \"퀴즈 문제\",\n");
        prompt.append("  \"option1\": \"선택지 1\",\n");
        prompt.append("  \"option2\": \"선택지 2\",\n");
        prompt.append("  \"option3\": \"선택지 3\",\n");
        prompt.append("  \"option4\": \"선택지 4\",\n");
        prompt.append("  \"correctAnswer\": 1,\n");
        prompt.append("  \"explanation\": \"정답 설명\"\n");
        prompt.append("}\n\n");
        prompt.append("문제는 간단하고 명확하게, 선택지는 모두 그럴듯하게 만들어주세요. ");
        prompt.append("correctAnswer는 1, 2, 3, 4 중 하나의 숫자입니다.");
        
        return prompt.toString();
    }

    /**
     * Gemini API 응답을 파싱하여 Quiz 객체로 변환합니다.
     */
    private Quiz parseQuizResponse(String responseText) {
        try {
            responseText = responseText.trim();
            
            // JSON 블록 추출
            int jsonStart = responseText.indexOf("{");
            int jsonEnd = responseText.lastIndexOf("}") + 1;
            if (jsonStart == -1 || jsonEnd == 0) {
                Log.e(TAG, "❌ JSON 형식을 찾을 수 없습니다.");
                Log.e(TAG, "응답 (처음 200자): " + (responseText.length() > 200 ? responseText.substring(0, 200) + "..." : responseText));
                return null;
            }
            
            String jsonText = responseText.substring(jsonStart, jsonEnd);
            org.json.JSONObject json = new org.json.JSONObject(jsonText);
            
            // JSON 파싱
            String question = json.optString("question", "");
            String option1 = json.optString("option1", "");
            String option2 = json.optString("option2", "");
            String option3 = json.optString("option3", "");
            String option4 = json.optString("option4", "");
            int correctAnswer = json.optInt("correctAnswer", 1);
            String explanation = json.optString("explanation", "");
            
            // Quiz 객체 생성 (studyLogId는 나중에 설정)
            Quiz quiz = new Quiz();
            quiz.setQuestion(question);
            quiz.setOption1(option1);
            quiz.setOption2(option2);
            quiz.setOption3(option3);
            quiz.setOption4(option4);
            quiz.setCorrectAnswer(correctAnswer);
            quiz.setExplanation(explanation);
            
            return quiz;
            
        } catch (Exception e) {
            Log.e(TAG, "❌ 응답 파싱 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            Log.e(TAG, "응답 (처음 200자): " + (responseText.length() > 200 ? responseText.substring(0, 200) + "..." : responseText));
            return null;
        }
    }
}
