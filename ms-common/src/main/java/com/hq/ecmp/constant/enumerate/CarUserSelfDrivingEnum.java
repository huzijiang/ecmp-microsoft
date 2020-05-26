package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 用车人是否 自驾枚举
 * @auther: zj.hu
 * @date: 2020-05-26
 */
public enum CarUserSelfDrivingEnum {

    /**
     *自驾
     */
    ONE_SELF("Y000","用户自驾"),

    /**
     *外部车队
     */
    FULL_TIME_DRIVER("N111","使用全职司机");


    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarUserSelfDrivingEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
