package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.User;
import com.lbt.yingx_lbt.po.Models;
import com.lbt.yingx_lbt.po.MonthAndSex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {
    //展示所有用户
    List<User> queryAllUser(PageBean pageBean);
    //添加用户
    String insertUser(User user);
    //根据id修改用户图片名
    void updatePicImgById(User user);
    //根据id修改用户状态
    void updateStatusById(User user);
    //根据id查找用户,得到其路径，在删除用户的同时将云存储的图片删除
    void deleteUserById(String id);
    //进行各种条件的模糊查询
    List<User> queryUserByTerm(String searchString,String searchField ,String searchOper);
    //查出所有用户，并使用EasyPOI导出
    String  ExportUserByEasyPOI(String path);
    //使用EasyPOI将用户导入
    String  ImportUserByEasyPOI(String path);
    //查询各个月份男性和女性的注册人数
    HashMap<String,List<String>> selectManOrWomanCount();
    //查询每个城市男性和女性的注册人数
    ArrayList<Models> selectManOrWomanByCity();
}
