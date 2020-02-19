package com.hq.ecmp.ms.api.vo.threeparty;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 航班信息视图对象
 * @Author: zj.hu
 * @Date: 2020-01-04 14:26
 */
@Data
public class FlightVo {

    /**
     * 航班号
     */
    @ApiParam(required = true)
    private String fltNo;
}
