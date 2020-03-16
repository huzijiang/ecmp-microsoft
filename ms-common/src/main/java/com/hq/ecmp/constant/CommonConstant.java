package com.hq.ecmp.constant;

/**
 * @ClassName CommonConstant
 * @Description TODO
 * @Author yj
 * @Date 2020/3/9 15:24
 * @Version 1.0
 */
public final class CommonConstant {

    //redis中约车次数的前缀
    public  static final String APPOINTMENT_NUMBER_PREFIX = "APPOINTMENT_NUMBER_";
    //司机等待的开启和关闭
    //开启
    public static final  String  START = "1";
    //关闭
    public static final  String  FINISH = "2";

    //联系乘客或者联系车队，对应的电话角色名
    public static final String PASSENGER_ROLE = "乘车人";
    public static final String DISPATCHER_ROLE = "调度员";
    public static final String CARGROUP_PHONE_ROLE = "车队座机";

    //订单途径地信息表，标识是否通过此途径地
    public static final String  PASS = "Y000";
    public static final String NO_PASS = "N111";

    //标识是否往返
    public static final String IS_RETURN = "Y000";
    public static final String IS_NOT_RETURN = "N444";

    //标识行程节点是否有效
    //有效节点
    public static final String VALID_NODE = "P000";
    //无效节点
    public static final String INVALID_NODE = "P444";

    //是否是同行人
    //00   是        01   否(即乘车人)
    public static final String IS_PEER = "00";
    public static final String IS_NOT_PEER = "01";

    //公务申请
    public static final String AFFICIAL_APPLY = "A001";
    //差旅申请
    public static final String TRAVLE_APPLY = "A002";

    /**
     * 开关状态开
     */
    public static final String SWITCH_ON = "0";
    /**
     * 开关状态关
     */
    public static final String SWITCH_OFF = "1";
    /**
     * 开关状态开：自定义
     */
    public static final String SWITCH_ON_CUSTOM = "2";



}
