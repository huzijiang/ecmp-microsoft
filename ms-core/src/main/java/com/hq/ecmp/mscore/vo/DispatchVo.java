package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="调度管理")
public class DispatchVo {

    @ApiModelProperty(value="订单编号")
    private Long orderId;

    @ApiModelProperty(value="申请人所属公司")
    private String companyName;

    @ApiModelProperty(value="申请人所属部门")
    private String deptName;

    @ApiModelProperty(value="申请人姓名")
    private String applyUserName;

    @ApiModelProperty(value="申请人手机号")
    private String applyUserTel;

    @ApiModelProperty(value="乘车人")
    private String useCarUserName;

    @ApiModelProperty(value="同行人数量")
    private Long travelPartnerCount;

    @ApiModelProperty(name="travelPartnerCount",value="同行人")
    private List<String> travelPartnerList;

    @ApiModelProperty(value="开始时间")
    private Date useCarDate;

    @ApiModelProperty(value="预计结束时间")
    private Date endDate;

    @ApiModelProperty(value="上车地址")
    private String startSite;

    @ApiModelProperty(value="下车地址")
    private String endSite;

    @ApiModelProperty(value="是否往返 Y000-是        N444 -否")
    private String itIsReturn;

    @ApiModelProperty(value="预计等待时长")
    private Long waitMinute;

    @ApiModelProperty(value="用车城市")
    private String useCarCity;

    @ApiModelProperty(value="用车场景")
    private String userCarScene;

    @ApiModelProperty(value="用车制度")
    private String userCarRegime;

    @ApiModelProperty(value="服务类型")
    private String serverType;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="操作时间")
    private Date opDate;

    @ApiModelProperty(value="操作权限  0：有权限  1:没有权限")
    private String  operationPermission;
}
