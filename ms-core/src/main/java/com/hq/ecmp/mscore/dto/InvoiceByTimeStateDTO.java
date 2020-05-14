package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("发票列表查询模型")
public class InvoiceByTimeStateDTO {

    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "状态")
    private String status;
    private Integer pageNum;
    private Integer pageSize;
    private Long createBy;





}
