package com.hq.ecmp.ms.api.dto.threeparty;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 14:22
 */
@Data
public class LocationDto {

    /**
     * 订单id
     */
    @NotEmpty
    @ApiParam(required = true)
    private Long orderId;
    /**用户角色(0司机,1乘客)*/
    @NotEmpty
    @ApiParam(required = true)
    private Integer userRole;
}
