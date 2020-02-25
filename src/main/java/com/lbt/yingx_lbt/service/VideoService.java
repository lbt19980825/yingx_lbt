package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.Video;
import com.lbt.yingx_lbt.po.VideoLikePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface VideoService {
    //查所有视频
    List<Video> showAllVideo(PageBean pageBean);
    //添加视频
    String insertVideo(Video video);
    //根据id修改封面和路径
    void updateCoverAndPathById(@Param("id") String id, @Param("name") String name,@Param("coverName")String coverName);
    //根据id删除视频（删除前要先查询到该视频的路径）
    Video deleteVideoById(String id);
    //app接口调用，供首页显示
    List<VideoLikePo> queryByReleaseTime();
    //上传视频并将其截取第一帧作为封面
    HashMap<String,String> uploadVideo(MultipartFile cover, String id);
    //app接口调用,根据类别查询该类别下所有视频
    List<Video> CateVideoList(String cateId);


}
