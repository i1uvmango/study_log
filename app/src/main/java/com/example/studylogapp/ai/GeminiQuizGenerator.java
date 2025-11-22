package com.example.studylogapp.ai;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.studylogapp.model.Quiz;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GeminiQuizGenerator {
    private static final String TAG = "GeminiQuizGenerator";
    private static final String API_KEY = "AIzaSyCbdxqUscDaHZzYDA8kW1__2GrSp4Xgkt8"; // TODO: API 키를 여기에 입력하세요
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent?key=";
    
    private Context context;

    public GeminiQuizGenerator(Context context) {
        this.context = context;
    }

    /**
     * 게시물 정보를 기반으로 퀴즈를 생성합니다.
     * 
     * @param imagePath 이미지 파일 경로
     * @param summary 게시물 요약
     * @param keyword 게시물 키워드
     * @return 생성된 퀴즈 객체, 실패 시 null
     */
    public Quiz generateQuiz(String imagePath, String summary, String keyword) {
        if (API_KEY.equals("YOUR_GEMINI_API_KEY")) {
            Log.e(TAG, "Gemini API 키가 설정되지 않았습니다.");
            return null;
        }

        try {
            // 이미지 로드 및 Base64 인코딩
            String imageBase64 = encodeImageToBase64(imagePath);
            if (imageBase64 == null) {
                Log.e(TAG, "이미지를 로드할 수 없습니다: " + imagePath);
                return null;
            }

            // 프롬프트 생성
            String prompt = createPrompt(summary, keyword);

            // Gemini API 호출
            String responseText = callGeminiAPI(prompt, imageBase64);
            if (responseText == null) {
                return null;
            }

            // 응답 파싱
            return parseQuizResponse(responseText);

        } catch (Exception e) {
            Log.e(TAG, "퀴즈 생성 중 오류 발생", e);
            return null;
        }
    }

    /**
     * Gemini API를 호출합니다.
     */
    private String callGeminiAPI(String prompt, String imageBase64) {
        try {
            URL url = new URL(API_URL + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // 요청 본문 생성
            JSONObject requestBody = new JSONObject();
            JSONObject content = new JSONObject();
            JSONObject part1 = new JSONObject();
            part1.put("text", prompt);
            
            JSONObject part2 = new JSONObject();
            JSONObject inlineData = new JSONObject();
            inlineData.put("mime_type", "image/jpeg");
            inlineData.put("data", imageBase64);
            part2.put("inline_data", inlineData);
            
            content.put("parts", new org.json.JSONArray().put(part1).put(part2));
            requestBody.put("contents", new org.json.JSONArray().put(content));

            // 요청 전송
            OutputStream os = connection.getOutputStream();
            os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            // 응답 읽기
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "API 호출 실패: " + responseCode);
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                errorReader.close();
                Log.e(TAG, "에러 응답: " + errorResponse.toString());
                return null;
            }

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // JSON 응답에서 텍스트 추출
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("candidates") && jsonResponse.getJSONArray("candidates").length() > 0) {
                JSONObject candidate = jsonResponse.getJSONArray("candidates").getJSONObject(0);
                if (candidate.has("content") && candidate.getJSONObject("content").has("parts")) {
                    JSONObject part = candidate.getJSONObject("content")
                        .getJSONArray("parts").getJSONObject(0);
                    if (part.has("text")) {
                        return part.getString("text");
                    }
                }
            }

            return null;

        } catch (Exception e) {
            Log.e(TAG, "API 호출 중 오류 발생", e);
            return null;
        }
    }

    /**
     * 이미지를 Base64로 인코딩합니다.
     */
    private String encodeImageToBase64(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return null;
            }
            
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageBytes = new byte[(int) imageFile.length()];
            fis.read(imageBytes);
            fis.close();
            
            return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        } catch (IOException e) {
            Log.e(TAG, "이미지 인코딩 실패", e);
            return null;
        }
    }

    /**
     * 프롬프트를 생성합니다.
     */
    private String createPrompt(String summary, String keyword) {
        return "다음 학습 내용을 기반으로 간단한 복습용 객관식 퀴즈를 만들어주세요.\n\n" +
                "요약: " + summary + "\n" +
                "키워드: " + keyword + "\n\n" +
                "다음 형식으로 JSON 형태로 응답해주세요:\n" +
                "{\n" +
                "  \"question\": \"퀴즈 문제\",\n" +
                "  \"option1\": \"선택지 1\",\n" +
                "  \"option2\": \"선택지 2\",\n" +
                "  \"option3\": \"선택지 3\",\n" +
                "  \"option4\": \"선택지 4\",\n" +
                "  \"correctAnswer\": 1,\n" +
                "  \"explanation\": \"정답 설명\"\n" +
                "}\n\n" +
                "문제는 간단하고 명확하게, 선택지는 모두 그럴듯하게 만들어주세요. " +
                "correctAnswer는 1, 2, 3, 4 중 하나의 숫자입니다.";
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
                Log.e(TAG, "JSON 형식을 찾을 수 없습니다. 응답: " + responseText);
                return null;
            }
            
            String jsonText = responseText.substring(jsonStart, jsonEnd);
            JSONObject json = new JSONObject(jsonText);
            
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
            Log.e(TAG, "응답 파싱 실패", e);
            Log.e(TAG, "응답 텍스트: " + responseText);
            return null;
        }
    }

}

