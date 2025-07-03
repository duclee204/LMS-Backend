package org.example.lmsbackend.controller;

import org.example.lmsbackend.model.Course;
import org.example.lmsbackend.dto.CourseDTO;
import org.example.lmsbackend.security.CustomUserDetails;
import org.example.lmsbackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseRestController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
        if (courseDTO.getPrice() == null) {
            return ResponseEntity.badRequest().body("Giá khóa học không được để trống.");
        }
        if (courseDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Giá phải lớn hơn 0.");
        }

        boolean created = courseService.createCourse(courseDTO);
        if (!created) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tạo khóa học thất bại");
        }
        System.out.println("✅ Giá nhận từ client: " + courseDTO.getPrice());
        return ResponseEntity.ok("Tạo khóa học thành công");
    }
    @GetMapping("/list")
    @PreAuthorize("hasRole('admin') or hasRole('instructor')")
    public ResponseEntity<List<CourseDTO>> listCourses(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String status
    ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer instructorId = null;

        if (principal instanceof CustomUserDetails customUser) {
            boolean isInstructor = customUser.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_instructor"));
            if (isInstructor) {
                instructorId = customUser.getId();
            }
            System.out.println("🔍 User ID: " + customUser.getId());
        }

        System.out.printf("getCourses with: categoryId=%s, instructorId=%s, status=%s%n",
                categoryId, instructorId, status);

        // 🔁 Gọi service trả về DTO thay vì entity
        List<CourseDTO> courses = courseService.getCourses(categoryId, instructorId, status);
        return ResponseEntity.ok(courses);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateCourse(@PathVariable("id") Integer courseId, @RequestBody Course course) {
        course.setCourseId(courseId);
        boolean updated = courseService.updateCourse(course);
        if (updated) {
            return ResponseEntity.ok("Course updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Integer courseId) {
        boolean deleted = courseService.deleteCourse(courseId);
        if (deleted) {
            return ResponseEntity.ok("Course deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
    }

}
