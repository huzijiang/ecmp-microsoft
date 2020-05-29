package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * @auther: zj.hu
 * @date: 2020-05-29
 */
public enum CarRentTypeEnum {

    FOUR_HOURS("T001","半日租"),
    EIGHT_HOURS("T002","整日租"),
    MORE_HOURS("T009","多日租");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarRentTypeEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
