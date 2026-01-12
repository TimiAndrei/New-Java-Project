package com.example.model.dto;

import java.util.List;

public class EditQuizRequest {
    private String title;
    private List<QuestionDto> questions;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<QuestionDto> getQuestions() { return questions; }
    public void setQuestions(List<QuestionDto> questions) { this.questions = questions; }

    public static class QuestionDto {
        private Long id; // null for new questions
        private String text;
        private String option1;
        private String option2;
        private String option3;
        private String option4;
        private int correctOptionIndex;
        private boolean delete; // true if question should be deleted

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public String getOption1() { return option1; }
        public void setOption1(String option1) { this.option1 = option1; }
        public String getOption2() { return option2; }
        public void setOption2(String option2) { this.option2 = option2; }
        public String getOption3() { return option3; }
        public void setOption3(String option3) { this.option3 = option3; }
        public String getOption4() { return option4; }
        public void setOption4(String option4) { this.option4 = option4; }
        public int getCorrectOptionIndex() { return correctOptionIndex; }
        public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
        public boolean isDelete() { return delete; }
        public void setDelete(boolean delete) { this.delete = delete; }
    }
}
