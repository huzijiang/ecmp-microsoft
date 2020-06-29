package com.hq.ecmp.util;

import java.net.URLDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangnan
 */
public final class DecodeUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DecodeUtil.class);

    /**
     * 解码
     * @param str
     * @param length
     * @return
     */
    public static String decode(String str, int length) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String r = decode(str);
        if (r.length() > length) {
            return r.substring(0, length);
        }
        return r;
    }

    /**
     * 解码
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (Exception e) {
            LOGGER.error("解码异常", e);
        }
        return str;
    }
}
