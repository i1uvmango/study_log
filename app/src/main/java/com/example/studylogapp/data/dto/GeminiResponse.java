package com.example.studylogapp.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Gemini API 응답 DTO
 */
public class GeminiResponse {
    @SerializedName("candidates")
    private List<Candidate> candidates;
    
    @SerializedName("error")
    private Error error;
    
    public GeminiResponse() {
    }
    
    public List<Candidate> getCandidates() {
        return candidates;
    }
    
    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }
    
    public Error getError() {
        return error;
    }
    
    public void setError(Error error) {
        this.error = error;
    }
    
    /**
     * Candidate 내부 클래스
     */
    public static class Candidate {
        @SerializedName("content")
        private Content content;
        
        public Candidate() {
        }
        
        public Content getContent() {
            return content;
        }
        
        public void setContent(Content content) {
            this.content = content;
        }
    }
    
    /**
     * Content 내부 클래스
     */
    public static class Content {
        @SerializedName("parts")
        private List<Part> parts;
        
        public Content() {
        }
        
        public List<Part> getParts() {
            return parts;
        }
        
        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }
    
    /**
     * Part 내부 클래스
     */
    public static class Part {
        @SerializedName("text")
        private String text;
        
        public Part() {
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
    }
    
    /**
     * Error 내부 클래스
     */
    public static class Error {
        @SerializedName("code")
        private Integer code;
        
        @SerializedName("message")
        private String message;
        
        @SerializedName("status")
        private String status;
        
        public Error() {
        }
        
        public Integer getCode() {
            return code;
        }
        
        public void setCode(Integer code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
}

