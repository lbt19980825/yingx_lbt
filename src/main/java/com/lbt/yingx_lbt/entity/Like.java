package com.lbt.yingx_lbt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="yx_like")
public class Like {
    @Id
    private String id;

    @Column(name="user_id")
    private String userId;

    @Column(name="like_time")
    private Date likeTime;

    @Column(name="video_photo_id")
    private String videoPhotoId;


}