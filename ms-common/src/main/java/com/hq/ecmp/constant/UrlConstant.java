package com.hq.ecmp.constant;


import com.hq.ecmp.util.SpringBeanUtil;

/**
 * @author yangnan
 */
public final class UrlConstant {
    private static final CloudUrlConfig microseviceConfig = SpringBeanUtil.getBean(CloudUrlConfig.class);

    public static String getCloudUrl() {

        return microseviceConfig.getUrl();
    }

}
