
package com.example.controller;
import com.example.model.dto.EditQuizRequest;

import com.example.model.dto.QuizResponse;
import com.example.model.dto.CreateQuizRequest;
import com.example.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @PutMapping("/{id}/edit-full")
    public QuizResponse editQuizFull(@PathVariable Long id, @RequestBody EditQuizRequest request) {
        return quizService.editQuizFull(id, request);
    }
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizResponse> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable Long id) {
        return ResponseEntity.of(quizService.getQuizById(id));
    }

    @PostMapping
    public QuizResponse createQuiz(@RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request);
    }

    @PutMapping("/{id}")
    public QuizResponse updateQuiz(@PathVariable Long id, @RequestBody CreateQuizRequest request) {
        return quizService.updateQuiz(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
    }
}
