package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "驾驶员注册详情")
public class RegisterDriverDetailVO {

    private String registerId;
    @ApiModelProperty(name = "userName",value = "姓名")
    private String userName;
    @ApiModelProperty(name = "mobilePhone",value = "手机号")
    private String mobilePhone;
    @ApiModelProperty(name = "jobNumber",value = "工号")
    private String jobNumber;
    @ApiModelProperty(name = "companyName",value = "所属公司")
    private String companyName;
    @ApiModelProperty(name = "carGroupName",value = "所属车队")
    private String carGroupName;
    @ApiParam(name = "idCard", value = "身份证", required = true )
    private String idCard;
    @ApiParam(name = "licenseType", value = "驾驶证类型", required = true )
    private String licenseType;
    @ApiParam(name = "licenseNumber", value = "驾驶证号码", required = true )
    private String licenseNumber;
    @ApiParam(name = "licenseInitIssueDate", value = "初次领证日期", required = false)
    private Date licenseInitIssueDate;
    @ApiParam(name = "licenseIssueDate", value = "证照有效期启期", required = false )
    private Date licenseIssueDate;
    @ApiParam(name = "licenseExpireDate", value = "证照有效期止期", required = false )
    private Date licenseExpireDate;
    @ApiParam(name = "licenseImages", value = "证照图片地址", required = false )
    private String licenseImages;
    @ApiModelProperty(name = "reason",value = "申请原因")
    private String reason;




}
