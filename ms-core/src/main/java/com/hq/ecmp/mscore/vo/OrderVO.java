package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "待服务详情")
public class OrderVO {

    private Long orderId;
    @ApiModelProperty(name = "driverMobile",value = "司机手机")
    private String driverMobile;
    @ApiModelProperty(name = "driverName",value = "司机姓名")
    private String driverName;
    @ApiModelProperty(name = "driverScore",value = "司机评分")
    private String driverScore;
    @ApiModelProperty(name = "carColor",value = "车辆颜色")
    private String carColor;
    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;
    @ApiModelProperty(name = "powerType",value = "车辆动力类型")
    private String powerType;
    @ApiModelProperty(name = "carType",value = "车辆类型")
    private String carType;
    @ApiModelProperty(name = "hint",value = "提示语")
    private String hint;
    @ApiModelProperty(name = "state",value = "订单状态")
    private String state;
    @ApiModelProperty(name = "carGroupPhone",value = "车队电话")
    private String carGroupPhone;
    @ApiModelProperty(name = "isAddContact",value = "是否添加联系人(1是0否)")
    private String isAddContact;
    @ApiModelProperty(name = "driverType",value = "司机类型(企业驾驶员/网约驾驶员)")
    private String driverType;
    @ApiModelProperty(name = "customerServicePhone",value = "客服电话")
    private String customerServicePhone;
    @ApiModelProperty(name = "useCarTime",value = "用车时间")
    private String useCarTime;
    @ApiModelProperty(name = "labelState",value = "辅助状态")
    private String labelState;

}
