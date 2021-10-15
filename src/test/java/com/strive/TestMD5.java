package com.strive;

import com.strive.util.MD5Util;
import org.junit.Test;

/**
 * @author 小白
 * @create 2021/10/9
 */
public class TestMD5 {
    @Test
    public void test() {
        //原始的密码是000000,经过MD5加密
        //此加密不可逆
        String ps = MD5Util.getMD5("000000");
        System.out.println(ps);
    }
}
