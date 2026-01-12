package com.example.model.dto;

import java.util.List;

public class CourseResponse {
    private boolean enrolled;
        public boolean isEnrolled() { return enrolled; }
        public void setEnrolled(boolean enrolled) { this.enrolled = enrolled; }
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String category;
    private String instructor;
    private String status;
    private List<String> lessonTitles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getLessonTitles() { return lessonTitles; }
    public void setLessonTitles(List<String> lessonTitles) { this.lessonTitles = lessonTitles; }
}