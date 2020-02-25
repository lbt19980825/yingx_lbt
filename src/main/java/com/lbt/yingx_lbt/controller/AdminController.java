package com.lbt.yingx_lbt.controller;

import com.lbt.yingx_lbt.annotation.LogRecords;
import com.lbt.yingx_lbt.entity.Admin;
import com.lbt.yingx_lbt.service.AdminService;
import com.lbt.yingx_lbt.util.AliyunUtils;
import com.lbt.yingx_lbt.util.SecurityCode;
import com.lbt.yingx_lbt.util.SecurityImage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    //账号密码方式登录
    @RequestMapping("/loginAdmin")
    @ResponseBody
    public HashMap<String,String> loginAdmin(Admin admin, String code, HttpSession session){
        HashMap<String, String> adminByUsername = adminService.findAdminByUsername(admin, code);
        return adminByUsername;
    }
    //验证码
    @RequestMapping("code")
    public String code(HttpSession session,ServletOutputStream stream){
        String securityCode = SecurityCode.getSecurityCode();//生成验证码，随机字符
        session.setAttribute("code",securityCode);//将生成的验证码存入session，供登录时验证
        BufferedImage image = SecurityImage.createImage(securityCode);//生成验证码图片
        try {
            ImageIO.write(image,"png",stream);//将验证码图片以png格式响应到页面
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //安全退出
    @LogRecords(value="退出")
    @RequestMapping("/exit")
    public String exit(HttpSession session){
        session.invalidate();//销毁session
        return "login/login";
    }
    //获取短信验证码
    @RequestMapping("/getMessage")
    @ResponseBody
    public String getMessage(String phone,HttpSession session){
        //调用工具类，生成6位随机数字
        String random = AliyunUtils.getRandom(6);
        //调用工具类，发送验证码
        AliyunUtils.sendMessage(random,phone);
        //将验证码存在session，供登录时验证
        session.setAttribute("messageCode",random);
        //将手机号存入session，供页面展示
        session.setAttribute("usernames",phone);
        return "发送成功";
    }
    //短息验证方式登录
    @RequestMapping("/phoneLogin")
    @ResponseBody
    public String phoneLogin(String phoneCode,HttpSession session){
        //存一个登录验证
        session.setAttribute("username",phoneCode);
        String messageCode = (String) session.getAttribute("messageCode");
        String message = null;
        if(phoneCode.equals(messageCode)){
            message = "验证成功";
        }else{
            message = "验证失败";
        }
        return message;
    }
}
