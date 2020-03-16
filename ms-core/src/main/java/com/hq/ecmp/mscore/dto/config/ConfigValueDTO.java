package com.hq.ecmp.mscore.dto.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/3/16
 * ecmp-microservice.
 */
@Data
@Builder
public class ConfigValueDTO {

    /**
     * 开关状态 0代表开 1代表关
     * eg: 背景图 0默认 1定义；背景图如果是默认的话是关闭状态，value也会返回图片的路径
     * 开屏图 0开 1关
     * 短信 0开 1关
     */
    private String status;

    /**
     * 值信息
     */
    private String value;

}
