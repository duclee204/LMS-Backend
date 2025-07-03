package org.example.lmsbackend.service;

import org.example.lmsbackend.model.Content;
import org.example.lmsbackend.model.Module;
import org.example.lmsbackend.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ModuleService moduleService;

    // ✅ Lấy content theo moduleId
    public List<Content> getContentsByModuleId(int moduleId) {
        return contentRepository.findByModuleId(moduleId);
    }

    // ✅ Lấy content theo courseId
    public List<Content> getContentsByCourseId(int courseId) {
        return contentRepository.findAllByModule_Course_CourseId(courseId);
    }

    // ✅ Upload tài liệu
    public void uploadDocument(String username, int moduleId, MultipartFile file) throws IOException {
        Module module = moduleService.getModuleById(moduleId);

        if (!module.getCourse().getInstructor().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to upload to this module");
        }

        // Tạo thư mục lưu file
        String uploadDir = "uploads/modules/" + moduleId;
        Files.createDirectories(Paths.get(uploadDir));

        // Lưu file
        Path path = Paths.get(uploadDir, file.getOriginalFilename());
        file.transferTo(path);

        // Tạo Content
        Content content = new Content();
        content.setTitle(file.getOriginalFilename());
        content.setType("document");
        content.setFileName(file.getOriginalFilename());
        content.setContentUrl("/" + path.toString().replace("\\", "/")); // chuẩn hóa URL
        content.setModule(module);
        content.setOrderNumber(getNextOrderNumber(moduleId));
        content.setDuration(null);
        content.setPublished(module.isPublished()); // nội dung thừa hưởng trạng thái của module
        content.setNotPublished(!module.isPublished());

        contentRepository.save(content);
    }

    // ✅ Cập nhật trạng thái của content
    public Content updateContentStatus(int contentId, boolean published, String username) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        // Kiểm tra quyền instructor
        if (!content.getModule().getCourse().getInstructor().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to update this content");
        }

        content.setPublished(published);
        content.setNotPublished(!published);
        return contentRepository.save(content);
    }

    // ✅ Hàm hỗ trợ: lấy orderNumber tiếp theo cho content trong module
    private int getNextOrderNumber(int moduleId) {
        List<Content> contents = contentRepository.findByModuleId(moduleId);
        return contents.isEmpty() ? 1 :
                contents.stream().mapToInt(Content::getOrderNumber).max().orElse(0) + 1;
    }

    // ✅ Đồng bộ trạng thái của tất cả content trong module (dùng khi cập nhật module)
    public void syncContentStatusByModule(Module module) {
        List<Content> contents = contentRepository.findByModuleId(module.getId());
        boolean isPublished = module.isPublished();

        for (Content content : contents) {
            content.setPublished(isPublished);
            content.setNotPublished(!isPublished);
        }
        contentRepository.saveAll(contents);
    }
}
