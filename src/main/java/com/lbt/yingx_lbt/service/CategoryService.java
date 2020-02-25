package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.Category;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.po.CategorySelfPo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {
    //展示所有一级类别
     List<Category> showAllFirstCategory(PageBean pageBean);
    //根据一级类别id获取该一级类别下的所有二级类别
     List<Category> showAllSecondCategory(PageBean pageBean,String parentId);
    //添加一级类别
    void insertFirstCategory(Category category);
    //修改一级或二级类别
    void updateFirstAndSecondCategory(Category category);
    //删除一级类别
    HashMap<String,Object> deleteFirstCategory(Category category);
    //删除二级类别
    HashMap<String,Object> deleteSecondCategory(Category category);
    //添加二级类别
    void insertSecondCategory(Category category,String parentId);
    //查询所有类别，供app分类展示
    public List<CategorySelfPo> queryCateVideoList();


}
