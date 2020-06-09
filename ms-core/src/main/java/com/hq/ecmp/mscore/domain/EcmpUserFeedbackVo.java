package com.hq.ecmp.mscore.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "投诉建议")
public class EcmpUserFeedbackVo {



    @ApiModelProperty(value="主键id")
    private  Long feedbackId;

    @ApiModelProperty(value="用车单位")
    private String ecmpName;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="提交时间")
    private Date createDate;

    @ApiModelProperty(value="回复时间")
    private Date replyDate;

    @ApiModelProperty(value="状态")
    private String status;

    @ApiModelProperty(value="建议内容")
    private String content;




}
