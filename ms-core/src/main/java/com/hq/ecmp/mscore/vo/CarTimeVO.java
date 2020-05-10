package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarTimeVO {
    private Long regimeId;
    private String ruleKey;
    private String startTime;
    private String endTime;
}
