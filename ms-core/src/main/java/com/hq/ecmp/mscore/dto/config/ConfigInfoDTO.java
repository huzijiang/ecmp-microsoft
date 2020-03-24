package com.hq.ecmp.mscore.dto.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xueyong
 * @date 2020/3/16
 * ecmp-microservice.
 */
@Data
public class ConfigInfoDTO implements Serializable {

    /**
     * 基本信息配置
     */
    @ApiModelProperty(name = "baseInfo", value = "基本信息配置")
    private EnterPriseBaseInfoDTO baseInfo;

    /**
     * 背景图配置
     */
    @ApiModelProperty(name = "backgroundImageInfo", value = "背景图配置")
    private ConfigValueDTO backgroundImageInfo;

    @ApiModelProperty(name = "messageInfo", value = "公告配置")
    private ConfigValueDTO messageInfo;

    @ApiModelProperty(name = "welcomeImageInfo", value = "开屏图配置")
    private ConfigValueDTO welcomeImageInfo;

    @ApiModelProperty(name = "smsInfo", value = "短信配置")
    private ConfigValueDTO smsInfo;

    @ApiModelProperty(name = "virtualPhoneInfo", value = "虚拟小号配置")
    private ConfigValueDTO virtualPhoneInfo;

    @ApiModelProperty(name = "orderConfirmInfo", value = "确认订单方式配置")
    private OrderConfirmSetting orderConfirmInfo;

    @ApiModelProperty(name = "dispatchInfo", value = "自动派单方式配置")
    private ConfigAutoDispatchDTO dispatchInfo;

    @ApiModelProperty(name = "waitMaxMinute", value = "往返用车等待时长配置")
    private ConfigValueDTO waitMaxMinute;
}
