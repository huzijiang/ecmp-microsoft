package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderListInfo
 * @Description TODO 乘客，我的行程订单列表vo模型
 * @Author yj
 * @Date 2020/3/4 10:22
 * @Version 1.0
 */
@Data
public class OrderListInfo {

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 车型级别名称  公务级//行政级
     */
    private String level;
    /**
     *  用车方式  自由车 /网约车
     */
    private String useCarMode;
    /**
     *   订单状态
     */
    private String state;
    /**
     *    服务类型// 1000预约//2001接机//2002送机//3000包车
     */
    private String serviceType;
    /**
     *     出发日期
     */
    private String setOutTime;
    /**
     *      出发地址
     */
    private String setOutAddress;
    /**
     *    到达地
     */
    private String arriveAddress;
    /**
     *    订单编号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNumber;
    /**
     *    轨迹表状态
     */
    private String labelState;

    /**
     *   N000正常单子  Y000是补单  D000是直接调度
     */
    private String itIsSupplement;

    /**
     * 支付状态
     */
    private String payState;

    @ApiModelProperty(value = "车型")
    private String carTypeName;

    @ApiModelProperty(value = "用车天数")
    private Integer useTime;

    @ApiModelProperty(value = "是否自驾 \n" +
            "Y000  是\n" +
            "N111  否")
    private String  selfDriver;

    @ApiModelProperty(value = "用车人")
    private String useCarName;

    @ApiModelProperty(value = "用车人手机号")
    private String useCarMobile;

    @ApiModelProperty(value = "用车时间")
    private  String useCarDate;

    @ApiModelProperty(value = "出发地")
    private String beginAddress;
}