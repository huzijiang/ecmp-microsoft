package com.hq.ecmp.constant;

/**
 * @author ghb
 * @description 支付有关公工常量
 * @date 2020/5/10
 */
public class OrderPayConstant {


    public static String PAID  =  "P999";   //已支付
    public static String UNPAID  =  "P001";  //未支付
    public static String CNFIRM_PAID  =  "P500";  //待确认

    public static String PAY_AFTER_STATEMENT  =  "M001";  //结单后付费
    public static String BILL_ADVANCE   =  "M002";  //开单预付费
    public static String CHARGE_CARD_DEDUCTION  =  "M003";  //充值卡扣款
    public static String PAY_ELSE  =  "M999";  //其他

    public static String PAY_CHANNEL_WX  =  "weixin";  //微信支付
    public static String PAY_CHANNEL_ALI  =  "zhifubao";  //支付宝支付

    public static String WX_CHANNEL_RATE  =  "0.006";  //微信税率
    public static String ALI_CHANNEL_RATE  =  "0.01";  //支付宝税率

    public static  String ORDER_PAY_TIMEOUT = "30m";  //支付宝--超时关闭该订单时间
    public static  String PRODUCT_CODE = "QUICK_MSECURITY_PAY";  //支付宝--销售产品码

    public static  String WX_RETURN_CODE_OK = "success";  //微信退款成功状态
    public static  String ALI_RETURN_CODE_OK = "success";  //支付宝退款成功状态
    public static  String WX_RETURN_CODE_ERROR = "fail";  //微信退款失败状态
    public static  String ALI_RETURN_CODE_ERROR = "fail";  //支付宝退款失败状态
}
