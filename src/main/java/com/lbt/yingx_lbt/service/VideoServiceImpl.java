package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.annotation.AddCache;
import com.lbt.yingx_lbt.annotation.DelCache;
import com.lbt.yingx_lbt.dao.LikeMapper;
import com.lbt.yingx_lbt.dao.LogMapper;
import com.lbt.yingx_lbt.dao.VideoMapper;
import com.lbt.yingx_lbt.entity.LikeExample;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.Video;
import com.lbt.yingx_lbt.entity.VideoExample;
import com.lbt.yingx_lbt.po.VideoLikePo;
import com.lbt.yingx_lbt.util.AliyunUtils;
import com.lbt.yingx_lbt.util.InterceptVideoPhotoUtil;
import com.lbt.yingx_lbt.util.QiNiuYunUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;
    @Resource
    private LikeMapper likeMapper;
    @Resource
    HttpSession session;

    //查所有视频
    @AddCache
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Video> showAllVideo(PageBean pageBean) {
        //查询总条数作为总行数
        Video video = new Video();
        int i = videoMapper.selectCount(video);
        pageBean.setTotalRow(i);
        //分页查询
        VideoExample videoExample = new VideoExample();
        RowBounds rowBounds = new RowBounds(pageBean.getIngore(),pageBean.getPageRow());
        List<Video> videos= videoMapper.selectByExampleAndRowBounds(videoExample, rowBounds);
        return videos;
    }

    //添加视频
    @DelCache
    @Override
    public String insertVideo(Video video) {
        //补全字段
        String s = UUID.randomUUID().toString();
        video.setId(s);//id
        video.setUploadTime(new Date());//上传时间
        //添加
        videoMapper.insert(video);
        return s;
    }

    //根据id修改封面和路径
    @DelCache
    @Override
    public void updateCoverAndPathById(String id,String name,String realCoverName) {
        //修改视频中封面和路径字段
        Video video = new Video();
        video.setId(id);
        video.setCover(realCoverName);
        video.setPath(name);
        videoMapper.updateByPrimaryKeySelective(video);
    }

    //根据id删除视频和封面（删除前要先查询到该视频的路径）,
    @DelCache
    @Override
    public Video deleteVideoById(String id) {
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andIdEqualTo(id);
        Video video = videoMapper.selectOneByExample(videoExample);
        String path = video.getPath();
        String cover = video.getCover();
        String videoKey = path.substring(33);//从图片路径索引位置33未开始为文件名字
        String coverKey = cover.substring(33);//从图片路径索引位置33未开始为文件名字
        String bucket = "yingx-video1";
        QiNiuYunUtil.delete(videoKey,bucket);
        QiNiuYunUtil.delete(coverKey,bucket);
        videoMapper.delete(video);
        return video;
    }

    //接口调用，供首页显示
    @AddCache
    @Override
    public List<VideoLikePo> queryByReleaseTime() {
        List<VideoLikePo> videoLikePos = videoMapper.queryByReleaseTime();
        //根据视频ID查出点赞数
        for (VideoLikePo videoLikePo : videoLikePos) {
            LikeExample likeExample = new LikeExample();
            likeExample.createCriteria().andVideoPhotoIdEqualTo(videoLikePo.getId());
            int i = likeMapper.selectCountByExample(likeExample);
            //设置点赞数量
            videoLikePo.setLikeCount(i);
        }
        return videoLikePos;
    }

    //上传视频并将其截取第一帧作为封面
    @DelCache
    @Override
    public HashMap<String, String> uploadVideo(MultipartFile cover, String id) {
        HashMap<String, String> map = new HashMap<>();
        String bucket = "yingx-video1";
        String dns = "http://q5uaig5zl.bkt.clouddn.com";
        String name = QiNiuYunUtil.Upload(cover,bucket,dns);//调用工具类将文件上传到七牛云
        //远程截取一个视频的第一帧（后期作为封面）
        //1、将截取图片的放在tomcat上
        //1.1根据绝对路径获取相对路径
        String realPath = session.getServletContext().getRealPath("/upload/photo");
        //1.2判断文件夹是否存在，不存在则创建文件夹
        File file = new File(realPath);
        if(!file.exists()){
            file.mkdir();
        }
        //2.截取视频
        //2.1拼接图片名字和图片路径,防止相同文件同名，设置一个随机数拼接
        String random = AliyunUtils.getRandom(12);
        String coverName = random+cover.getOriginalFilename()+".jpg";
        String coverPath = realPath+"\\"+coverName;
        //2.2截取
        try {                                //原视频路径，截取后的图片路径
            InterceptVideoPhotoUtil.fetchFrame(name,coverPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3.将截取的图片放到七牛云
        String tempName = QiNiuYunUtil.Upload(coverName, coverPath, bucket);
        //拼接真正访问远程七牛云的图片路径
        String realCoverName = dns+"\\"+tempName;
        map.put("name",name);
        map.put("realCoverName",realCoverName);
        return map;
    }

    //app接口调用,根据类别查询该类别下所有视频
    @AddCache
    @Override
    public List<Video> CateVideoList(String cateId) {
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andCategoryIdEqualTo(cateId);
        List<Video> videos = videoMapper.selectByExample(videoExample);
        return videos;
    }


}
