package com.hq.ecmp.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: chao.zhang
 * @Date: 2020/5/21 15:00
 */
@AllArgsConstructor
public enum DriverNatureEnum {

    OWNER_DRIVER("Z000","合同制(自有)驾驶员"),
    IN_SYSTEM_DRIVER("Z001","在编驾驶员"),
    HIRED_DRIVER("Z002","外聘驾驶员"),
    BORROWED_DRIVER("Z003","借调的驾驶员");

    @Setter
    @Getter
    private String key;
    @Setter
    @Getter
    private String desc;


}
