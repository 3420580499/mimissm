package com.strive.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.strive.pojo.ProductInfo;
import com.strive.pojo.vo.ProductInfoVo;
import com.strive.service.ProductService;
import com.strive.util.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author 小白
 * @create 2021/10/10
 */
@Controller
@RequestMapping("/prod")
public class ProductInfoAction {

    String fileName =" ";
    private static final int PAGE_NUM = 5;
    @Autowired
    private ProductService productService;

    //所有的商品
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request){
        List<ProductInfo> list = productService.getAll();
        request.setAttribute("list",list);
        return "product";
    }
    //先查询第一页的商品(每页的数据有5条)
    @RequestMapping("/split")
    public String split(HttpServletRequest request){
        PageInfo<ProductInfo> pageInfo = productService.splitPage(1,PAGE_NUM);
        //传进去的是PageInfo对象,里面有很多的属性
        request.setAttribute("info",pageInfo);
        return "product";
    }
    //Ajax实现分页的
    //要处理Ajax的请求,得加上这个注解
    @ResponseBody
    @RequestMapping (value = "/ajaxSplit",method = RequestMethod.POST)
    public void ajaxSplit (ProductInfoVo vo, HttpSession session){
        /*//product.jsp中发送了一个Ajax的请求,把页码传来过来,我们要重新获取当前页的数据
        PageInfo pageInfo = productService.splitPage(vo.getPage(),PAGE_NUM);
        //把他放到session中,覆盖掉上面的info
        session.setAttribute("info",pageInfo);*/
        /*
        * 后面的对条件查询进行分页时点击按钮2我们要携带vo
        * 正常的商品展示,我们也默认为是没有条件的对条件查询,都给他携带一个vo,vo里面有当前第几页page
        * */
        PageHelper.startPage(vo.getPage(),PAGE_NUM);
        List<ProductInfo> list = productService.selectCondition(vo);
        PageInfo<ProductInfo> pageInfo = new PageInfo(list);
        session.setAttribute("info",pageInfo);
    }

    //实现图片选取后的回显功能
    //会使用到件上传核心组件(springmvc.xml中)
    //参数的名称必须是那个上传文件的input框的id属性名称
    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        if(pimage != null){
            //获取一个复杂的文件名
            fileName = FileNameUtil.getUUIDFileName()+FileNameUtil.getFileType(pimage.getOriginalFilename());
        }
        //提取文件夹的真实路径
        String path = request.getServletContext().getRealPath("/image_big");
        //转存(将图片存入到这个路径中)
        //但好像有一个bug,如果用户没有提交这个数据,数据却存了进来
        try {
            //这里存入的是target目录下,clean了图片数据就就清除了
            pimage.transferTo(new File(path+File.separator+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //we back a object
        //返回一个Object对象,要有一个imgurl的属性
        //这种方法会用到org.json依赖
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgurl",fileName);
        return jsonObject.toString();
    }

    //接收添加商品的请求
    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        //其中这个info对象中有两个属性是没有赋值的(图片的名字,上架的时间)
        info.setpImage(fileName);
        info.setpDate(new Date());
        int result = productService.save(info);
        if(result == 0){
            request.setAttribute("msg","商品增加失败");
        }else{
            request.setAttribute("msg","商品添加成功");
        }
        //清空fileName,方便下一次上传文件
        fileName = " ";
        return "forward:/prod/split.action";
    }
    //接收用户点击更新商品的按钮时的请求,参数是传过来的id
    @RequestMapping("/one")
    public String one(int pid,HttpServletRequest request){
        ProductInfo info =productService.one(pid);
        //将更新商品的商品数据,传给update.jsp.给它显示
        request.setAttribute("prod",info);
        return "update";
    }
    //在update.jsp页面中,我们可以进行对单个商品的更新操作
    //当用户填完更新后的商品并提交后,我们仍然要跳转到分页的页面中
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        //因为ajax的异步图片上传,如果有上传过,
        // 则fileName里有上传上来的图片的名称,
        // 如果没有使用异步ajax上传过图片,则fileName="",
        // 实体类info使用隐藏表单域提供上来的pImage原始图片的名称;
        //第一种情况:用户更新了图片,会调用onchange,进而发送ajaxImg请求,fileName存的就是文件名称
        //第二种情况:用户没有更新图片,update.jsp上面有一个隐藏的表单,它会提供原始图片的名称
        if (!fileName.equals(" ")) {
            info.setpImage(fileName);
        }
        int result = productService.Update(info);
        if(result == 0){
            request.setAttribute("msg","商品更新失败");
        }else {
            request.setAttribute("msg","商品更新成功");
        }
        return "forward:/prod/split.action";
    }

    //接收用户删除商品的操作
    @RequestMapping("/delete")
    public String delete(int pid,HttpServletRequest request){
        int result = productService.remove(pid);
        if(result == 0){
            request.setAttribute("msg","商品删除失败");
        }else {
            request.setAttribute("msg","商品删除成功");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }
    //跳转到这里
    @ResponseBody
    @RequestMapping(value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxImg(HttpServletRequest request){
        //取得第1页的数据(并在当前页面展示)
        PageInfo info = productService.splitPage(1, PAGE_NUM);
        request.getSession().setAttribute("info",info);
        return request.getAttribute("msg");
    }

    //实现用户批量删除用户的功能
    //接受的参数是用id拼好的字符串
    @RequestMapping("deleteMany")
    //参数名称是pids,要和Ajax中的参数名称对上,否则会注入不上
    public String deleteMany (String pids,HttpServletRequest request){
        //转为数组
        String strs [] = pids.split(",");
        try {
            int num = productService.deleteMany(strs);
            if(num > 0 ){
                request.setAttribute("msg","批量删除成功!");
            }else{
                request.setAttribute("msg","批量删除失败!");
            }
        } catch (Exception e) {
            request.setAttribute("msg","商品不可删除!");
        }
        return  "forward:/prod/deleteAjaxSplit.action";
    }
    //接收使用条件查询的请求
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        //专门进行分页处理,点击按钮2发送的Ajax请求也会携带vo
        PageHelper.startPage(1,PAGE_NUM);
        List<ProductInfo> list = productService.selectCondition(vo);
        PageInfo<ProductInfo> pageInfo = new PageInfo(list);
        session.setAttribute("info",pageInfo);
    }
}


























