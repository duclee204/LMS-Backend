package org.example.lmsbackend.repository;

import org.apache.ibatis.annotations.*;
import org.example.lmsbackend.model.Course;
import org.example.lmsbackend.dto.CourseDTO;

import java.util.List;

@Mapper
public interface CourseMapper {

    @Insert("INSERT INTO courses (title, description, category_id, instructor_id, status, price) " +
            "VALUES (#{title}, #{description}, #{categoryId}, #{instructorId}, #{status}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    int insertCourse(Course course);
    @Select("""
    SELECT 
        c.course_id AS id,
        c.title,
        c.description,
        c.status,
        c.price,
        c.created_at AS createdAt,
        c.updated_at AS updatedAt,
        c.category_id AS categoryId,
        cat.name AS categoryName,
        c.instructor_id AS instructorId,
        u.full_name AS instructorName
    FROM courses c
    LEFT JOIN users u ON c.instructor_id = u.user_id
    LEFT JOIN categories cat ON c.category_id = cat.category_id
    WHERE (#{categoryId} IS NULL OR c.category_id = #{categoryId})
      AND (#{instructorId} IS NULL OR c.instructor_id = #{instructorId})
      AND (#{status} IS NULL OR c.status = #{status})
""")
    List<CourseDTO> findCourses(
            @Param("categoryId") Integer categoryId,
            @Param("instructorId") Integer instructorId,
            @Param("status") String status
    );

    @Update("""
        UPDATE courses
        SET 
            title = #{title},
            description = #{description},
            category_id = #{categoryId},
            instructor_id = #{instructorId},
            status = #{status},
            price = #{price},
            updated_at = CURRENT_TIMESTAMP
        WHERE course_id = #{courseId}
    """)
    int updateCourse(Course course);

    @Delete("DELETE FROM courses WHERE course_id = #{courseId}")
    int deleteCourse(@Param("courseId") Integer courseId);
    @Select("SELECT COUNT(*) FROM courses WHERE course_id = #{courseId} AND instructor_id = #{instructorId}")
    int countByInstructorAndCourse(@Param("instructorId") int instructorId, @Param("courseId") int courseId);

}
