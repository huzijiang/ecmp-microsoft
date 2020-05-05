package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/5/5 14:47
 */
@Data
public class CompanyDto {

    @ApiModelProperty(value = "公司树起点公司id",required = false)
    private Long deptId;

    @ApiModelProperty(value = "本公司id")
    private Long companyId;
}
