package com.hq.ecmp.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 生成申请单编号
 * @Author: chao.zhang
 * @Date: 2020/3/25 15:04
 */
public class RandomUtil {

    /**
     * 生成申请单号：当前年月日时分秒+6位随机数
     *
     * @return
     */
    public static String getRandomNumber() {

        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        int num = (int)((Math.random()*9+1)*100000);

        return  str + num ;
    }


}
