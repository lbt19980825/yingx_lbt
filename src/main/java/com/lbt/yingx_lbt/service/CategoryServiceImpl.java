package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.dao.CategoryMapper;
import com.lbt.yingx_lbt.dao.VideoMapper;
import com.lbt.yingx_lbt.entity.Category;
import com.lbt.yingx_lbt.entity.CategoryExample;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.VideoExample;
import com.lbt.yingx_lbt.po.CategorySelfPo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Table;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private VideoMapper videoMapper;

    //展示所有一级类别
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Category> showAllFirstCategory(PageBean pageBean) {
        Category category = new Category();
        category.setLevels("1");//设置级别为一级类别
        int i = categoryMapper.selectCount(category);//查询一级类别总共有多少行
        pageBean.setTotalRow(i);//设置总行数;

        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andLevelsEqualTo("1");
        RowBounds rowBounds = new RowBounds(pageBean.getIngore(),pageBean.getPageRow());//忽略几条，获取几条
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(categoryExample, rowBounds);//分页查询

        return categories;
    }

    //根据一级类别id获取该一级类别下的所有二级类别
    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public List<Category> showAllSecondCategory(PageBean pageBean, String parentId) {
        //先查询该一级类别下的二级类共有多少条
        Integer allSecondCount = categoryMapper.findAllSecondCount(parentId);
        pageBean.setTotalRow(allSecondCount);
        List<Category> allSecondCategory = categoryMapper.findAllSecondCategory(pageBean, parentId);
        return allSecondCategory;
    }

    //添加一级类别
    @Override
    public void insertFirstCategory(Category category) {
        category.setId(UUID.randomUUID().toString());
        category.setLevels("1");
        categoryMapper.insert(category);
    }

    //修改一级或二级类别
    @Override
    public void updateFirstAndSecondCategory(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }


    //删除一级类别
    @Override
    public HashMap<String,Object> deleteFirstCategory(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andParentIdEqualTo(category.getId());
        int i = categoryMapper.selectCountByExample(categoryExample);
        if(i==0){
            categoryMapper.deleteByPrimaryKey(category.getId());
            map.put("status","200");
            map.put("message","删除成功");
        }else{
            map.put("status","400");
            map.put("message","删除失败,该一级类别下还有二级类别！！");
        }
        return map;
    }

    //删除二级类别
    @Override
    public HashMap<String,Object> deleteSecondCategory(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        //查询是否有此类别的视频
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andCategoryIdEqualTo(category.getId());
        int i = videoMapper.selectCountByExample(videoExample);
        if(i==0){
            categoryMapper.deleteByPrimaryKey(category.getId());
            map.put("status","200");
            map.put("message","删除成功");
        }else{
           map.put("status","400");
           map.put("message","删除失败,该二级类别下还有视频！！");
        }
        return map;
    }

    //添加二级类别
    @Override
    public void insertSecondCategory(Category category,String parentId) {
        category.setId(UUID.randomUUID().toString());
        category.setLevels("2");//设置级别
        category.setParentId(parentId);//设置所属一级类别id
        categoryMapper.insert(category);
    }



    //查询所有类别，供app分类展示
    public List<CategorySelfPo> queryCateVideoList(){
        List<CategorySelfPo> categorySelfPos = categoryMapper.quertAllOne();
        for (CategorySelfPo categorySelfPo : categorySelfPos) {
            List<CategorySelfPo> categorySelfPos1 = categoryMapper.quertAllTwo(categorySelfPo.getId());
            categorySelfPo.setCategoryList(categorySelfPos1);
        }
        return categorySelfPos;
    }



}
