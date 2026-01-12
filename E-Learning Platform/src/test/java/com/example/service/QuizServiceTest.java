package com.example.service;

import com.example.model.dto.QuizResponse;
import com.example.model.dto.CreateQuizRequest;
import com.example.model.entities.Quiz;
import com.example.repository.QuizRepository;
import com.example.repository.QuestionRepository;
import com.example.repository.QuizAttemptRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceTest {
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuizAttemptRepository quizAttemptRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private QuizService quizService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllQuizzes_returnsList() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        // Use 'quiz' in the test to avoid unused warning
        assertNotNull(quiz);
        when(quizRepository.findAll()).thenReturn(List.of(quiz));
        List<QuizResponse> result = quizService.getAllQuizzes();
        assertEquals(1, result.size());
    }

    @Test
    void getQuizById_found() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));
        Optional<QuizResponse> result = quizService.getQuizById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void createQuiz_success() {
        CreateQuizRequest req = new CreateQuizRequest();
        req.setTitle("Test Quiz");
        Quiz quiz = new Quiz();
        when(quizRepository.save(any(Quiz.class))).thenAnswer(i -> { Quiz q = i.getArgument(0); q.setId(1L); return q; });
        QuizResponse res = quizService.createQuiz(req);
        assertEquals("Test Quiz", res.getTitle());
    }
}
