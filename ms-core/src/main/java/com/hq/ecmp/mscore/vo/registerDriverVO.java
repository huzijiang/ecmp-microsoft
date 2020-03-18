package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "待审核申请驾驶员")
public class registerDriverVO {

    /**
     * 姓名
     */
    private String userName;
    /**
     *手机号
     */
    private String mobilePhone;
    /**
     * 工号
     */
    private String jobNumber;
    /**
     * 所属公司
     */
    private String companyName;
    /**
     * 所属车队
     */
    private String carGroupName;
    /**
     * 申请原因
     */
    private String reason;



}
