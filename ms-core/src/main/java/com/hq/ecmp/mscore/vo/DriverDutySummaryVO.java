package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/11 11:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDutySummaryVO {

    @ApiModelProperty(name = "shouldDuty",value = "应出勤天数")
    private Integer shouldDuty;

    @ApiModelProperty(name = "alreadyDuty",value = "已出勤天数")
    private Integer alreadyDuty;
}
