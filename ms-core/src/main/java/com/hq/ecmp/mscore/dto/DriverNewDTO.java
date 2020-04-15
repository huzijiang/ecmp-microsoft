package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/9 15:12
 */
@Data
public class DriverNewDTO {
    /**
     * 驾驶员id
     *
     */
    private Long driverId;
    /**
     * 车队id
     *
     */
    private Long deptId;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 离职时间
     */
    @ApiModelProperty(value= "离职时间")
    private Date dimTime;




}
