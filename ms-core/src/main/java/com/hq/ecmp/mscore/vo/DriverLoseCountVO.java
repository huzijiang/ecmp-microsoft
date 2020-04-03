package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "企业信息模型")
public class DriverLoseCountVO {




    @ApiModelProperty(value = "司机人数")
    private int driverCount;



    @Override
    public String toString() {
        return "NewsVO{" +

                ", driverCount='" + driverCount + '\'' +

                '}';
    }


}
