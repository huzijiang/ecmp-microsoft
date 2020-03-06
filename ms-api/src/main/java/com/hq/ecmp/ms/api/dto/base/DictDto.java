package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: shixin
 * @Date: 2020-03-04 15:26
 */
@Data
public class DictDto {
    /**
     * 字典编码
     */
    @NotEmpty()
    @ApiParam(required = true,value = "注意是长整型")
    Long   dictCode;
    /**
     * 字典排序
     */
    Integer dictSort;
    /**
     * 字典标签
     */
    String dictLabel;
    /**
     * 字典键值
     */
    String dictValue;
    /**
     * 字典类型
     */
    String dictType;
    /**
     * 样式属性
     */
    String cssClass;
    /**
     * 表格回显样式
     */
    String listClass;
    /**
     * 是否默认（Y是 N否）
     */
    String isDefault;
    /**
     * 状态（0正常 1停用）
     */
    String status;
}
