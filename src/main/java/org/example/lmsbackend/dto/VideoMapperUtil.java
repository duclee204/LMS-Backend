package org.example.lmsbackend.dto;

import org.example.lmsbackend.model.Video;

public class VideoMapperUtil {
    public static VideoDTO toDTO(Video video) {
        if (video == null) return null;

        VideoDTO dto = new VideoDTO();
        dto.setVideoId(video.getVideoId());
        dto.setTitle(video.getTitle());
        dto.setDescription(video.getDescription());
        dto.setFileUrl(video.getFileUrl());
        dto.setDuration(video.getDuration());
        dto.setFileSize(video.getFileSize());
        dto.setMimeType(video.getMimeType());
        dto.setUploadedAt(video.getUploadedAt());
        return dto;
    }


}
