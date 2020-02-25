package com.lbt.yingx_lbt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="yx_video")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    private String id;

    private String title;

    private String brief;

    private String cover;

    private String path;

    @Column(name="upload_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;

    @Column(name="user_id")
    private String userId;

    @Column(name="category_id")
    private String categoryId;

    @Column(name="group_id")
    private String groupId;


}