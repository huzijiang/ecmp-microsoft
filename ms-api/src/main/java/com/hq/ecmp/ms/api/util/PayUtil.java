package com.hq.ecmp.ms.api.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    /**
     * @author ghb
     * @description  xml转换成string类型
     */
    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("业务处理异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("xml 转换 String报错，原因为："+e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }

}

