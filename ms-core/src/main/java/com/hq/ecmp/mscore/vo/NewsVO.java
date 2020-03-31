package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "企业信息模型")
public class NewsVO {


    @ApiModelProperty(value = "员工人数")
    private int userCount;

    @ApiModelProperty(value = "司机人数")
    private int driverCount;

    @ApiModelProperty(value = "车辆人数")
    private int carCount;

    @Override
    public String toString() {
        return "NewsVO{" +
                "userCount='" + userCount + '\'' +
                ", driverCount='" + driverCount + '\'' +
                ", carCount='" + carCount + '\'' +
                '}';
    }


}
