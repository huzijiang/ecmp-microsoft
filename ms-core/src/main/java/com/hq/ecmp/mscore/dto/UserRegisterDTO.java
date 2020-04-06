package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author
 * @date 2020-03-17
 */
@Data
public class UserRegisterDTO {
    @ApiParam(name = "invitationId", value = "邀请ID", required = true )
    private String invitationId;
    @ApiParam(name = "name", value = "姓名", required = true )
    private String name;
    @ApiParam(name = "gender", value = "性别", required = true )
    private String gender;
    @ApiParam(name = "jobNumber", value = "工号", required = true )
    private String jobNumber;
    @ApiParam(name = "email", value = "邮箱", required = true )
    private String email;
    @ApiParam(name = "mobile", value = "手机号", required = true )
    private String mobile;
    @ApiParam(name = "smsAuthCode", value = "短信验证码", required = true )
    private String smsAuthCode;
    @ApiParam(name = "reason", value = "申请原因", required = true )
    private String reason;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
