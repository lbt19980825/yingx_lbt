package com.lbt.yingx_lbt.service;

import com.lbt.yingx_lbt.entity.Admin;

import java.util.HashMap;

public interface AdminService {
    //登录
    public HashMap<String,String> findAdminByUsername(Admin admin, String code);
}
