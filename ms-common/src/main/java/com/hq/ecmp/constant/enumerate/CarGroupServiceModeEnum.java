package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 车队服务使用模式枚举
 * @auther: zj.hu
 * @date: 2020-05-26
 */
public enum CarGroupServiceModeEnum {

    /**
     *车和驾驶员都用
     */
    USE_CAR_AND_DRIVER("CA00","车和驾驶员都用"),

    /**
     *只用车
     */
    USE_CAR("CA01","只用车"),

    /**
     *只用驾驶员
     */
    USE_DRIVER("CA10","只用驾驶员"),
    /**
     *车和司机都不用
     */
    DOT_USE("CA11","车和司机都不用");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarGroupServiceModeEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
