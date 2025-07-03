package org.example.lmsbackend.controller;

import org.example.lmsbackend.dto.ModulesDTO;
import org.example.lmsbackend.dto.ContentResponseDTO;
import org.example.lmsbackend.dto.ModuleResponseDTO;
import org.example.lmsbackend.model.Module;
import org.example.lmsbackend.model.Content;
import org.example.lmsbackend.service.ContentService;
import org.example.lmsbackend.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModulesRestController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ContentService contentService;

    // ✅ Tạo module mới
    @PostMapping
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<?> createModule(@RequestBody ModulesDTO dto, Principal principal) {
        Module module = moduleService.createModule(principal.getName(), dto);

        ModuleResponseDTO response = new ModuleResponseDTO();
        response.setModuleId(module.getId());
        response.setTitle(module.getTitle());
        response.setDescription(module.getDescription());
        response.setOrderNumber(module.getOrderNumber());
        response.setCourseId(module.getCourse().getCourseId());
        response.setCourseTitle(module.getCourse().getTitle());
        response.setPublished(module.isPublished());

        return ResponseEntity.ok(response);
    }

    // ✅ Upload tài liệu cho module → sử dụng Content
    @PostMapping("/{moduleId}/documents")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<?> upload(@PathVariable int moduleId,
                                    @RequestParam("file") MultipartFile file,
                                    Principal principal) {
        try {
            contentService.uploadDocument(principal.getName(), moduleId, file);
            return ResponseEntity.ok("Upload successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ✅ Lấy danh sách module theo courseId
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<List<ModuleResponseDTO>> getModulesByCourseId(@PathVariable int courseId,
                                                                        Principal principal) {
        moduleService.ensureInstructorOwnsCourse(courseId, principal.getName());

        List<Module> modules = moduleService.getModulesByCourseId(courseId);
        List<ModuleResponseDTO> dtos = modules.stream().map(module -> {
            ModuleResponseDTO dto = new ModuleResponseDTO();
            dto.setModuleId(module.getId());
            dto.setTitle(module.getTitle());
            dto.setDescription(module.getDescription());
            dto.setOrderNumber(module.getOrderNumber());
            dto.setPublished(module.isPublished());
            dto.setCourseId(module.getCourse().getCourseId());
            dto.setCourseTitle(module.getCourse().getTitle());
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    // ✅ Cập nhật trạng thái module
    @PutMapping("/{moduleId}/status")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<?> updateStatus(@PathVariable int moduleId,
                                          @RequestParam boolean published,
                                          Principal principal) {
        Module updated = moduleService.updateModuleStatus(moduleId, published, principal.getName());
        return ResponseEntity.ok("Cập nhật trạng thái thành công: " + (published ? "Published" : "Not Published"));
    }

    // ✅ Cập nhật trạng thái content
    @PutMapping("/contents/{contentId}/status")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<?> updateContentStatus(@PathVariable int contentId,
                                                 @RequestParam boolean published,
                                                 Principal principal) {
        try {
            Content updated = contentService.updateContentStatus(contentId, published, principal.getName());
            return ResponseEntity.ok("Cập nhật trạng thái tài liệu thành công: " + (published ? "Published" : "Not Published"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // ✅ Lấy danh sách content theo module
    @GetMapping("/{moduleId}/contents")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<List<ContentResponseDTO>> getContentsByModule(@PathVariable int moduleId,
                                                                        Principal principal) {
        moduleService.ensureInstructorOwnsModule(moduleId, principal.getName());
        List<Content> contents = contentService.getContentsByModuleId(moduleId);

        List<ContentResponseDTO> response = contents.stream().map(content -> {
            ContentResponseDTO dto = new ContentResponseDTO();
            dto.setContentId(content.getId());
            dto.setModuleId(content.getModule().getId());
            dto.setTitle(content.getTitle());
            dto.setType(content.getType());
            dto.setContentUrl(content.getContentUrl());
            dto.setFileName(content.getFileName());
            dto.setDuration(content.getDuration());
            dto.setOrderNumber(content.getOrderNumber());
            dto.setPublished(content.isPublished());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    // ✅ Lấy danh sách content theo course
    @GetMapping("/course/{courseId}/contents")
    @PreAuthorize("hasRole('instructor')")
    public ResponseEntity<List<ContentResponseDTO>> getContentsByCourse(@PathVariable int courseId,
                                                                        Principal principal) {
        moduleService.ensureInstructorOwnsCourse(courseId, principal.getName());
        List<Content> contents = contentService.getContentsByCourseId(courseId);

        List<ContentResponseDTO> response = contents.stream().map(content -> {
            ContentResponseDTO dto = new ContentResponseDTO();
            dto.setContentId(content.getId());
            dto.setModuleId(content.getModule().getId());
            dto.setTitle(content.getTitle());
            dto.setType(content.getType());
            dto.setContentUrl(content.getContentUrl());
            dto.setFileName(content.getFileName());
            dto.setDuration(content.getDuration());
            dto.setOrderNumber(content.getOrderNumber());
            dto.setPublished(content.isPublished());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}
