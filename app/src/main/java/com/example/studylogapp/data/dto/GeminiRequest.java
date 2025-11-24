package com.example.studylogapp.data.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Gemini API 요청 DTO
 */
public class GeminiRequest {
    @SerializedName("contents")
    private List<Content> contents;
    
    @SerializedName("generationConfig")
    private GenerationConfig generationConfig;
    
    public GeminiRequest() {
    }
    
    public GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {
        this.contents = contents;
        this.generationConfig = generationConfig;
    }
    
    public List<Content> getContents() {
        return contents;
    }
    
    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
    
    public GenerationConfig getGenerationConfig() {
        return generationConfig;
    }
    
    public void setGenerationConfig(GenerationConfig generationConfig) {
        this.generationConfig = generationConfig;
    }
    
    /**
     * Content 내부 클래스
     */
    public static class Content {
        @SerializedName("parts")
        private List<Part> parts;
        
        public Content() {
        }
        
        public Content(List<Part> parts) {
            this.parts = parts;
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
        
        public Part(String text) {
            this.text = text;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
    }
    
    /**
     * GenerationConfig 내부 클래스
     */
    public static class GenerationConfig {
        @SerializedName("temperature")
        private Double temperature;
        
        @SerializedName("topK")
        private Integer topK;
        
        @SerializedName("topP")
        private Double topP;
        
        @SerializedName("maxOutputTokens")
        private Integer maxOutputTokens;
        
        public GenerationConfig() {
        }
        
        public GenerationConfig(Double temperature, Integer topK, Double topP, Integer maxOutputTokens) {
            this.temperature = temperature;
            this.topK = topK;
            this.topP = topP;
            this.maxOutputTokens = maxOutputTokens;
        }
        
        public Double getTemperature() {
            return temperature;
        }
        
        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }
        
        public Integer getTopK() {
            return topK;
        }
        
        public void setTopK(Integer topK) {
            this.topK = topK;
        }
        
        public Double getTopP() {
            return topP;
        }
        
        public void setTopP(Double topP) {
            this.topP = topP;
        }
        
        public Integer getMaxOutputTokens() {
            return maxOutputTokens;
        }
        
        public void setMaxOutputTokens(Integer maxOutputTokens) {
            this.maxOutputTokens = maxOutputTokens;
        }
    }
}

