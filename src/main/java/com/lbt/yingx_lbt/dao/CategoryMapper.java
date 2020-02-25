package com.lbt.yingx_lbt.dao;


import com.lbt.yingx_lbt.entity.Category;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.po.CategorySelfPo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CategoryMapper extends Mapper<Category> {
    //根据一级类别id获取该一级类别下的所有二级类别
    List<Category> findAllSecondCategory(@Param("pageBean") PageBean pageBean, @Param("parentId") String parentId);
    //查询该一级类别下共有多少条二级类别
    Integer findAllSecondCount(String parentId);
    //供app接口使用
    //查询一级类别
    List<CategorySelfPo> quertAllOne();
    //根据一级查二级
    List<CategorySelfPo> quertAllTwo(String parentId);

}