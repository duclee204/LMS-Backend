// QuizController.java
package org.example.lmsbackend.controller;

import org.example.lmsbackend.dto.QuizDTO;
import org.example.lmsbackend.dto.QuizResponseDTO;
import org.example.lmsbackend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(@RequestBody QuizDTO dto) {
        Integer instructorId = getCurrentInstructorId();
        return ResponseEntity.ok(quizService.createQuiz(dto, instructorId));
    }

    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizResponseDTO> getQuizById(@PathVariable Integer quizId) {
        return quizService.getQuizById(quizId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private Integer getCurrentInstructorId() {
        return 1; // Mock dữ liệu, thay bằng userId từ JWT
    }
}