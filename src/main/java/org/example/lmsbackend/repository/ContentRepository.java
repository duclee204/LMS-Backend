package org.example.lmsbackend.repository;

import org.example.lmsbackend.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Integer> {
    List<Content> findByModuleId(Integer moduleId);
    List<Content> findAllByModule_Course_CourseId(Integer courseId);
}
