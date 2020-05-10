package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2020-01-03 17:41
 */
@Data
public class RegimeCheckDto {

    /**
     * 申请单编号
     */
    @ApiParam(required = true)
    @NotNull
    private Long regimeId;
    private String startTime;
    private String useCarMode;
    private String endTime;
    private List<String> cityCodes;
    private String startCityCode;


}
