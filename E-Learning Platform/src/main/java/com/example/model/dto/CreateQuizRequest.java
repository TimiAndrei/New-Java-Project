package com.example.model.dto;

public class CreateQuizRequest {
    private String title;
    private Long lessonId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
}