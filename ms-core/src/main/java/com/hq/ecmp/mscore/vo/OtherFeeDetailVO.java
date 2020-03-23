package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@ApiModel(description = "地址模型")
public class OtherFeeDetailVO {

    private String typeName;
    private String cost;
    private Double costFee;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Double getCostFee() {
        return costFee;
    }

    public void setCostFee(String cost) {
        if (StringUtils.isBlank(cost)){
            this.costFee=0.0;
        }
        BigDecimal big=new BigDecimal(cost).stripTrailingZeros();
        this.costFee = Double.parseDouble(big.toPlainString());
    }
}
