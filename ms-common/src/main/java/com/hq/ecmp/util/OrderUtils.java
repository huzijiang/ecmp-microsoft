package com.hq.ecmp.util;

import com.hq.common.utils.DateUtils;
import com.hq.core.web.domain.server.Sys;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @ClassName OrderUtils
 * @Description TODO
 * @Author yj
 * @Date 2020/3/19 11:33
 * @Version 1.0
 */
public class OrderUtils {

    public static String generateOrderCode(int count){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer end = new StringBuffer();
        String format = simpleDateFormat.format(DateUtils.getNowDate());
        StringBuffer result = new StringBuffer(format);
        Random random = new Random();
        for (int i=0;i<count;i++){
            end = end.append("9");
        }
        Integer endL = Integer.parseInt(end.toString());
        Integer i = random.nextInt(endL);
        result = result.append(i+"");

        return result.toString();
    }

    /**
     * 生成订单编号
     * @return
     */
    public static  String getOrderNum(){
        return generateOrderCode(6);
    }
}
