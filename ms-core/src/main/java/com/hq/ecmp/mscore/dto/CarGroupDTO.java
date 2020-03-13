package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 10:30
 */
@Data
public class CarGroupDTO {


    /**
     * 城市编码
     */
    @ApiModelProperty(name = "city", value = "归属城市编码")
    private Long city;

    @ApiModelProperty(name = "cityName", value = "归属城市名字")
    private String cityName;

    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;   //TODO 新增

    @ApiModelProperty(name = "ownerOrg", value = "所属组织",example = "北京分公司")
    private Long ownerOrg;

    @ApiModelProperty(name = "leader", value = "车队负责人")
    private Long leader;

    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;   //TODO 新增

    @ApiModelProperty(name = "shortAddress", value = "车队地址 短地址",example = "亦城财富中心A座")
    private String shortAddress;   //TODO 新增

    @ApiModelProperty(name = "fullAddress", value = "详细地址",example = "北京市通州区荣京东街28号")
    private String fullAddress;   //TODO 新增

}
