package org.example.lmsbackend.repository;

import org.example.lmsbackend.model.Video;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface VideoMapper {

    @Insert("""
        INSERT INTO videos (title, description, file_url, duration, file_size, mime_type, uploaded_at)
        VALUES (#{title}, #{description}, #{fileUrl}, #{duration}, #{fileSize}, #{mimeType}, NOW())
    """)
    @Options(useGeneratedKeys = true, keyProperty = "videoId")
    int insertVideo(Video video);

    @Select("""
    SELECT 
        video_id, title, description, file_url, duration, file_size, mime_type, uploaded_at
    FROM videos
    WHERE (#{title} IS NULL OR title LIKE CONCAT('%', #{title}, '%'))
""")
    @Results({
            @Result(property = "videoId", column = "video_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "description", column = "description"),
            @Result(property = "fileUrl", column = "file_url"),
            @Result(property = "duration", column = "duration"),
            @Result(property = "fileSize", column = "file_size"),
            @Result(property = "mimeType", column = "mime_type"),
            @Result(property = "uploadedAt", column = "uploaded_at"),
    })
    List<Video> findVideos(@Param("title") String title);


    @Select("SELECT * FROM videos WHERE video_id = #{videoId}")
    Video findById(@Param("videoId") Long videoId);

    @Update("""
        UPDATE videos
        SET 
            title = #{title},
            description = #{description},
            file_url = #{fileUrl},
            duration = #{duration},
            file_size = #{fileSize},
            mime_type = #{mimeType},
            uploaded_at = #{uploadedAt}
        WHERE video_id = #{videoId}
    """)
    int updateVideo(Video video);

    @Delete("DELETE FROM videos WHERE video_id = #{videoId}")
    int deleteVideo(@Param("videoId") Long videoId);


}
