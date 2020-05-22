package com.hq.ecmp.constant.enumerate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 车辆状态枚举
 * @Author: zj.hu
 * @Date: 2020-03-23 11:38
 */
public enum  CarStateEnum {
    /**
     *车况良好
     */
    EFFECTIVE("S000","可派"),
    /**
     *禁用中
     */
    NONEFFECTIVE("S001","禁用中"),
    /**
     *维护中
     */
    MAINTENANCE("S002","维护中"),
    /**
     *车辆已到期
     */
    EXPIRE("S003","车辆已到期"),
    /**
     *被借调
     */
    WAS_BORROWED("S101","被借调");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarStateEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }


}
