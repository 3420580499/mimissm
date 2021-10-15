package com.strive.service.impl;

import com.strive.mapper.AdminMapper;
import com.strive.pojo.Admin;
import com.strive.pojo.AdminExample;
import com.strive.service.AdminService;
import com.strive.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/10
 */
@Service
public class AdminServiceImpl implements AdminService {

    //在业务逻辑层中,一定会有数据访问层的对象
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String name, String password) {
        //根据用户传入的用户名查询相应的用户对象
        //使用的mapper是 List<Admin> selectByExample(AdminExample example)
        //如果有条件,则一定要创建AdminExample的对象(这个对象会在SQL语句后面拼接上查询条件),用来封装条件
        AdminExample example = new AdminExample();
        /*
        * 如何添加条件
        * select * from admin where a_name = 'admin'
        * */
        //如果下面的代码不写,就不会有where语句
        example.createCriteria().andANameEqualTo(name);
        List<Admin> list= adminMapper.selectByExample(example);
        if(list.size()>0){
            //如果查询到用户对象,在进行密码的对比
            if (list.get(0).getaPass().equals(MD5Util.getMD5(password))) {
                return list.get(0);
            }
        }
        return null;
    }
}
