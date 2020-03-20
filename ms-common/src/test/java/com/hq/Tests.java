package com.hq;

import com.hq.ecmp.util.OrderUtils;

/**
 * @ClassName Tests
 * @Description TODO
 * @Author yj
 * @Date 2020/3/19 19:15
 * @Version 1.0
 */
public class Tests {
    public static void main(String[] args) {
        String s = OrderUtils.generateOrderCode(6);
        System.out.println(s);
    }
}
