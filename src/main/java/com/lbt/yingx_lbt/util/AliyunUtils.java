package com.lbt.yingx_lbt.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

public class AliyunUtils {
    //小配置文件验证信息
    public static HashMap<String,String> Information(){
        HashMap<String, String> map = new HashMap<>();
        //读取小配置文件
        Properties prop = new Properties();
        //如果properties文件和某个java文件在同一目录下，可以如下读取配置文件：
        //如果出现空指针异常原因可能为：要把小配置文件放在resources目录下
       InputStream inStream = AliyunUtils.class.getClassLoader().getResourceAsStream("AliyunPeoperties.properties");
        try {
            prop.load(inStream);//properties文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        String accessKeyId = prop.getProperty("accessKeyId");
        String accessKeySecret = prop.getProperty("accessKeySecret");
        String signName = prop.getProperty("signName");
        String templateCode = prop.getProperty("templateCode");
        map.put("accessKeyId",accessKeyId);
        map.put("accessKeySecret",accessKeySecret);
        map.put("signName",signName);
        map.put("templateCode",templateCode);
        return map;
    }
    //生成6位随机数
    public static  String getRandom(int n){
        char[] code =  "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(code[new Random().nextInt(code.length)]);
        }
        return sb.toString();
    }
    //发送短信验证
    public static void sendMessage(String code,String phone){
        //获取配置信息
        HashMap<String, String> map = Information();
        String accessKeyId = map.get("accessKeyId");
        String accessKeySecret = map.get("accessKeySecret");
        String signName = map.get("signName");
        String templateCode = map.get("templateCode");
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName",signName);
        request.putQueryParameter("TemplateCode",templateCode);
        request.putQueryParameter("TemplateParam","{code:"+code+"}");
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("出现异常，发送失败");
        }
    }
}
