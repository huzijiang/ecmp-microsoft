package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 10:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarGroupDTO {


    @ApiModelProperty(name = "carGroupId", value = "车队id",required = false)
    private Long carGroupId;

    @ApiModelProperty(name = "userIdS", value = "主管id数组",required = false)
    private Long[]  userIds;

    @ApiModelProperty(name = "parentCarGroupId", value = "父车队id",required = false)
    private Long parentCarGroupId;

    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;

    @ApiModelProperty(name = "carGroupCode", value = "车队编号")
    private String carGroupCode;

    @ApiModelProperty(name = "ownerOrg", value = "所属组织 分子公司id/或部门id/或父车队id")
    private Long ownerOrg;



    @ApiModelProperty(name = "city", value = "归属城市编码")
    private String city;

    @ApiModelProperty(name = "owneCompany", value = "归属城市编码")
    private  Long owneCompany;

  //  @ApiModelProperty(name = "cityName", value = "归属城市名字")
  // private String cityName;



    @ApiModelProperty(name = "leader", value = "车队负责人")
    private Long leader;  // TODO 冗余字段  暂时不用管它   业务中没出现  调度员下面是单独存dispatchers的


    @ApiModelProperty(name = "shortAddress", value = "车队地址 短地址",example = "亦城财富中心A座")
    private String shortAddress;

    @ApiModelProperty(name = "fullAddress", value = "详细地址",example = "北京市通州区荣京东街28号")
    private String fullAddress;

    @ApiModelProperty(name = "province", value = "所属省份代码：110000  北京",example = "110000")
    private String province;

    @ApiModelProperty(name = "longitude", value = "驻地经度")
    private String longitude;

    @ApiModelProperty(name = "latitude", value = "驻地纬度")
    private String latitude;

    @ApiModelProperty(name = "telephone", value = "车队座机")
    private String telephone;

    @ApiModelProperty(name = "dispatchers", value = "调度员列表")
    private List<UserVO> dispatchers;

    @ApiModelProperty(name = "companyId", value = "所属公司id")
    private Long companyId;

    @ApiModelProperty(name = "ownerOrgName", value = "所属组织 名称",required = false)
    private String ownerOrgName;  //回显使用

    @ApiModelProperty(name = "cityName", value = "归属城市名称")
    private String cityName;

    @ApiModelProperty(name = "deptIds", value = "服务部门id集合")
    private Long[] deptIds;

    @ApiModelProperty(name = "companyIds", value = "允许调度的外部公司id集合")
    private Long[] companyIds;

    @ApiModelProperty(name = "itIsInner", value = "是否是内部车队   C000   内部车队   C111   外部车队")
    private String itIsInner;


}
