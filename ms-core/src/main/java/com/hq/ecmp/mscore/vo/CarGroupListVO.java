package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 23:41
 */
@Data
public class CarGroupListVO {

    @ApiModelProperty(name = "carGroupId", value = "车队id")
    private Long carGroupId;

    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;

    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;

    @ApiModelProperty(name = "ownerOrg", value = "所属组织",example = "北京分公司")
    private Long ownerOrg;

    @ApiModelProperty(name = "countCar", value = "车队车辆数")
    private Integer countCar;

    @ApiModelProperty(name = "countDriver", value = "车队驾驶员人数")
    private Integer countDriver;

    @ApiModelProperty(name = "state", value = "车队状态")
    private Integer state;

}
