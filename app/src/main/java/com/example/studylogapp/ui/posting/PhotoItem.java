package com.example.studylogapp.ui.posting;

public class PhotoItem {
    private String imagePath;
    private String summary;
    private String keyword;

    public PhotoItem(String imagePath, String summary, String keyword) {
        this.imagePath = imagePath;
        this.summary = summary;
        this.keyword = keyword;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}

