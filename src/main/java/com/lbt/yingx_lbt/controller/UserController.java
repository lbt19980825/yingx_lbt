package com.lbt.yingx_lbt.controller;


import com.lbt.yingx_lbt.annotation.LogRecords;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.User;
import com.lbt.yingx_lbt.po.Models;
import com.lbt.yingx_lbt.service.UserService;;
import com.lbt.yingx_lbt.util.QiNiuYunUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    //展示所有数据
    @RequestMapping("/showAll")
    @ResponseBody                         //当前页      //每页现实的行数    //后面三个参数用来进行模糊查询，searchString：输入的查询内容
                                                                            //searchField:根据什么查（用户名，id等等）searchOper：查询的条件：大于小于等于等等
    public HashMap<String,Object>  showAll(Integer page,Integer rows,String searchString,String searchField ,String searchOper){
        List<User> users = null;
        HashMap<String, Object> map = new HashMap<>();
        PageBean pageBean = new PageBean();
        pageBean.setPageRow(rows);//每页显示的行数
        pageBean.setPageNo(page);//设置当前页数
        //只要searchField或者searchOper参数有一个不为空就说明用户进行了模糊查询,反之则为查所有
        //当searchString输入的内容为空时，查所有
        if(searchField!=null||searchOper!=null){
                //对于各种查询条件在业务层进行处理
                users = userService.queryUserByTerm(searchString, searchField, searchOper);

        }else{
            users = userService.queryAllUser(pageBean);
        }
        //将要返回给前台的数据放在map集合中
        //total:总页数   records：总条数   page：当前页   rows：数据
        map.put("total",pageBean.getTotalPage());
        map.put("records",pageBean.getTotalRow());
        map.put("page", pageBean.getPageNo());
        map.put("rows",users);
        return map;
    }
    //对表进行增删改操作
    @LogRecords(value="添加或删除用户")
    @RequestMapping("/edit")
    @ResponseBody
    public String edit(User user,String oper){
        String s = null;
        if("add".equals(oper)){
           s = userService.insertUser(user);
        }
        if("edit".equals(oper)){

        }
        if("del".equals(oper)){
            userService.deleteUserById(user.getId());
        }
        return s;//返回的是该用户的id
    }

    //添加用户时的图片上传
    @LogRecords(value="上传图片")
    @RequestMapping("/uploadUserPicImg")
    public void uploadUserPicImg(MultipartFile picImg,String id){
        String bucket = "yingx-pic";
        String dns = "http://q5um6y4cu.bkt.clouddn.com";
        String name = QiNiuYunUtil.Upload(picImg,bucket,dns);
        //修改用户信息的图片名
        User user = new User();
        user.setPicImg(name);
        user.setId(id);
        //调用修改方法（字段为空不修改）
        userService.updatePicImgById(user);
    }

    //修改用户状态
    @LogRecords(value="修改用户状态")
    @RequestMapping("/updateStatus")
    @ResponseBody
    public void updateStatus(User user){
        userService.updateStatusById(user);
    }

    //使用EasyPOI导出用户数据
    @RequestMapping("/exportUser")
    @ResponseBody
    public String exportUser(String path){
        String s = userService.ExportUserByEasyPOI(path);
        return s;
    }
    //使用EasyPOI导入用户数据
    @RequestMapping("/importUser")
    @ResponseBody
    public String importUser(String path){
        String s = userService.ImportUserByEasyPOI(path);
        return s;
    }

    //查询各个月份男性和女性的注册人数
    @RequestMapping("/userStatistics")
    @ResponseBody
    public HashMap<String,List<String>> userStatistics(){
        HashMap<String, List<String>> stringListHashMap = userService.selectManOrWomanCount();
        return stringListHashMap;
    }
    //查询每个城市男性和女性的人数
    @RequestMapping("/userDistribution")
    @ResponseBody
    public List<Models> userDistribution(){
        ArrayList<Models> models = userService.selectManOrWomanByCity();
        return models;
    }
}
