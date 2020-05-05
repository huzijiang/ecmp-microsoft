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
public class OrderStateCountVO {


    /**
     * 经度
     */
    @ApiModelProperty(name = "longitude",value = "待派车数量")
    private Long dispatchedCount;

    /**
     * 城市code
     */
    @ApiModelProperty(name = "reassignedCount",value = "待改派数量")
    private Long reassignedCount;

    public OrderStateCountVO() {
    }

    public OrderStateCountVO(Long dispatchedCount, Long reassignedCount) {
        this.dispatchedCount = dispatchedCount;
        this.reassignedCount = reassignedCount;
    }
}
