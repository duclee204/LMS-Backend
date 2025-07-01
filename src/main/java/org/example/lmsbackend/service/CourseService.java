package org.example.lmsbackend.service;

import org.example.lmsbackend.model.Course;
import org.example.lmsbackend.dto.CourseDTO;
import org.example.lmsbackend.repository.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public boolean createCourse(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setCategoryId(dto.getCategoryId());
        course.setInstructorId(dto.getInstructorId());
        course.setStatus(Course.Status.valueOf(dto.getStatus())); // ✅
        course.setPrice(BigDecimal.valueOf(100.0)); // hoặc dto.getPrice()

        return courseMapper.insertCourse(course) > 0;
    }

    public List<CourseDTO> getCourses(Integer categoryId, Integer instructorId, String status) {
        System.out.println("📦 getCourses with: categoryId=" + categoryId + ", instructorId=" + instructorId + ", status=" + status);
        return courseMapper.findCourses(categoryId, instructorId, status);
    }
    public boolean isInstructorOfCourse(int instructorId, int courseId) {
        return courseMapper.countByInstructorAndCourse(instructorId, courseId) > 0;
    }
    public boolean updateCourse(Course course) {
        return courseMapper.updateCourse(course) > 0;
    }

    public boolean deleteCourse(Integer courseId) {
        return courseMapper.deleteCourse(courseId) > 0;
    }
}