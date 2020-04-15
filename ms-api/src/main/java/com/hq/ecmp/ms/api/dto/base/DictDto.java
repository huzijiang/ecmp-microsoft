package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: shixin
 * @Date: 2020-03-04 15:26
 */
@Data
public class DictDto {



    /**
     * 数据字典类型
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private  String dictType;


}
