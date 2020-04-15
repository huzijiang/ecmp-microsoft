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
@ApiModel(description = "乘车人模型")
public class PassengerInfoVO {

    /**
     * 长地址
     */
    @ApiModelProperty(name = "name",value = "姓名")
    private String name;

    /**
     * 短地址（具体地址）
     */
    @ApiModelProperty(name = "phone",value = "电话")
    private String phone;

    private String type;

    public PassengerInfoVO() {
    }

    public PassengerInfoVO(String name, String phone, String type) {
        this.name = name;
        this.phone = phone;
        this.type = type;
    }
}
