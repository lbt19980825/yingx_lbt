package com.lbt.yingx_lbt.appinterface;

import com.lbt.yingx_lbt.common.CommonResult;
import com.lbt.yingx_lbt.controller.VideoController;
import com.lbt.yingx_lbt.dao.CategoryMapper;
import com.lbt.yingx_lbt.dao.LikeMapper;
import com.lbt.yingx_lbt.dao.UserMapper;
import com.lbt.yingx_lbt.entity.*;
import com.lbt.yingx_lbt.po.CategorySelfPo;
import com.lbt.yingx_lbt.po.VideoLikePo;
import com.lbt.yingx_lbt.service.CategoryService;
import com.lbt.yingx_lbt.service.VideoService;
import com.lbt.yingx_lbt.util.AliyunUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppInterfaceController {
    @Resource
    private VideoService videoService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private VideoController videoController;
    @Resource
    private LikeMapper likeMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private UserMapper userMapper;

    //发送验证码
    @RequestMapping("/getPhoneCode")
    public CommonResult getPhoneCode(String phone){
        try {
            //获取6位随机数作为验证码
            String random = AliyunUtils.getRandom(6);
            //发送验证码
            AliyunUtils.sendMessage(random,phone);
            return new CommonResult().success("100","验证码发送成功",phone);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().fail("104","验证码发送失败");
        }
    }

    //首页展示视频
    @RequestMapping("/queryByReleaseTime")
    public CommonResult queryByReleaseTime(){
        try {
            //调用service，根据上传时间查询所有视频
            List<VideoLikePo> videoLikePos = videoService.queryByReleaseTime();
            return new CommonResult().success("100","查询成功",videoLikePos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().fail("104","查询失败");
        }
    }
    //分类展示类别
    @RequestMapping("/queryAllCategory")
    public CommonResult queryAllCategory(){
        try {
            List<CategorySelfPo> categorySelfPos = categoryService.queryCateVideoList();
            return new CommonResult().success("100","查询成功",categorySelfPos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().fail("104","查询失败");
        }
    }

    //发布视频     未调试
    /*
    description 视频简介 string 是  
    videoFile 视频文件 MultipartFile 是  
    videoTitle 视频标题 string 是  
    categoryId 二级分类id string 是 uuid
    userId 用户id String 是 uuid
     */
    @RequestMapping("/save")
    public CommonResult save(String description, MultipartFile videoFile,String videoTitle,String categoryId,String userId){
        try {
            Video video = new Video(null, videoTitle, description, null, null, null, userId, categoryId, null);
            String id = videoService.insertVideo(video);
            videoService.uploadVideo(videoFile,id);
            return new CommonResult().success("100","添加成功",null);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().fail("104","添加失败");
        }
    }

    //查询二级类别分类视频接口
    /*
    参数名     参数说明    类型      是否必填    示例值          描述
    cateId      类别id    String      是           16          二级类别id
     */
    @RequestMapping("/queryCateVideoList")
    public CommonResult queryCateVideoList(String cateId){
        try {
            List<VideoLikePo> videoLikePos = new ArrayList<VideoLikePo>();
            List<Video> videos = videoService.CateVideoList(cateId);
            for (Video video : videos) {
                VideoLikePo videoLikePo = new VideoLikePo();
                //查询每个视频点赞数
                LikeExample likeExample = new LikeExample();
                likeExample.createCriteria().andVideoPhotoIdEqualTo(video.getId());
                int i = likeMapper.selectCountByExample(likeExample);
                //查询每个视频的类别名（根据类别id）
                CategoryExample categoryExample = new CategoryExample();
                categoryExample.createCriteria().andIdEqualTo(video.getCategoryId());
                Category category = categoryMapper.selectOneByExample(categoryExample);
                System.out.println(category);
                //查询每个视频的所属用户(根据用户id)
                UserExample userExample = new UserExample();
                userExample.createCriteria().andIdEqualTo(video.getUserId());
                User user = userMapper.selectOneByExample(userExample);
                //因为前后端视频字段不同，所以把值赋予给videoLikePo
                videoLikePo.setId(video.getId());
                videoLikePo.setVideoTitle(video.getTitle());
                videoLikePo.setCover(video.getCover());
                videoLikePo.setUploadTime(video.getUploadTime());
                videoLikePo.setDescription(video.getBrief());
                videoLikePo.setLikeCount(i);
                videoLikePo.setCateName(category.getName());
                videoLikePo.setCategoryId(video.getCategoryId());
                videoLikePo.setUserId(video.getUserId());
                videoLikePo.setUserName(user.getUsername());
                videoLikePos.add(videoLikePo);
            }
            return new CommonResult().success("100","查询成功",videoLikePos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().fail("104","查询失败");
        }
    }

    //播放视频接口
    /*
    参数名     参数说明    类型      是否必填    示例值     描述
    videoId     视频id    string      是           1       视频id
    cateId      类别id    string      是           16  当前视频所属类别id
    userId      用户id    string      是           1   当前登录用户id
     */
   /* @RequestMapping("/queryByVideoDetail")
    public CommonResult queryByVideoDetail(String videoId,String cateId,String userId){

    }*/
}
