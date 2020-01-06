package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-01-06 16:05
 */
@Data
public class AppCurrentVersionDto {

    /**
     * 版本号
     */
    @ApiParam(required = true)
    String version;

    /**
     * 类型：
     * 苹果        ： AP00
     * android    ： AN00
     */
    String type;
}
