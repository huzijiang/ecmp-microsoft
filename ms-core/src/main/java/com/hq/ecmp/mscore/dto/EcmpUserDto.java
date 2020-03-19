package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("部门员工dto(返回前端)")
public class EcmpUserDto {

    @ApiModelProperty(value="用户ID")
    private Long userId;

    /** 部门ID */
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value="用户账号")
    private String userName;

    @ApiModelProperty(value="用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型,0=0系统用户")
    private String userType;

    @ApiModelProperty(value= "用户邮箱")
    private String email;

    @ApiModelProperty(value="手机号码")
    private String phonenumber;

    @ApiModelProperty(value= "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value="头像地址")
    private String avatar;

    @ApiModelProperty(value= "密码")
    private String password;

    @ApiModelProperty(value="帐号状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value= "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty(value= "最后登陆IP")
    private String loginIp;

    @ApiModelProperty(value= "最后登陆时间,width = 30, dateFormat = \"yyyy-MM-dd\"")
    private Date loginDate;

    @ApiModelProperty(value= "是否司机(0否 1是)")
    private String itIsDriver;

    @ApiModelProperty(value= "是否车队调度(0否 1是)")
    private String itIsDispatcher;

    @ApiModelProperty(value= "驻地代码")
    private String stationCode;
    @ApiModelProperty(value= "离职日期")
    private Date dimissionTime;
}
