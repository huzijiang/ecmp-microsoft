package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:11
 */
@Data
public class DispatchLockDriverDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    private String dispatcherId;
    /**
     * 申请单 编号
     */
    private String applyId;
    /**
     * 订单 编号
     */
    private String orderNo;

    /**
     * 车辆编号
     */
    private String carId;

    /**
     * 驾驶员 编号
     */
    private String driverId;


    /**
     *
     * 包车时长
     *
     */
    @ApiModelProperty(name = "rentTime", value = "包车时长: 具体的天数 0.5-5.0")
    private String rentTime;

    /**
     * 针对包车业务
     * 车队服务模式
     *      * CA00  车和驾驶员都用
     *      * CA01  只用车
     *      * CA10  只用驾驶员
     *      * CA11  车和司机都不用
     */
    @ApiModelProperty(name = "carGroupServiceMode", value = "车队服务模式 CA00-车和驾驶员都用，CA01-只用车，CA10-只用驾驶员,CA11-车和司机都不用  ")
    private String carGroupServiceMode;

    /**
     *
     * 针对包车业务
     * 车队内外属性
     * C000   内部车队
     * C111   外部车队
     */
    @ApiModelProperty(name = "carGroupSource", value = "车队内外属性 C000-内部车队,C111-外部车队 ")
    private String carGroupSource;

    /**
     * 针对包车业务
     * 用车人是否自驾
     */
    @ApiModelProperty(name = "itIsSelfDriver", value = "用车人是否自驾 Y000-内部车队,N111-外部车队")
    private String itIsSelfDriver;


}
