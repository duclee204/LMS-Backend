package org.example.lmsbackend.dto;

import org.example.lmsbackend.model.Video;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VideoDTO {
    private Long videoId;
    private String title;
    private String description;
    private String fileUrl;
    private Integer duration;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadedAt;

    // Constructors
    public VideoDTO() {}

    public VideoDTO(Long videoId, String title, String description, String fileUrl,
                    Integer duration, Long fileSize, String mimeType, LocalDateTime uploadedAt) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.fileUrl = fileUrl;
        this.duration = duration;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.uploadedAt = uploadedAt;
    }

    // Getters and Setters
    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }


}

