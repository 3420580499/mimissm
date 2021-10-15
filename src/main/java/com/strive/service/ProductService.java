package com.strive.service;

import com.github.pagehelper.PageInfo;
import com.strive.pojo.ProductInfo;
import com.strive.pojo.vo.ProductInfoVo;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/10
 */
public interface ProductService {

    List<ProductInfo> getAll();
    //参数为当前的页数,一页有几条数据
    PageInfo splitPage(int nowPage,int pageNum);
    //添加商品
    int save(ProductInfo info);
    //根据id显示商品(在页面上回显商品的信息)
    ProductInfo one (int id);
    //更新商品
    int Update (ProductInfo info );
    //删除商品(根据id删除商品)
    int remove(int id);
    //批量删除商品(传入要删除的id值)
    int deleteMany(String ids []);
    //多条件查询
    List<ProductInfo> selectCondition(ProductInfoVo vo);
}
