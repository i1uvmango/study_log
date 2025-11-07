package com.example.studylogapp.ui.calendar;

public class CalendarDay {
    private String date;
    private boolean isHeader;
    private boolean isToday;
    private boolean hasPost;
    private String thumbnailUri;

    public CalendarDay(String date, boolean isHeader, boolean isToday, boolean hasPost, String thumbnailUri) {
        this.date = date;
        this.isHeader = isHeader;
        this.isToday = isToday;
        this.hasPost = hasPost;
        this.thumbnailUri = thumbnailUri;
    }

    public String getDate() {
        return date;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isToday() {
        return isToday;
    }

    public boolean hasPost() {
        return hasPost;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }
}

