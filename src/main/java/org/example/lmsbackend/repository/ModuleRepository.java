package org.example.lmsbackend.repository;

import org.example.lmsbackend.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findByCourse_CourseId(Integer courseId);
}
