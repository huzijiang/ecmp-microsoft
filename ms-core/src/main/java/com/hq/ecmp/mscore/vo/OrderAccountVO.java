package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "发票列表模型")
public class OrderAccountVO {

    @ApiModelProperty(name = "periodId",value = "账期Id")
    private int periodId;
    @ApiModelProperty(name = "beginDate",value = "开始时间")
    private String beginDate;
    @ApiModelProperty(name = "endDate",value = "结束时间")
    private String endDate;
    @ApiModelProperty(name = "desc",value = "账期描述")
    private String desc;
    @ApiModelProperty(name = "total",value = "金额")
    private String total;

    @Override
    public String toString() {
        return "OrderAccountVO{" +
                "periodId='" + periodId + '\'' +
                "beginDate='" + beginDate + '\'' +
                "endDate='" + endDate + '\'' +
                ", desc='" + desc + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
