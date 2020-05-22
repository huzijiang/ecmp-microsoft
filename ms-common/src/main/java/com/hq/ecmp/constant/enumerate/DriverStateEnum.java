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
     * 等待启用
     */
    WAIT_EFFECTIVE("W009","待启用"),

    /**
     * 司机已经离职或者失效
     */
    DIMISSION("NV00","已离职/失效"),
    /**
     * 病假
     */
    SICK_LEAVE("X000","病假"),
    /**
     * 年假
     */
    ANNUAL_LEAVE("X002","年假"),
    /**
     * 公休
     */
    WEEKEND("X003","公休"),
    /**
     * 正常值班
     */
    DUTY("X999","正常值班");

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
