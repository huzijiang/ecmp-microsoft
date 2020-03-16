package com.hq.ecmp.constant;

/**
 * @author xueyong
 */

public enum ConfigTypeEnum {

    BASE_INFO("BASE_INFO", "baseInfo"),
    BACKGROUND_IMAGE_INFO("BACKGROUND_IMAGE_INFO", "backgroundImageInfo"),
    MESSAGE_INFO("MESSAGE_INFO", "messageInfo"),
    WELCOME_IMAGE_INFO("WELCOME_IMAGE_INFO", "welcomeImageInfo"),
    SMS_INFO("SMS_INFO", "smsInfo"),
    VIRTUAL_PHONE_INFO("VIRTUAL_PHONE_INFO", "virtualPhoneInfo"),
    ORDER_CONFIRM_INFO("ORDER_CONFIRM_INFO", "orderConfirmInfo"),
    DISPATCH_INFO("DISPATCH_INFO", "dispatchInfo"),
    WAIT_MAX_MINUTE("WAIT_MAX_MINUTE", "waitMaxMinute"),
    ;

    private String configType;
    private String configKey;

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    ConfigTypeEnum(String configType, String configKey) {
        this.configType = configType;
        this.configKey = configKey;
    }

}
