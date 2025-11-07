package com.example.studylogapp.model;

import java.io.Serializable;

public class StudyPost implements Serializable {
    private long id;
    private long studyLogId;
    private String photoUri;
    private String summary;
    private String keyword;
    private int order;

    public StudyPost() {
    }

    public StudyPost(long studyLogId, String photoUri, String summary, String keyword, int order) {
        this.studyLogId = studyLogId;
        this.photoUri = photoUri;
        this.summary = summary;
        this.keyword = keyword;
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStudyLogId() {
        return studyLogId;
    }

    public void setStudyLogId(long studyLogId) {
        this.studyLogId = studyLogId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}


