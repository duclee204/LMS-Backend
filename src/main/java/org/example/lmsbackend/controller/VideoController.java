package org.example.lmsbackend.controller;

import org.example.lmsbackend.dto.VideoDTO;
import org.example.lmsbackend.model.Video;
import org.example.lmsbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public ResponseEntity<List<VideoDTO>> getAllVideos(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(videoService.getAllVideos(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable Long id) {
        VideoDTO video = videoService.getVideoById(id);
        if (video == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(video);
    }

    @PostMapping
    public ResponseEntity<String> createVideo(@RequestBody Video video) {
        int inserted = videoService.createVideo(video);
        return inserted > 0 ? ResponseEntity.ok("Video created successfully")
                : ResponseEntity.badRequest().body("Failed to create video");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateVideo(@PathVariable Long id, @RequestBody Video video) {
        video.setVideoId(id);
        int updated = videoService.updateVideo(video);
        return updated > 0 ? ResponseEntity.ok("Video updated successfully")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id) {
        int deleted = videoService.deleteVideo(id);
        return deleted > 0 ? ResponseEntity.ok("Video deleted successfully")
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        System.out.println("File received: " + file.getOriginalFilename());
        String fileUrl = videoService.saveFile(file); // ✅ Lưu file vào ổ đĩa
        if (fileUrl == null) {
            return ResponseEntity.internalServerError().body("Failed to save file");
        }

        // Bước 1: Lưu file vào thư mục cục bộ
        String uploadDir = "uploads/videos/";
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        try {
            // Tạo thư mục nếu chưa có
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Ghi file vào ổ đĩa
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, file.getBytes());

            // Bước 2: Lưu thông tin video vào database
            Video video = new Video();
            video.setTitle(title);
            video.setDescription(description);
            video.setFileUrl(fileUrl); // đường dẫn đã trả về
            video.setMimeType(file.getContentType());
            video.setFileSize(file.getSize());
            video.setDuration(null); // nếu bạn chưa xử lý

            int inserted = videoService.createVideo(video);
            if (inserted > 0) {
                return ResponseEntity.ok("Video uploaded successfully");
            } else {
                return ResponseEntity.badRequest().body("Saved file but failed to insert video info");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to upload video: " + e.getMessage());
        }

    }


}