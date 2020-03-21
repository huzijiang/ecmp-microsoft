package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hq.core.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("员工管理vo(传入后端)")
public class EcmpUserVo {

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "临时字段，员工昵称或手机号")
    private String nickNameOrPhone;

    @ApiModelProperty(value = "用户ID")
    private Long userId;


    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value =  "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型 0=0系统用户")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value =  "手机号码")
    private String phonenumber;

    @ApiModelProperty(value =  "用户性别 0=男,1=女,2=未知")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "帐号状态 0=正常,1=停用")
    private String status;

    @ApiModelProperty(value ="删除标志（0代表存在 2代表删除")
    private String delFlag;

    @ApiModelProperty(value ="最后登陆IP")
    private String loginIp;

    @ApiModelProperty(value  = "最后登陆时间 yyyy-MM-dd")
    private Date loginDate;

    @ApiModelProperty(value = "是否司机 0否 1是")
    private String itIsDriver;

    @ApiModelProperty(value = "是否车队调度 0否 1是")
    private String itIsDispatcher;

    @ApiModelProperty(value = "创建者")
    private String createBy;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新者")
    private String updateBy;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "输入的新手机号")
    private String newPhoneNum;

    @ApiModelProperty(value = "再次输入的手机号")
    private String reWritePhone;

    @ApiModelProperty(value= "驻地代码")
    private String stationCode;

    @ApiModelProperty(value= "离职日期")
    private Date dimissionTime;

    @ApiModelProperty(value= "企业支付")
    private String payMode;

    @ApiModelProperty(value= "备注")
    private String remark;

    @ApiModelProperty(value= "工号")
    private String jobNumber;

    @ApiModelProperty(value= "临时字段，选中的单个/多个角色id")
    private Long[]  roleIds;

    @ApiModelProperty(value= "当天日期 yyyy-MM-dd")
    private String  dateOfTheDay;

    @ApiModelProperty(value= "用车制度id集合")
    private List<Long> regimenId;

    @ApiModelProperty(value= "用户拥有的角色名称")
    private List<String> roleName;

    @ApiModelProperty(value= "归属部门")
    private String subDept;

    @ApiModelProperty(value= "归属公司")
    private String subCompany;

}
