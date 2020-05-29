package com.hq.ecmp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crk
 */
@Getter
@AllArgsConstructor
public enum  AccountConstant {
    APPROVE_T001("S000","待财务审核"),
    APPROVE_T002("S001","财务已审核"),
    APPROVE_T003("S008","未开票"),
    APPROVE_T004("S009","已开票"),
    ;

    private String key;
    private String desc;//描述
}
