package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.annotation.AddCache;
import com.lbt.yingx_lbt.dao.FeedbackMapper;
import com.lbt.yingx_lbt.entity.Feedback;
import com.lbt.yingx_lbt.entity.PageBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    @Resource
    private FeedbackMapper feedbackMapper;


    //查所有反馈信息
    @AddCache
    @Override
    public List<Feedback> showAllFeedback(PageBean pageBean) {
        //先查出有多少条反馈信息
        int i = feedbackMapper.selectCount(new Feedback());
        //设置总行数
        pageBean.setTotalRow(i);
        List<Feedback> feedbacks = feedbackMapper.selectAll();
        return feedbacks;
    }
}
