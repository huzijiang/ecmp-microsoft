package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 车队来源内外属性枚举
 * @auther: zj.hu
 * @date: 2020-05-26
 */
public enum CarGroupSourceEnum {

    /**
     *内部车队
     */
    INNER_CAR_GROUP("C000","内部车队"),

    /**
     *外部车队
     */
    OUTER_CAR_GROUP("C111","外部车队");


    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarGroupSourceEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}



