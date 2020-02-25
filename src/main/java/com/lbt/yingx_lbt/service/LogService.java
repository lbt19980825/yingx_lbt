package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.Log;
import com.lbt.yingx_lbt.entity.PageBean;

import java.util.List;


public interface LogService {
    //查所有日志信息
    List<Log> showAllLog(PageBean pageBean);
}
