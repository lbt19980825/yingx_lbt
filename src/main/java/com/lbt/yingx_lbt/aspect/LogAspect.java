package com.lbt.yingx_lbt.aspect;

import com.lbt.yingx_lbt.annotation.LogRecords;
import com.lbt.yingx_lbt.dao.LogMapper;
import com.lbt.yingx_lbt.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;


@Configuration
@Aspect
public class LogAspect {
    @Resource
    private HttpSession session;
    @Resource
    private LogMapper logMapper;
    //环绕通知
    @Around("@annotation(com.lbt.yingx_lbt.annotation.LogRecords)")
    public Object Log(ProceedingJoinPoint proceedingJoinPoint){
        //获取切到的方法  注意：一定要用MethodSignature接收，否则无法获取方法信息
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获取方法信息
        Method method = signature.getMethod();
        //获取方法上的注解(要指明具体哪个注解)
        LogRecords logRecords = method.getAnnotation(LogRecords.class);
        //获取注解中的值
        String value = logRecords.value();
        //要入库的信息： 哪位管理员   什么时间    做了什么操作   是否成功了
        //哪位管理员
        String username = (String) session.getAttribute("usernames");
        //时间
        Date date = new Date();
        //进行的操作
        String methodName = signature.getName();
        String message = null;
        try {
            //放行的方法  没有此方法无法进行后续操作
            Object proceed = proceedingJoinPoint.proceed();
            message = "success";
            //将日志信息入库
            Log log = new Log(UUID.randomUUID().toString(),username,date,value+"("+methodName+")",message);
            logMapper.insert(log);
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            message = "fail";
            //将日志信息入库
            Log log = new Log(UUID.randomUUID().toString(),username,date,value+"("+methodName+")",message);
            logMapper.insert(log);
            return null;
        }
    }

}
