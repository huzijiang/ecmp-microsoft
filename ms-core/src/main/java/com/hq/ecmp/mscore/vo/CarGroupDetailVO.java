package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/13 14:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupDetailVO {


    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;

    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;

    @ApiModelProperty(name = "ownerOrg", value = "所属组织id",example = "北京分公司id")
    private Long ownerOrg;

    @ApiModelProperty(name = "ownerOrg", value = "所属组织名字",example = "北京分公司")
    private String ownerOrgName;

    @ApiModelProperty(name = "cityName", value = "归属城市名字")
    private String cityName;


    @ApiModelProperty(name = "dispatchers", value = "调度员列表")
    private List<UserVO> dispatchers;


    @ApiModelProperty(name = "fullAddress", value = "详细地址",example = "北京市通州区荣京东街28号")
    private String fullAddress;


    @ApiModelProperty(name = "shortAddress", value = "车队地址 短地址",example = "亦城财富中心A座")
    private String shortAddress;

    @ApiModelProperty(name = "countDriver", value = "车队人数")
    private Integer countDriver;

    @ApiModelProperty(name = "itIsInner", value = "是否是内部车队   C000   内部车队   C111   外部车队")
    private String itIsInner;
}
