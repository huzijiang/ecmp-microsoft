package com.hq.ecmp.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/7 19:31
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum  NeedApproveEnum {

    NEED_APPROVE("Y000","需要审批"),
    NEED_NOT_APPROVE("N111","不需要审批")
    ;

    private String key;
    private String desc;

}
