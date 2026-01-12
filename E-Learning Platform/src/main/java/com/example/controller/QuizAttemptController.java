package com.example.controller;

import com.example.model.entities.QuizAttempt;
import com.example.service.QuizService;
import com.example.model.entities.User;
import com.example.model.entities.Quiz;
import com.example.model.entities.Question;
import com.example.repository.UserRepository;
import com.example.repository.QuizRepository;
import com.example.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QuizAttemptController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/quizzes/{quizId}/submit")
    public String submitQuizAttempt(@PathVariable Long quizId, @RequestParam Map<String, String> params, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof com.example.security.CustomUserDetails userDetails) {
                userId = userDetails.getUser().getId();
            } else {
                String email = auth.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) userId = user.getId();
            }
        }
        if (userId == null) return "redirect:/login";
        // Parse answers from params
        Map<Long, Integer> answers = new HashMap<>();
        for (String key : params.keySet()) {
            if (key.startsWith("answers[")) {
                String qidStr = key.substring(8, key.length() - 1);
                Long qid = Long.valueOf(qidStr);
                Integer selected = Integer.valueOf(params.get(key));
                answers.put(qid, selected);
            }
        }
        QuizAttempt attempt = quizService.submitAttempt(userId, quizId, answers);
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        model.addAttribute("quiz", quiz);
        model.addAttribute("attempt", attempt);
        // For result display: fetch questions and mark selected/correct
        Map<Long, Question> questionMap = new HashMap<>();
        for (Question q : quiz.getQuestions()) questionMap.put(q.getId(), q);
        model.addAttribute("questionMap", questionMap);
        return "quiz_result";
    }
}
