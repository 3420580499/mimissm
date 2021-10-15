package com.strive.service;

import com.strive.pojo.ProductType;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/11
 */
public interface ProductTypeService {
    //获取所有的类型
    List<ProductType> getAll();
}
