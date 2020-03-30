package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zj.hu
 * @Date: 2020-03-30 09:16
 */
public enum DriverLockStateEnum {

    UNLOCK("0000","未锁定"),
    LOCKED("1111","已锁定");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    DriverLockStateEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
