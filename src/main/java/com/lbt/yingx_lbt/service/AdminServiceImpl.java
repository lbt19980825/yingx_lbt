package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.annotation.AddCache;
import com.lbt.yingx_lbt.dao.AdminMapper;
import com.lbt.yingx_lbt.entity.Admin;
import com.lbt.yingx_lbt.entity.AdminExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private HttpSession session;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    public HashMap<String,String> findAdminByUsername(Admin admin,String code) {
        String  code1 = (String) session.getAttribute("code");//获取生成的验证码
        HashMap<String, String> map = new HashMap<>();
        //判断验证码是否正确
        if(code1.equals(code)){
            //判断用户是否存在
            AdminExample adminExample = new AdminExample();
            adminExample.createCriteria().andUsernameEqualTo(admin.getUsername());
            Admin admin1 = adminMapper.selectOneByExample(adminExample);
            if(admin1!=null){
                //判断密码是否正确
                if(admin.getPassword().equals(admin1.getPassword())){
                    //存放一个登录标记
                    session.setAttribute("usernames",admin1.getUsername());
                    map.put("status","200");
                    map.put("message","登陆成功");
                }else{
                    map.put("status","400");
                    map.put("message","密码错误！");
                }
            }else{
                map.put("status","400");
                map.put("message","管理员不存在！");
            }
        }else{
            map.put("status","400");
            map.put("message","验证码错误！");
        }
        return map;
    }
}
