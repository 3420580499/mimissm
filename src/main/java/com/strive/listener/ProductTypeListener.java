package com.strive.listener;

import com.strive.pojo.ProductType;
import com.strive.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * @author 小白
 * @create 2021/10/11
 */
//注册这个lisenter
@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //虽然我们在另一个listener中已经创建了spring容器,但是我们不能确定到底是它快还是当前监听器快
        //手工从Spring容器中取出ProductTypeServiceImpl的对象.
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_*.xml");
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("productTypeServiceImpl");
        List<ProductType> typeList = productTypeService.getAll();
        //放入全局应用作用域中,供新增页面,修改页面,前台的查询功能提供全部商品类别集合
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);
    }
   //Tomcat7没有给予默认实现,只能重写
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
