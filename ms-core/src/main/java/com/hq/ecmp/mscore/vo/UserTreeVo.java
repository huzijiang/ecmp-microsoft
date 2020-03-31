package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("员工数模型")
public class UserTreeVo {

    @ApiModelProperty(value = "部门ID")
    private Long deptId;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户账号")
    private String userName;
    @ApiModelProperty(value =  "用户昵称")
    private String nickName;
    @ApiModelProperty(value =  "手机号码")
    private String phonenumber;
    @ApiModelProperty(value =  "用户性别 0=男,1=女,2=未知")
    private String sex;
    @ApiModelProperty(value = "是否司机 0否 1是")
    private String itIsDriver;
    @ApiModelProperty(value = "是否车队调度 0否 1是")
    private String itIsDispatcher;
    @ApiModelProperty(value= "工号")
    private String jobNumber;
}
