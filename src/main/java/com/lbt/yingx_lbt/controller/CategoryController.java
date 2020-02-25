package com.lbt.yingx_lbt.controller;

import com.lbt.yingx_lbt.annotation.LogRecords;
import com.lbt.yingx_lbt.entity.Category;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    //展示所有一节类别
    @RequestMapping("/showAllFirstCategory")
    @ResponseBody
    public HashMap<String,Object> showAllFirstCategory(Integer page,Integer rows){
        HashMap<String, Object> map = new HashMap<>();
        PageBean pageBean = new PageBean();
        pageBean.setPageRow(rows);//每页显示的行数
        pageBean.setPageNo(page);//设置当前页数
        List<Category> categories = categoryService.showAllFirstCategory(pageBean);
        //将要返回给前台的数据放在map集合中
        //total:总页数   records：总条数   page：当前页   rows：数据
        map.put("total",pageBean.getTotalPage());
        map.put("records",pageBean.getTotalRow());
        map.put("page", pageBean.getPageNo());
        map.put("rows",categories);
        return map;
    }

    //根据一级类别id获取该一级类别下的所有二级类别
    @RequestMapping("/showAllSecondByParentId")
    @ResponseBody
    public HashMap<String,Object> showAllSecondByParentId(Integer page,Integer rows,String parentId){
        HashMap<String, Object> map = new HashMap<>();
        PageBean pageBean = new PageBean();
        pageBean.setPageRow(rows);//每页显示的行数
        pageBean.setPageNo(page);//设置当前页数
        List<Category> categories = categoryService.showAllSecondCategory(pageBean, parentId);
        //将要返回给前台的数据放在map集合中
        //total:总页数   records：总条数   page：当前页   rows：数据
        map.put("total",pageBean.getTotalPage());
        map.put("records",pageBean.getTotalRow());
        map.put("page", pageBean.getPageNo());
        map.put("rows",categories);
        return map;
    }
    //对一级表表进行增删改操作
    @LogRecords(value="修改||添加||删除一级类别")
    @RequestMapping("/edit")
    @ResponseBody
    public HashMap<String ,Object> edit(Category category,String oper){
        HashMap<String, Object> map = null;
        if("add".equals(oper)){//添加操作
            categoryService.insertFirstCategory(category);
        }
        if("del".equals(oper)){//删除操作
            map = categoryService.deleteFirstCategory(category);
        }
        if("edit".equals(oper)){//修改操作
            categoryService.updateFirstAndSecondCategory(category);
        }
        return map;
    }
    //对二级表表进行增删改操作
    @LogRecords(value="修改||添加||删除二级类别")
    @RequestMapping("/secondEdit")
    @ResponseBody
    public HashMap<String ,Object> secondEdit(Category category,String oper,String parentId){
        HashMap<String, Object> map = null;
        if("add".equals(oper)){
            categoryService.insertSecondCategory(category,parentId);
        }
        if("del".equals(oper)){
            map= categoryService.deleteSecondCategory(category);
        }
        if("edit".equals(oper)){
            categoryService.updateFirstAndSecondCategory(category);
        }
        return map;
    }
}
