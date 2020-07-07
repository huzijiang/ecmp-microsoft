package com.hq.ecmp.constant;

/**
 * @ClassName CommonConstant
 * @Description TODO
 * @Author yj
 * @Date 2020/3/9 15:24
 * @Version 1.0
 */
public final class CommonConstant {

    //客服电话
    public static final String CUSTOMER_PHONE = "customer_phone";
    public static final String ONLINE_CAR_TYPR_REDIS = "onlie_car_type";
    //redis中调度锁的前缀
    public static final String DISPATCH_LOCK_PREFIX = "dispatch_";
    //redis中约车次数的前缀
    public static final String APPOINTMENT_NUMBER_PREFIX = "APPOINTMENT_NUMBER_";
    public static final String PROJECT_USER_TREE = "PROJECT_USER_TREE:%s";
    //司机等待的开启和关闭
    //开启
    public static final String START = "1";
    //关闭
    public static final String FINISH = "2";

    /**总公司系统管理员角色**/
    public static final String ADMIN_ROLE="admin";
    /**分子公司管理员角色*/
    public static final String SUB_ADMIN_ROLE="sub_admin";

    //联系乘客或者联系车队，对应的电话角色名
    public static final String PASSENGER_ROLE = "乘车人";
    public static final String DISPATCHER_ROLE = "调度员";
    public static final String CARGROUP_PHONE_ROLE = "车队座机";

    //订单途径地信息表，标识是否通过此途径地
    public static final String PASS = "Y000";
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
     * 驾驶员状态：待审核
     */
    public static final String STATE_WAIT = "W001";
    /**
     * DRIVER
     * 驾驶员状态：生效中
     */
    public static final String STATE_ON = "V000";
    /**
     * 驾驶员状态：失效/离职
     */
    public static final String STATE_OFF = "NV00";

    public static final String SWITCH_ON_CUSTOM = "2";
    //后台基本配置前缀
    public static final String SYS_CONFIG_PREFIX = "sys.";
    public static final String NOTHING = "无";

    /**
     * 数字常亮0
     */
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;

    public static final String DEPT_TYPE_ORG = "1";
    //后台基本配置前缀
    public static final String DEPT_TYPE_DEPT = "2";
    public static final String DEPT_TYPE_FLEET = "3";

    public static final String ALLOW_DATA = "0-0";
    //初始角色KEY
    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_SUB_ADMIN = "sub_admin";

    /**
     * 制度用车城市规则C001不限,C002可用城市,C003不可用城市
     */
    public static final String ROLE_CITY_C001 = "C001";
    public static final String ROLE_CITY_C002 = "C002";
    public static final String ROLE_CITY_C003 = "C003";
    public static final String ROLE_EMPLOYEE = "employee";

    public static final String ROLE_DRIVER = "driver";

    public static final String ROLE_DEPT_MANAGER = "dept_manager";

    public static final String ROLE_PROJECT_MANAGER = "project_manager";

    public static final String ROLE_DISPATCHER = "dispatcher";
    /**
     * 系统判断字段：是
     */
    public static final String YES = "0";
    /**
     * 系统判断字段：否
     */
    public static final String NO = "1";

    public static final String NOT_INVOICED = "S008";  //订单财务信息表 S008未开票

//    public static final String ADMIN = "%管理%";
    public static final String ADMIN = "%admin%";

//    public static final String EMPLOYEE = "%员工%";
    public static final String EMPLOYEE = "%employee%";

//    public static final String DISPATCHER = "%调度%";
    public static final String DISPATCHER = "%dispatcher%";

    //    public static final String CUSTOMER_SERVICE = "%客服%";
    public static final String CUSTOMER_SERVICE = "%customer_service%";
}
