package com.hq.ecmp.ms.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CommonUtils {

    //年月日加四位随机数
    public static String getRandomFileName(){
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int rannum = (int) (new Random().nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取5位随机数
        return str+rannum;// 

    }
}
