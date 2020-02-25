package com.lbt.yingx_lbt.controller;

import com.lbt.yingx_lbt.dao.FeedbackMapper;
import com.lbt.yingx_lbt.entity.Feedback;
import com.lbt.yingx_lbt.entity.Log;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    @Resource
    private FeedbackService feedbackService;

    @RequestMapping("showAll")
    @ResponseBody
    public HashMap<String,Object> showAll(Integer page,Integer rows){
        PageBean pageBean = new PageBean();
        pageBean.setPageNo(page);
        pageBean.setPageRow(rows);
        HashMap<String, Object> map = new HashMap<>();
        //查所有日志
        List<Feedback> feedbacks = feedbackService.showAllFeedback(pageBean);
        //将要返回给前台的数据放在map集合中
        //total:总页数   records：总条数   page：当前页   rows：数据
        map.put("total",pageBean.getTotalPage());
        map.put("records",pageBean.getTotalRow());
        map.put("page", pageBean.getPageNo());
        map.put("rows",feedbacks);
        return map;
    }

}
