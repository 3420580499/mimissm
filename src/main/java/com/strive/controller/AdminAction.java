package com.strive.controller;

import com.strive.pojo.Admin;
import com.strive.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @author 小白
 * @create 2021/10/10
 */
@Controller
@RequestMapping("/admin")
public class AdminAction {
    //在所有的界面层中,一定有业务逻辑层的对象
    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")
    public String login (String name , String pwd , HttpServletRequest request){
        Admin admin = adminService.login(name,pwd);
        if(admin != null){
            request.setAttribute("admin",admin);
            return "main";
        }else {
            request.setAttribute("errmsg","用户名或者密码不正确");
            return "login";
        }

    }
}
