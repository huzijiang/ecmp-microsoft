package com.hq.ecmp.mscore.dto.config;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/3/23
 * ecmp-microservice.
 */
@Data
public class AutoDispatchSetting {
    /**
     * 表示周几 注意0代表周日 以此类推
     */
    private String weekType;
    private String startTime;
    private String endTime;
    private String nextDay;
}
