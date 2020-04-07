package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 没有值 通用枚举
 *
 * @Author: zj.hu
 * @Date: 2020-04-07 15:22
 */
public enum NoValueCommonEnum {

    NO_STRING("-","字符串无值"),
    NO_NUMBER("0","无数字值");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    NoValueCommonEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
