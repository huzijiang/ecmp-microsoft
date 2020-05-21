package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zj.hu
 * @Date: 2020-05-15 18:02
 */
public enum CarLevelMatchEnum {

    MATCH("0000",""),
    UN_MATCH("1111","跨级调度");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarLevelMatchEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
