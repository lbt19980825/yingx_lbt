package com.lbt.yingx_lbt;

import com.alibaba.fastjson.JSON;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GoEasyTests {


    @Test
    public void testSendMessageJson() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);

            Random random = new Random();
            //int i = random.nextInt(); 取值-2147483647~2147483648
            //获取随机数  参数：随机数最大值  0<=i<10
            //int i = random.nextInt(10);

            HashMap<String, Object> map = new HashMap<>();
            map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
            map.put("boys", Arrays.asList(random.nextInt(50), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            map.put("girls", Arrays.asList(5, 20, 36, 100, 10, 20));

            //将对象转为json格式字符串
            String content = JSON.toJSONString(map);

            //配置GoEasy参数：redionHost:应用的地址,Appkey：你的appKey
            GoEasy goEasy = new GoEasy( "https://rest-hangzhou.goeasy.io/publish", "BC-c99343854d5947fdb1c4120141a68f4f");
            //发送消息
            goEasy.publish("My184Channel", content);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void wqewq(){

    }
}
