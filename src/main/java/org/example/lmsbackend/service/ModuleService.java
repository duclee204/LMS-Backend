package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.ModulesDTO;
import org.example.lmsbackend.model.Course;
import org.example.lmsbackend.model.Module;
import org.example.lmsbackend.model.User;
import org.example.lmsbackend.repository.CourseRepository;
import org.example.lmsbackend.repository.ModuleRepository;
import org.example.lmsbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.example.lmsbackend.model.Content;

@Service
public class ModuleService {
    @Autowired private CourseRepository courseRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private UserRepository userRepository;

    public List<Module> getModulesByCourseId(int courseId) {
        return moduleRepository.findByCourse_CourseId(courseId);
    }

    public Module createModule(String username, ModulesDTO dto) {
        User user = userRepository.findByUsername(username);
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructor().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You are not the instructor of this course.");
        }

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrderNumber(dto.getOrderNumber());

        // ✅ KHỞI TẠO TRẠNG THÁI MẶC ĐỊNH
        module.setPublished(false);

        return moduleRepository.save(module);
    }

    public Module getModuleById(int moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }
    public Module updateModuleStatus(int moduleId, boolean published, String username) {
        Module module = getModuleById(moduleId);
        if (!module.getCourse().getInstructor().getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this module");
        }

        module.setPublished(published);

        // Cập nhật tất cả content trong module theo trạng thái module
        for (Content content : module.getContents()) {
            content.setPublished(published);
            content.setNotPublished(!published);
        }

        return moduleRepository.save(module); // cascade sẽ lưu cả content nếu có @OneToMany(cascade = ...)
    }
    public void ensureInstructorOwnsCourse(int courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User user = userRepository.findByUsername(username);
        if (user == null || !course.getInstructor().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn không phải giảng viên của khóa học này.");
        }
    }
    public void ensureInstructorOwnsModule(int moduleId, String username) {
        Module module = getModuleById(moduleId);
        if (!module.getCourse().getInstructor().getUsername().equals(username)) {
            throw new RuntimeException("Bạn không phải giảng viên của module này.");
        }
    }
}
