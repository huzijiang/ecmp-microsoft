package com.hq.ecmp.ms.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

/**
 * @author ghb
 * @description 支付工具类
 * @date 2020/5/5
 */
public class PayUtil {

    static Logger log = LogManager.getLogger(PayUtil.class.getName());

    /**
     * 生成随机数
     *
     * @return
     */
    public static String makeUUID(int len) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, len);
    }

}

