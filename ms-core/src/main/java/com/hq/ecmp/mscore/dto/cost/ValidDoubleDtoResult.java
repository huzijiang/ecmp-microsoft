package com.hq.ecmp.mscore.dto.cost;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/18 11:44
 * @Version 1.0
 */
@Data
@Builder
public class ValidDoubleDtoResult {
    /**
     * 城市名字
     */
    private String cityName;
    /**
     * 车型名字
     */
    private String carTypeName;
    /**
     * 服务类型
     */
    private String serviceType;
}
