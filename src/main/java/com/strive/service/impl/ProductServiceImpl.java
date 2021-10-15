package com.strive.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.strive.mapper.ProductInfoMapper;
import com.strive.pojo.ProductInfo;
import com.strive.pojo.ProductInfoExample;
import com.strive.pojo.vo.ProductInfoVo;
import com.strive.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小白
 * @create 2021/10/10
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductInfoMapper productInfoMapper;

    //得到所有的商品信息
    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    //通过分页插件实现分页
    @Override
    public PageInfo splitPage(int nowPage, int pageNum) {
        //使用PageHelper这个工具类来设置分页信息
        PageHelper.startPage(nowPage,pageNum);

        //进行PageInfo的数据封装
        //进行有条件的查询操作,必须要创建ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序,按主键降序排序.
        //select * from product_info order by p_id desc
        example.setOrderByClause("p_id desc");
        //设置完排序后,取集合,切记切记:一定在取集合之前,设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list =  productInfoMapper.selectByExample(example);
        //将查询到的集合封装进PageInfo对象中
        //这个Pageinfo对象有很多的属性,如:当前页数,总共页数等
        PageInfo<ProductInfo> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo one(int id) {
        return productInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int Update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int remove(int id) {
        return productInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteMany(String[] ids) {
        return productInfoMapper.deleteMore(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }


}
