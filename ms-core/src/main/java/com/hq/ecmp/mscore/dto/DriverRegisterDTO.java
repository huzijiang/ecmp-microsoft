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
public class DriverRegisterDTO {
    @ApiParam(name = "name", value = "姓名", required = true )
    private String name;
    @ApiParam(name = "gender", value = "性别", required = true )
    private String gender;
    @ApiParam(name = "jobNumber", value = "工号", required = false )
    private String jobNumber;
    @ApiParam(name = "carGroupId", value = "车队", required = true )
    private String carGroupId;
    @ApiParam(name = "idCard", value = "身份证", required = true )
    private String idCard;
    @ApiParam(name = "licenseType", value = "驾驶证类型", required = true )
    private String licenseType;
    @ApiParam(name = "licenseNumber", value = "驾驶证号码", required = true )
    private String licenseNumber;
    @ApiParam(name = "licenseInitIssueDate", value = "初次领证日期", required = false)
    private String licenseInitIssueDate;
    @ApiParam(name = "licenseIssueDate", value = "证照有效期启期", required = true )
    private String licenseIssueDate;
    @ApiParam(name = "licenseExpireDate", value = "证照有效期止期", required = true )
    private String licenseExpireDate;
    @ApiParam(name = "licenseImages", value = "证照图片地址", required = false )
    private String licenseImages;
    @ApiParam(name = "mobile", value = "手机号", required = true )
    private String mobile;
    @ApiParam(name = "smsAuthCode", value = "短信验证码", required = true )
    private String smsAuthCode;
    @ApiParam(name = "reason", value = "申请原因", required = true )
    private String reason;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
