package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
*   @author yj
*   @date 15:36 2020/6/9
*   @Param  
*   @return 
**/
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

    @ApiModelProperty(value="出发地城市")
    private String startCity;

    @ApiModelProperty(value="目的地地城市")
    private String endCity;

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

    @ApiModelProperty(value="申请原因")
    private String reason;

    @ApiModelProperty(value="成本中心")
    private String costCenter;

    @ApiModelProperty(value="项目编号")
    private String projectName;

    @ApiModelProperty(value="以等待时间")
    private String waitingTime;

    @ApiModelProperty(value = "跳内部派车还是外部派车，1 内部  2 外部")
    private Integer inOrOut;

    @ApiModelProperty(value = "车队类型 C000   内部车队  C111   外部车队")
    private String  useCarGroupType;

    @ApiModelProperty(value = "乘车人电话")
    private  String userCarUserMobile;

    @ApiModelProperty(value = "包车总天数")
    private Double charterCarDaysCount;

    @ApiModelProperty(value = "历史包车天数")
    private String oldUseTime;

    @ApiModelProperty(value = "所用车型id")
    private Long carTypeId;

    @ApiModelProperty(value = "所用车型名字")
    private String carTypeName;

    @ApiModelProperty(value = "用车备注")
    private String notes;

    @ApiModelProperty(value = "服务模式")
    private String carGroupUserMode;

    @ApiModelProperty(value = "用车城市code")
    private String useCarCityCode;

    @ApiModelProperty(value = "是否自驾 Y000 是  N111 否")
    private String selfDriver;

    @ApiModelProperty(value = "内部调度选的外部车队id")
    private Long nextCarGroupId;

    @ApiModelProperty(value = "是否自驾 Y000 是  N111 否")
    private String itIsSelfDriver;

    @ApiModelProperty(value = "外部车队名称")
    private String carGroupName;

    @ApiModelProperty(value = "多个下车地址")
    private String addressInfo;
    /**
     * 是否安全提醒
     * Y000  是，安全提醒已勾选
     * N111  否，安全提醒未勾选
     */
    @ApiModelProperty(value = "是否安全提醒\n" +
            "      Y000  是，安全提醒已勾选\n" +
            "      N111  否，安全提醒未勾选")
    private String safeRemind;

    /**
     * 排序依赖字段
     */
    private String orderByState;

    /**
     * 制度id
     */
    private Long regimeId;
    /**
     * 行程id
     */
    private Long journeyId;
    /**
     * 订单的申请人id
     */
    private Long userId;

    /**
     * 内部调度员的 姓名和电话，用逗号分隔
     */
    private String innerDisNameAndPhone;
    /**
     * 订单号
     */
    private String orderNum;
}
