package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 23:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarGroupListVO {



    //1
    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;

    //2
    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;


    //5
    @ApiModelProperty(name = "ownerOrg", value = "所属组织",example = "北京分公司id")
    private Integer ownerOrg;

   //6
   @ApiModelProperty(name = "countCar", value = "服务城市代码")
   private String city;

    //7
    @ApiModelProperty(name = "countCar", value = "下级车队数")
    private Integer countSubCarGroup;

    //8
    @ApiModelProperty(name = "state", value = "车队状态")
    private String state;

    //9
    @ApiModelProperty(name = "carGroupId", value = "车队id")
    private Long carGroupId;

    //3
    @ApiModelProperty(name = "leaderNames", value = "车队主管（调度员）名字")
    private List<String> leaderNames;

    //4
    @ApiModelProperty(name = "countDriver", value = "车队人数（驾驶员 加 调度员） ")
    private Integer countMember;

    @ApiModelProperty(value = "所属公司名称")
    private String ownerOrgName;

    @ApiModelProperty(value = "当前车队的车辆数")
    private  Integer carNum;


    @ApiModelProperty(name = "ownerCompanyName", value = "所属公司名字")
    private String ownerCompanyName;


    @ApiModelProperty(name = "ownerCompany", value = "所属公司id")
    private Long ownerCompany;

    @ApiModelProperty(name = "cityName", value = "服务城市名字")
    private String cityName;
    @ApiModelProperty(name = "cityName", value = "服务城市名字")
    private String cityCode;

    @ApiModelProperty(name = "itIsInner", value = "是否是内部车队   C000   内部车队   C111   外部车队")
    private String itIsInner;
}
