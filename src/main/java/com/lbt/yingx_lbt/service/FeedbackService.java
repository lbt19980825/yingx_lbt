package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.Feedback;
import com.lbt.yingx_lbt.entity.PageBean;

import java.util.List;

public interface FeedbackService {
    //查所有反馈信息
    List<Feedback> showAllFeedback(PageBean pageBean);
}
