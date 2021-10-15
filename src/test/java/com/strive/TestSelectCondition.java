package com.strive;

import com.strive.mapper.ProductInfoMapper;
import com.strive.pojo.ProductInfo;
import com.strive.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/14
 */
//使用spring提供的测试注解
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class TestSelectCondition{

    //因为加了注解,这里会自动注入
    @Autowired
    private ProductInfoMapper mapper;

    @Test
    public void testCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        List<ProductInfo> list = mapper.selectCondition(vo);
        list.forEach(System.out::println);
    }
}