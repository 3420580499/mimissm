package com.strive.service;

import com.strive.pojo.Admin;

/**
 * @author 小白
 * @create 2021/10/10
 */
public interface AdminService {

    //登录功能的实现
    Admin login (String name,String password);
}
