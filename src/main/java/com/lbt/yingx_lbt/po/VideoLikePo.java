package com.lbt.yingx_lbt.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class VideoLikePo implements Serializable {
    private String id;
    private String videoTitle;
    private String cover;
    private String path;
    private Date uploadTime;
    private String description;
    private Integer likeCount;
    private String cateName;
    private String userPhoto;
    private String categoryId;
    private String userId;
    private String userName;

}
