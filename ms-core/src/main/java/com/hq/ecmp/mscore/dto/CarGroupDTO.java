package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 10:30
 */
@Data
public class CarGroupDTO {


    @ApiModelProperty(name = "carGroupId", value = "车队id",required = false)
    private Long carGroupId;

    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;   //TODO 新增

    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;   //TODO 新增

    @ApiModelProperty(name = "ownerOrg", value = "所属组织",example = "北京分公司")
    private Long ownerOrg;

    @ApiModelProperty(name = "city", value = "归属城市编码")
    private String city;

  //  @ApiModelProperty(name = "cityName", value = "归属城市名字")
  // private String cityName;



    @ApiModelProperty(name = "leader", value = "车队负责人")
    private Long leader;  // TODO 冗余字段  暂时不用管它   业务中没出现  调度员下面是单独存dispatchers的


    @ApiModelProperty(name = "shortAddress", value = "车队地址 短地址",example = "亦城财富中心A座")
    private String shortAddress;   //TODO 新增

    @ApiModelProperty(name = "fullAddress", value = "详细地址",example = "北京市通州区荣京东街28号")
    private String fullAddress;   //TODO 新增

    @ApiModelProperty(name = "province", value = "所属省份代码：110000  北京",example = "110000")
    private String province;   //TODO 新增

    @ApiModelProperty(name = "longitude", value = "驻地经度")
    private String longitude;   //TODO 新增

    @ApiModelProperty(name = "latitude", value = "驻地纬度")
    private String latitude;   //TODO 新增

    @ApiModelProperty(name = "telephone", value = "车队座机")
    private String telephone;   //TODO 新增

    @ApiModelProperty(name = "dispatchers", value = "调度员列表")
    private List<UserVO> dispatchers;

    @ApiModelProperty(name = "owneCompany", value = "所属公司id")
    private Long owneCompany;   //TODO 新增

}
