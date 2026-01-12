package com.example.service;

import com.example.model.dto.EditQuizRequest;
// ...existing imports...



import com.example.model.dto.MyQuizResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
import com.example.model.dto.QuestionResponse;
import com.example.model.dto.CreateQuizRequest;
import java.util.Optional;

@Service
public class QuizService {

        public QuizResponse editQuizFull(Long quizId, EditQuizRequest request) {
            Quiz quiz = quizRepository.findById(quizId).orElseThrow();
            quiz.setTitle(request.getTitle());
            // Update or delete existing questions, add new ones
            for (EditQuizRequest.QuestionDto qdto : request.getQuestions()) {
                if (qdto.getId() != null) {
                    // Existing question
                    Question q = questionRepository.findById(qdto.getId()).orElse(null);
                    if (q != null) {
                        if (qdto.isDelete()) {
                            questionRepository.delete(q);
                            continue;
                        }
                        q.setText(qdto.getText());
                        q.setOption1(qdto.getOption1());
                        q.setOption2(qdto.getOption2());
                        q.setOption3(qdto.getOption3());
                        q.setOption4(qdto.getOption4());
                        q.setCorrectOptionIndex(qdto.getCorrectOptionIndex());
                        questionRepository.save(q);
                    }
                } else if (!qdto.isDelete()) {
                    // New question
                    Question q = new Question();
                    q.setQuiz(quiz);
                    q.setText(qdto.getText());
                    q.setOption1(qdto.getOption1());
                    q.setOption2(qdto.getOption2());
                    q.setOption3(qdto.getOption3());
                    q.setOption4(qdto.getOption4());
                    q.setCorrectOptionIndex(qdto.getCorrectOptionIndex());
                    questionRepository.save(q);
                }
            }
            quizRepository.save(quiz);
            return toQuizResponse(quiz);
        }
    /**
     * Returns all quizzes with attempt status for the current authenticated user.
     */
    public List<MyQuizResponse> getQuizzesForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetails)) {
            throw new RuntimeException("No authenticated user");
        }
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizAttempt> attempts = quizAttemptRepository.findByUser_Id(user.getId());
        java.util.Set<Long> attemptedQuizIds = attempts.stream().map(a -> a.getQuiz().getId()).collect(java.util.stream.Collectors.toSet());
        return quizzes.stream().map(q -> {
            MyQuizResponse resp = new MyQuizResponse();
            resp.setId(q.getId());
            resp.setTitle(q.getTitle());
            resp.setLessonTitle(q.getLesson() != null ? q.getLesson().getTitle() : null);
            resp.setCourseTitle(q.getLesson() != null && q.getLesson().getCourse() != null ? q.getLesson().getCourse().getTitle() : null);
            resp.setAttempted(attemptedQuizIds.contains(q.getId()));
            return resp;
        }).toList();
    }
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
            // Set quiz reference on lesson
            lesson.setQuiz(quiz);
            lessonRepository.save(lesson);
        }
        return toQuizResponse(quizRepository.save(quiz));
    }

    public QuizResponse updateQuiz(Long id, CreateQuizRequest request) {
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        quiz.setTitle(request.getTitle());
        if (request.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow();
            quiz.setLesson(lesson);
            // Set quiz reference on lesson
            lesson.setQuiz(quiz);
            lessonRepository.save(lesson);
        }
        return toQuizResponse(quizRepository.save(quiz));
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz != null) {
            // Remove quiz reference from lesson
            if (quiz.getLesson() != null) {
                Lesson lesson = quiz.getLesson();
                lesson.setQuiz(null);
                lessonRepository.save(lesson);
            }
            // Delete all quiz attempts for this quiz (to avoid FK constraint)
            List<QuizAttempt> attempts = quizAttemptRepository.findByQuiz_Id(id);
            for (QuizAttempt attempt : attempts) {
                attempt.setAnswers(null); // Remove all answers
                quizAttemptRepository.save(attempt);
            }
            quizAttemptRepository.deleteAll(attempts);
            // Explicitly delete all questions for this quiz (defensive, in case cascade fails)
            questionRepository.deleteAll(questionRepository.findByQuiz_Id(id));
            quizRepository.deleteById(id);
        }
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
        // Map questions to QuestionResponse
        List<QuestionResponse> questionResponses = quiz.getQuestions().stream().map(q -> {
            QuestionResponse qr = new QuestionResponse();
            qr.setId(q.getId());
            qr.setText(q.getText());
            qr.setOption1(q.getOption1());
            qr.setOption2(q.getOption2());
            qr.setOption3(q.getOption3());
            qr.setOption4(q.getOption4());
            qr.setCorrectOptionIndex(q.getCorrectOptionIndex());
            return qr;
        }).toList();
        dto.setQuestions(questionResponses);
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
        if (options != null && options.size() == 4) {
            question.setOption1(options.get(0));
            question.setOption2(options.get(1));
            question.setOption3(options.get(2));
            question.setOption4(options.get(3));
        }
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
