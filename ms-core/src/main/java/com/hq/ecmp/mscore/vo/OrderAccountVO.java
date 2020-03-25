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

    @ApiModelProperty(name = "accountDate",value = "账期")
    private String accountDate;
    @ApiModelProperty(name = "desc",value = "账期描述")
    private String desc;
    private String amount;

    @Override
    public String toString() {
        return "OrderAccountVO{" +
                "accountDate='" + accountDate + '\'' +
                ", desc='" + desc + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
