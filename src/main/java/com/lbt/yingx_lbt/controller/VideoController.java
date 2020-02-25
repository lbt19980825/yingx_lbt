package com.lbt.yingx_lbt.controller;

import com.lbt.yingx_lbt.annotation.LogRecords;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.User;
import com.lbt.yingx_lbt.entity.Video;
import com.lbt.yingx_lbt.service.VideoService;
import com.lbt.yingx_lbt.util.AliyunUtils;
import com.lbt.yingx_lbt.util.InterceptVideoPhotoUtil;
import com.lbt.yingx_lbt.util.QiNiuYunUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/video")
public class VideoController {
    @Resource
    private VideoService videoService;

    @RequestMapping("/showAll")
    @ResponseBody
    public HashMap<String,Object> showAll(Integer page,Integer rows){
        HashMap<String, Object> map = new HashMap<>();
        PageBean pageBean = new PageBean();
        pageBean.setPageRow(rows);//每页显示的行数
        pageBean.setPageNo(page);//设置当前页数
        List<Video> videos = videoService.showAllVideo(pageBean);
        //将要返回给前台的数据放在map集合中
        //total:总页数   records：总条数   page：当前页   rows：数据
        map.put("total",pageBean.getTotalPage());
        map.put("records",pageBean.getTotalRow());
        map.put("page", pageBean.getPageNo());
        map.put("rows",videos);
        return map;
    }

    @LogRecords(value="修改或删除视频")
    @RequestMapping("/edit")
    @ResponseBody
    public String edit(Video video, String oper){
        String s = null;
        if("add".equals(oper)){
            s = videoService.insertVideo(video);
        }
        if("edit".equals(oper)){

        }
        if("del".equals(oper)){
            videoService.deleteVideoById(video.getId());
        }
        return s;
    }

    //上传视频
    @LogRecords(value="上传视频")
    @RequestMapping("/uploadVideo")
    public void uploadVideo(MultipartFile cover, String id, HttpSession session){
        HashMap<String, String> map = videoService.uploadVideo(cover, id);
        String name = map.get("name");
        String realCoverName = map.get("realCoverName");
        videoService.updateCoverAndPathById(id,name,realCoverName);//调用业务层方法，修改视频的路径名和封面名
    }

}
