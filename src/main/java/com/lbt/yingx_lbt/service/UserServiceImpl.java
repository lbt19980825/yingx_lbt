package com.lbt.yingx_lbt.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.lbt.yingx_lbt.dao.UserMapper;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.User;
import com.lbt.yingx_lbt.entity.UserExample;
import com.lbt.yingx_lbt.po.City;
import com.lbt.yingx_lbt.po.Models;
import com.lbt.yingx_lbt.po.MonthAndSex;
import com.lbt.yingx_lbt.util.QiNiuYunUtil;
import io.goeasy.GoEasy;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    //展示所有用户
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<User> queryAllUser(PageBean pageBean) {
        int i = userMapper.selectCount(new User());//要显示的总行数
        pageBean.setTotalRow(i);//设置总行数
        UserExample userExample = new UserExample();
        RowBounds rowBounds = new RowBounds(pageBean.getIngore(),pageBean.getPageRow());//忽略几条，获取几条
        List<User> users = userMapper.selectByExampleAndRowBounds(userExample, rowBounds);//分页查询
        return users;
    }

    //添加用户
    @Override
    public String insertUser(User user) {
        String s = UUID.randomUUID().toString();//生成UUID
        user.setId(s);//设置id
        user.setCreateDate(new Date());//设置创建时间
        user.setScore(0.0);//设置分数
        user.setStatus("激活");//设置状态
        userMapper.insert(user);//将数据插入数据库
        /*
        一旦添加了用户，则会触发GoEasy，自动刷新用户统计页面
         */
        //调用UserService,查询到用户统计需要的数据
        HashMap<String, List<String>> map = userService.selectManOrWomanCount();
        //将对象转为json格式字符串
        String content = JSON.toJSONString(map);

        //配置GoEasy参数：redionHost:应用的地址,Appkey：你的appKey
        GoEasy goEasy = new GoEasy( "https://rest-hangzhou.goeasy.io/publish", "BC-b2ef222836d14a99b39876c04a64fa66");
        //发送消息
        goEasy.publish("刘宝腾 ", content);
        return s;//返回一个id供上传照片时修改用户路径使用
    }

    //根据id修改用户图片名
    @Override
    public void updatePicImgById(User user) {
        userMapper.updateByPrimaryKeySelective(user);//根据主键修改
    }

    //根据id修改用户状态
    @Override
    public void updateStatusById(User user) {
        if("激活".equals(user.getStatus())){
            user.setStatus("冻结");
            userMapper.updateByPrimaryKeySelective(user);
        }else{
            user.setStatus("激活");
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    //根据id查找用户,得到其路径，在删除用户的同时将云存储的图片删除
    @Override
    public void deleteUserById(String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(id);
        User user = userMapper.selectOneByExample(userExample);
        String path = user.getPicImg();
        String key = path.substring(33);//从图片路径索引位置33未开始为文件名字
        String bucket = "yingx-pic";
        QiNiuYunUtil.delete(key,bucket);
        userMapper.deleteByPrimaryKey(id);
    }

    //进行各种条件的模糊查询   eq:等于   cn：包含
    @Override
    public List<User> queryUserByTerm(String searchString, String searchField, String searchOper) {
        List<User> users = null;
        UserExample userExample = new UserExample();
        if("id".equals(searchField)){   //按id搜索
            if("eq".equals(searchOper)){
                userExample.createCriteria().andIdEqualTo(searchString);
                users = userMapper.selectByExample(userExample);
            }
            if("cn".equals(searchOper)){
                userExample.createCriteria().andIdLike("%"+searchString+"%");
                users= userMapper.selectByExample(userExample);
            }
        }
        if("username".equals(searchField)){//按用户名搜索
            if("eq".equals(searchOper)){
                userExample.createCriteria().andUsernameEqualTo(searchString);
                users = userMapper.selectByExample(userExample);
            }
            if("cn".equals(searchOper)){
                userExample.createCriteria().andUsernameLike("%"+searchString+"%");
                users = userMapper.selectByExample(userExample);
            }
        }
        if("phone".equals(searchField)){ //按手机号搜索
            if("eq".equals(searchOper)){
                userExample.createCriteria().andPhoneEqualTo(searchString);
                users = userMapper.selectByExample(userExample);
            }
            if("cn".equals(searchOper)){
                userExample.createCriteria().andPhoneLike("%"+searchString+"%");
                users = userMapper.selectByExample(userExample);
            }

        }
        if("wechat".equals(searchField)){//按微信搜索
            if("eq".equals(searchOper)){
                userExample.createCriteria().andWechatEqualTo(searchString);
                users = userMapper.selectByExample(userExample);
            }
            if("cn".equals(searchOper)){
                userExample.createCriteria().andWechatLike("%"+searchString+"%");
                users = userMapper.selectByExample(userExample);
            }
        }
        return users;
    }

    //查出所有用户，并使用EasyPOI导出
    @Override
    public String ExportUserByEasyPOI(String path) {
        if(!path.contains(".xls")){
            return "导入失败";
        }
        List<User> users = userMapper.selectAll();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户表", "用户信息"), User.class, users);
        try {
            workbook.write(new FileOutputStream(new File(path)));
            workbook.close();
            return "导入成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "导入失败";
        }
    }

    //使用EasyPOI将用户导入
    @Override
    public String ImportUserByEasyPOI(String path) {
        if(!path.contains(".xls")){
            return "导入失败";
        }
        //配置导入参数
        ImportParams params = new ImportParams();
        params.setTitleRows(1);  //标题占几行 默认0
        params.setHeadRows(1);  //表头占几行  默认1
        try {
            //获取导入的文件
            FileInputStream inputStream = new FileInputStream(new File(path));
            //导入    参数：读入流,实体类映射
            List<User> userList = ExcelImportUtil.importExcel(inputStream,User.class, params);
            //遍历
            for (User user : userList) {
                user.setId(UUID.randomUUID().toString());
                user.setCreateDate(new Date());
                userMapper.insert(user);
            }
            return "导入成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "导入失败";
        }
    }

    //查询各个月份男性和女性的注册人数
    @Override
    public HashMap<String,List<String>> selectManOrWomanCount() {
        HashMap<String, List<String>> map = new HashMap<>();
        ArrayList<String> month = new ArrayList<>();
        ArrayList<String> boys = new ArrayList<>();
        ArrayList<String> womans = new ArrayList<>();
        List<MonthAndSex> monthAndSexes = userMapper.selectManCountByCreateDate();
        for (MonthAndSex monthAndSex : monthAndSexes) {
            month.add(monthAndSex.getMonth());//月份
            boys.add(monthAndSex.getCount());//男姓
        }
        List<MonthAndSex> monthAndSexes1 = userMapper.selectWomanCountByCreateDate();
        for (MonthAndSex monthAndSex : monthAndSexes1) {
            womans.add(monthAndSex.getCount());//女性
        }
        map.put("month",month);
        map.put("boys",boys);
        map.put("womans",womans);
        return map;
    }

    //查询每个城市男性和女性的注册人数
    @Override
    public ArrayList<Models> selectManOrWomanByCity() {
        //查男性
        List<City> manCities = userMapper.selectManCountByCity();
        //查女性
        List<City> womanCities = userMapper.selectWomanCountByCity();
        ArrayList<Models> models = new ArrayList<>();
        models.add(new Models("男性",manCities));
        models.add(new Models("女性",womanCities));
        return models;
    }

}
