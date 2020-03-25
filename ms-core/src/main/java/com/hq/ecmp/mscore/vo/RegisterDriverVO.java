package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "待审核申请驾驶员")
public class RegisterDriverVO {


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
    @ApiModelProperty(name = "reason",value = "申请原因")
    private String reason;



}
