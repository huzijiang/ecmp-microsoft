package com.hq.ecmp.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yangnan
 */
@Component
public final class CloudUrlConfig {

    /**
     * header请求
     */

    @Value("${microservice.url}")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
