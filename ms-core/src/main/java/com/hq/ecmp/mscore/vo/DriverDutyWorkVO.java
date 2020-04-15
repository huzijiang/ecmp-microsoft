package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/11 11:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverDutyWorkVO {

    /**
     * 司机排班日期
     */
   // private String dutyDate;

    /**
     * 排班的状态 eg 1正常 2请假
     */
    private String status;

    @ApiModelProperty(value = "上班时间")
    private List<String> dutyDate;
    @ApiModelProperty(value = "休假时间")
    private List<String> holidays;
}
