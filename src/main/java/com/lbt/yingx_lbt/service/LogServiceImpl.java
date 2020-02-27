package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.annotation.AddCache;
import com.lbt.yingx_lbt.dao.LogMapper;
import com.lbt.yingx_lbt.entity.Log;
import com.lbt.yingx_lbt.entity.LogExample;
import com.lbt.yingx_lbt.entity.PageBean;
import com.lbt.yingx_lbt.entity.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper logMapper;
    @Override
    @AddCache
    public List<Log> showAllLog(PageBean pageBean) {
        //查询共有多少行日志
        int i = logMapper.selectCount(new Log());
        //设置页面总行数
        pageBean.setTotalRow(i);
        LogExample logExample = new LogExample();
        RowBounds rowBounds = new RowBounds(pageBean.getIngore(),pageBean.getPageRow());//忽略几条，获取几条
        List<Log> logs = logMapper.selectByExampleAndRowBounds(logExample, rowBounds);
        return logs;
    }
}
