package com.strive.service.impl;

import com.strive.mapper.ProductTypeMapper;
import com.strive.pojo.ProductType;
import com.strive.pojo.ProductTypeExample;
import com.strive.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/11
 */
@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Override
    public List<ProductType> getAll() {
        return productTypeMapper.selectByExample(new ProductTypeExample());
    }

}
