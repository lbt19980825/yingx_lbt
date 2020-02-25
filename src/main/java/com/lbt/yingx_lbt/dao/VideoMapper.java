package com.lbt.yingx_lbt.dao;


import com.lbt.yingx_lbt.entity.Video;
import com.lbt.yingx_lbt.po.VideoLikePo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {
    //查询所有视频
    List<VideoLikePo> queryByReleaseTime();
}