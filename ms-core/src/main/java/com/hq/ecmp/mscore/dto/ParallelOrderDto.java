package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ParallelOrderDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/6 11:58
 * @Version 1.0
 */
@Data
public class ParallelOrderDto {


    /**
     * 用车权限ID
     */
    @ApiModelProperty(name = "ticketId", value = "用车权限ID")
    private Long ticketId;

    /**
     * 开始地点坐标
     */
    @ApiModelProperty(name = "startPoint", value = "开始地点坐标,传A,B A 为精度，B为维度，中间拿逗号分隔")
    private String startPoint;

    /**
     * 结束地点坐标
     */
    @ApiModelProperty(name = "endPoint", value = "结束地点坐标,传A,B A 为精度，B为维度，中间拿逗号分隔")
    private String endPoint;

    /**
     * 开始地址
     */
    @ApiModelProperty(name = "startAddr", value = "开始地址")
    private String startAddr;

    /**
     * 开始地址长地址
     */
    @ApiModelProperty(name = "starAddrLong",value = "开始地址长地址")
    private String starAddrLong;

    /**
     * 结束地址
     */
    @ApiModelProperty(name = "endAddr", value = "结束地址")
    private String endAddr;

    /**
     * 结束地址长地址
     */
    @ApiModelProperty(name = "endAddrLong", value = "结束地址长地址")
    private String endAddrLong;

    /**
     * 用车时间
     */
    @ApiModelProperty(name = "bookingDate", value = "用车时间")
    private String bookingDate;

}
