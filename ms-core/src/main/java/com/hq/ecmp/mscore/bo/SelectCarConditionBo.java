package com.hq.ecmp.mscore.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-21 10:47
 */
@Data
public class SelectCarConditionBo {

    /**
     * 调度员编号
     */
    private Long dispatcherId;

    /**
     * 车队编号
     */
    private String carGroupId;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 乘客数量
     */
    private int   passengers;


    /**
     * 车辆级别要求
     */
    private String carLevel;

    /**
     * 指定的司机编号
     */
    private String  driverId;

    /**
     * 车牌号
     */
    private String  carLicense;

    /**
     * 车型车系信息
     */
    private String  carTypeInfo;

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
    @ApiModelProperty(name = "itIsSelfDriver", value = "用车人是否自驾 Y000-是,N111-否")
    private String itIsSelfDriver;


}
