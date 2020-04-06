package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "审批模型")
public class RegisterVO {


    @ApiModelProperty(value = "人数")
    private int registerCount;


    @Override
    public String toString() {
        return "RegisterVO{" +
                "registerCount='" + registerCount + '\'' +

                '}';
    }


}
