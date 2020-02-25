package com.lbt.yingx_lbt.dao;




import java.util.List;

import com.lbt.yingx_lbt.po.City;
import com.lbt.yingx_lbt.po.MonthAndSex;
import tk.mybatis.mapper.common.Mapper;
import com.lbt.yingx_lbt.entity.User;

public interface UserMapper extends Mapper<User> {
    //查询每个月份男性的注册用户数量
    List<MonthAndSex> selectManCountByCreateDate();
    //查询每个月份女性的注册用户数量
    List<MonthAndSex> selectWomanCountByCreateDate();
    //查询每个城市男性的注册用户数量
    List<City> selectManCountByCity();
    //查询每个城市女性的注册用户数量
    List<City> selectWomanCountByCity();

}