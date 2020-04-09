package com.hq.ecmp.constant;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/13 18:17
 */
public final class ApplyStateConstant {

    //申请审批状态
    //S001  申请中
    //S002  通过
    //S003  驳回
    //S004  已撤销
    //S005  已过期
    public static final String ON_APPLYING = "S001";
    public static final String APPLY_PASS = "S002";
    public static final String REJECT_APPLY = "S003";
    public static final String CANCEL_APPLY = "S004";
    public static final String EXPIRED_APPLY = "S005";
}
