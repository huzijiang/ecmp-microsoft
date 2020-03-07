package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: ShiXin
 * @Date: 2020-03-04 15:26
 */
@Data
public class DictTypeDto {

    /**
     * 字典主键
     */
    @NotEmpty()
    @ApiParam(required = true,value = "注意是长整型")
    Long   dictId;
    /**
     * 字典名称
     */
    String dictName;
    /**
     * 字典类型
     */
    String dictType;
    /**
     * 状态
     */
    String status;



}
