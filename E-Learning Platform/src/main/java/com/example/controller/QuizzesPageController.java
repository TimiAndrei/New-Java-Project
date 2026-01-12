package com.example.controller;

import com.example.service.QuizService;
import com.example.model.dto.MyQuizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizzesPageController {
    private final QuizService quizService;

    @Autowired
    public QuizzesPageController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/quizzes")
    public String showQuizzes(Model model) {
        java.util.List<MyQuizResponse> myQuizzes = quizService.getQuizzesForCurrentUser();
        model.addAttribute("myQuizzes", myQuizzes);
        return "quizzes";
    }
}
