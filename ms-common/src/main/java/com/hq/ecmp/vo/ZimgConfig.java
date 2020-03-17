package com.hq.ecmp.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xueyong
 */
@Data
@Configuration
public class ZimgConfig {

    @Value("${app.zimg.server}")
    private String zimgServer;
    @Value("${app.zimg.access.url}")
    private String imageServer;
}