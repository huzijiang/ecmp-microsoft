package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zj.hu
 * @Date: 2020-03-23 11:38
 */
public enum CarLockStateEnum {

    UNLOCK("0000","未锁定"),
    LOCKED("1111","已锁定");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    CarLockStateEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
