package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.dto.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "订单异议")
public class EcmpUserFeedbackInfoVo extends PageRequest {

    //feedbackId  主键id
    @ApiModelProperty(value="主键id")
    private  Long feedbackId;

    //用户id
    @ApiModelProperty(value="用户id")
    private  Long userId;

    //用户名称
    @ApiModelProperty(value="用户姓名")
    private  String nickName;

    //用户手机号
    @ApiModelProperty(value="用户手机号")
    private  String phone;

    //订单id
    @ApiModelProperty(value="订单id")
    private Long orderId;

    //订单服务类型
    @ApiModelProperty(value="订单服务类型")
    private String  serviceType;

    //订单用车方式
    @ApiModelProperty(value="订单用车方式")
    private String  useCarMode;

    //回复状态
    @ApiModelProperty(value="回复状态")
    private String state;

    //异议类型
    @ApiModelProperty(value="异议类型")
    private String type;

    //提交内容
    @ApiModelProperty(value="提交内容")
    private String content;

    //回复内容
    @ApiModelProperty(value="回复内容")
    private String result;

    //图片
    @ApiModelProperty(value="图片")
    private List<String> imageUrl;

    //反馈人
    @ApiModelProperty(value="反馈人")
    private Long createBy;

    //反馈时间
    @ApiModelProperty(value="反馈时间")
    private Date createTime;

    //回复人
    @ApiModelProperty(value="回复人")
    private Long updateBy;

    //回复时间
    @ApiModelProperty(value="回复时间")
    private Date updateTime;
}
