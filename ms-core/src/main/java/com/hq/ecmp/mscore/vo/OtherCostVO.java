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
@ApiModel(description = "地址模型")
public class OtherCostVO {

    @ApiModelProperty(name = "typeName",value = "费用名称")
    private String typeName;
    @ApiModelProperty(name = "cost",value = "费用")
    private String cost;
    @ApiModelProperty(name = "baseMileage",value = "基础里程")
    private String baseMileage;
    @ApiModelProperty(name = "baseTime",value = "基础时长")
    private String baseTime;
    @ApiModelProperty(name = "desc",value = "费用描述")
    private String desc;

    public OtherCostVO() {
    }

    public OtherCostVO(String typeName, String cost) {
        this.typeName = typeName;
        this.cost = cost;
    }

    public OtherCostVO(String typeName, String cost, String desc) {
        this.typeName = typeName;
        this.cost = cost;
        this.desc = desc;
    }

    public OtherCostVO(String typeName, String cost, String baseMileage, String baseTime, String desc) {
        this.typeName = typeName;
        this.cost = cost;
        this.baseMileage = baseMileage;
        this.baseTime = baseTime;
        this.desc = desc;
    }
}
