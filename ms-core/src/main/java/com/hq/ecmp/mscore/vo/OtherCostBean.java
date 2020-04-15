package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */

@AllArgsConstructor
@NoArgsConstructor
public class OtherCostBean {

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
        return Double.valueOf(cost);
    }
    
}
