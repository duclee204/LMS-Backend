package org.example.lmsbackend.service;

import org.example.lmsbackend.dto.VideoDTO;
import org.example.lmsbackend.dto.VideoMapperUtil;
import org.example.lmsbackend.model.Video;
import org.example.lmsbackend.repository.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.example.lmsbackend.dto.VideoMapperUtil.toDTO;

@Service
public class VideoService {

    private  VideoMapper videoMapper;

    @Autowired
    public VideoService(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public int createVideo(Video video) {
        return videoMapper.insertVideo(video);
    }

    public List<VideoDTO> getAllVideos(String title) {
        List<Video> videos = videoMapper.findVideos(title);
        System.out.println("VIDEOS from DB: " + videos);
        return videos.stream()
                .map(VideoMapperUtil::toDTO)
                .collect(Collectors.toList());
    }

    public VideoDTO getVideoById(Long id) {
        Video video = videoMapper.findById(id);
        return toDTO(video);
    }


    public int updateVideo(Video video) {
        return videoMapper.updateVideo(video);
    }

    public int deleteVideo(Long id) {
        return videoMapper.deleteVideo(id);
    }

    public String saveFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return "/videos/" + fileName; // trả về URL để frontend phát video
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
