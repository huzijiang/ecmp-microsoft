package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/23 16:29
 */
@Data
public class DriverScheduleDTO {

    @ApiModelProperty(value = "年月 ",example = "2020-03")
    private String scheduleDate;
}
