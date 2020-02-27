package com.lbt.yingx_lbt.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Aspect
@Configuration
public class RedisCache {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate redisTemplate;
    @Around("@annotation(com.lbt.yingx_lbt.annotation.AddCache)")
    public Object saveCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("进入了环绕通知");
        //解决系列化乱码问题
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //储存形式：Hash
        /*
        KEY(该方法所属类的全限定名)   value（ key（方法名+实参） value（数据））
         */
        //key的形式使用字符串拼接
        StringBuilder stringBuilder = new StringBuilder();
        //获取累的全限定名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        stringBuilder.append(methodName);
        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        //遍历数组并拼接实参
        for (Object arg : args) {
            stringBuilder.append(arg);
        }
        //获取拼接好的key
        String key = stringBuilder.toString();
        //String类型的操作（对KEY操作）
        HashOperations hashOperations = redisTemplate.opsForHash();
        //去redis中查询改key是否存在
        Boolean aBoolean = hashOperations.hasKey(className, key);
        System.out.println(key+":"+aBoolean);
        //根据key判断缓存是否存在
        Object result = null;
        if(aBoolean){
            //存在，取出数据并返回
            result = hashOperations.get(className,key);
        }else{
             //不存在，放行方法
            result = proceedingJoinPoint.proceed();
            //将查询结果放回缓存
            hashOperations.put(className,key,result);
        }
        return result;
    }
    //清除缓存
    @After("@annotation(com.lbt.yingx_lbt.annotation.DelCache)")
    public void deleteCache(JoinPoint joinPoint){
        //清除缓存，并获取KEY
        String className = joinPoint.getTarget().getClass().getName();
        //清除该类下的所有缓存
        stringRedisTemplate.delete(className);
    }
}
