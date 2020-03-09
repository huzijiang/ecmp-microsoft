package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ApplyUseWithTravelDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/7 13:37
 * @Version 1.0
 */
@Data
public class ApplyUseWithTravelDto extends ParallelOrderDto {
    /**
     * 订单ID
     */
    @ApiModelProperty(name = "orderId", value = "用车权限ID")
    private Long orderId;
}
