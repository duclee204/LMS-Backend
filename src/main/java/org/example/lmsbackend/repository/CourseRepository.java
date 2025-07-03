// ------------------------
// CourseRepository.java
// ------------------------
package org.example.lmsbackend.repository;

import org.example.lmsbackend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCourseIdAndInstructor_UserId(Integer courseId, Integer instructorId);
}
