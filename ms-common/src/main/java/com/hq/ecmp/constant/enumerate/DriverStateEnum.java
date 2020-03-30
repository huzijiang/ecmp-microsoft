package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @Author: zj.hu
 * @Date: 2020-03-30 09:16
 */
public enum DriverStateEnum {

    /**
     * 可用
     */
    EFFECTIVE("V000","生效中"),
    /**
     * 等待审核
     */
    WAIT_CHECK("W001","待审核"),
    /**
     * 司机已经离职或者失效
     */
    DIMISSION("NV00","已离职/失效");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    DriverStateEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
