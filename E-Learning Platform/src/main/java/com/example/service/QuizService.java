package com.example.service;

import com.example.model.entities.*;
import com.example.repository.QuizRepository;
import com.example.repository.QuestionRepository;
import com.example.repository.QuizAttemptRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.example.model.dto.QuizResponse;
import com.example.model.dto.CreateQuizRequest;
import java.util.Optional;

@Service
public class QuizService {
        @Autowired
        private com.example.repository.LessonRepository lessonRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private UserRepository userRepository;
    public List<QuizResponse> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        System.out.println("DEBUG: Found " + quizzes.size() + " quizzes in DB");
        return quizzes.stream().map(this::toQuizResponse).toList();
    }

    public Optional<QuizResponse> getQuizById(Long id) {
        return quizRepository.findById(id).map(this::toQuizResponse);
    }

    public QuizResponse createQuiz(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow();
            quiz.setLesson(lesson);
        }
        return toQuizResponse(quizRepository.save(quiz));
    }

    public QuizResponse updateQuiz(Long id, CreateQuizRequest request) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        quiz.setTitle(request.getTitle());
        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow();
            quiz.setLesson(lesson);
        }
        return toQuizResponse(quizRepository.save(quiz));
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    private QuizResponse toQuizResponse(Quiz quiz) {
        QuizResponse dto = new QuizResponse();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        if (quiz.getLesson() != null) {
            dto.setLessonId(quiz.getLesson().getId());
            dto.setLessonTitle(quiz.getLesson().getTitle());
            if (quiz.getLesson().getCourse() != null) {
                dto.setCourseTitle(quiz.getLesson().getCourse().getTitle());
            }
        }
        return dto;
        }
    public Quiz createQuiz(String title, Long courseId) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        // Course should be set by controller/service
        return quizRepository.save(quiz);
    }

    public Question addQuestion(Long quizId, String text, List<String> options, int correctOptionIndex) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        Question question = new Question();
        question.setQuiz(quiz);
        question.setText(text);
        question.setOptions(options);
        question.setCorrectOptionIndex(correctOptionIndex);
        return questionRepository.save(question);
    }

    public QuizAttempt submitAttempt(Long userId, Long quizId, Map<Long, Integer> answers) {
        User user = userRepository.findById(userId).orElseThrow();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        int score = 0;
        for (Map.Entry<Long, Integer> entry : answers.entrySet()) {
            Question question = questionRepository.findById(entry.getKey()).orElseThrow();
            if (question.getCorrectOptionIndex() == entry.getValue()) {
                score++;
            }
        }
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setAnswers(answers);
        attempt.setScore(score);
        attempt.setSubmittedAt(LocalDateTime.now());
        return quizAttemptRepository.save(attempt);
    }
}
