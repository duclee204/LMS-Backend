package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.QuizDTO;
import org.example.lmsbackend.dto.QuizResponseDTO;
import org.example.lmsbackend.model.Course;
import org.example.lmsbackend.model.Quiz;
import org.example.lmsbackend.repository.CourseRepository;
import org.example.lmsbackend.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CourseRepository courseRepository;

    public QuizResponseDTO createQuiz(QuizDTO dto, Integer instructorId) {
        Course course = courseRepository
                .findByCourseIdAndInstructor_UserId(dto.getCourseId(), instructorId)
                .orElseThrow(() -> new RuntimeException("Bạn không có quyền tạo bài kiểm tra cho khóa học này."));

        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setQuizType(dto.getQuizType());
        quiz.setTimeLimit(dto.getTimeLimit());
        quiz.setShuffleAnswers(dto.getShuffleAnswers());
        quiz.setAllowMultipleAttempts(dto.getAllowMultipleAttempts());
        quiz.setShowQuizResponses(dto.getShowQuizResponses());
        quiz.setShowOneQuestionAtATime(dto.getShowOneQuestionAtATime());
        quiz.setDueDate(dto.getDueDate());
        quiz.setAvailableFrom(dto.getAvailableFrom());
        quiz.setAvailableUntil(dto.getAvailableUntil());
        quiz.setPublish(dto.getPublish()); // Mới thêm trường "publish"
        quiz.setCourse(course);

        Quiz savedQuiz = quizRepository.save(quiz);
        return toResponseDTO(savedQuiz);
    }

    public List<QuizResponseDTO> getAllQuizzes() {
        return quizRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<QuizResponseDTO> getQuizById(Integer quizId) {
        return quizRepository.findById(quizId)
                .map(this::toResponseDTO);
    }

    private QuizResponseDTO toResponseDTO(Quiz quiz) {
        QuizResponseDTO dto = new QuizResponseDTO();
        dto.setId(quiz.getQuizId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setQuizType(quiz.getQuizType());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setShuffleAnswers(quiz.getShuffleAnswers());
        dto.setAllowMultipleAttempts(quiz.getAllowMultipleAttempts());
        dto.setShowQuizResponses(quiz.getShowQuizResponses());
        dto.setShowOneQuestionAtATime(quiz.getShowOneQuestionAtATime());
        dto.setDueDate(quiz.getDueDate());
        dto.setAvailableFrom(quiz.getAvailableFrom());
        dto.setAvailableUntil(quiz.getAvailableUntil());
        dto.setPublish(quiz.getPublish());

        if (quiz.getCourse() != null) {
            dto.setCourseId(quiz.getCourseId());
            dto.setCourseTitle(quiz.getCourse().getTitle());

            if (quiz.getCourse().getCategory() != null) {
                dto.setCategoryName(quiz.getCourse().getCategory().getName());
            }
        }

        return dto;
    }
}
