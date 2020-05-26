package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:04
 */
@Data
public class DispatchSelectCarDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    private String dispatcherId;

    /**
     * 订单 编号
     */
    private String orderNo;

    /**
     * 车型级别代码,不传默认全部
     *
     * P001-公务级
     * P002-行政级
     * P003-六座商务
     */
    private String carModelLevelType;

    /**
     * 车牌号
     */
    private String plateLicence;

    /**
     * 驾驶员 编号
     */
    private String driverId;

    /**
     * 车型信息
     *   包含品牌、车系
     */
    private String carTypeInfo;


    /**
     * 针对包车业务
     * 车队服务模式
     *      * CA00  车和驾驶员都用
     *      * CA01  只用车
     *      * CA10  只用驾驶员
     *      * CA11  车和司机都不用
     *
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
