package com.hq.ecmp.ms.api.conf;

/**
 * @author ghb
 * @description 支付宝支付配置类
 * @date 2020/5/5
 */
public class WechatPayConfig {

        //正式环境
//        public static String refund_notify_url = "https://app-api.hqzhixing.com/pay/wechat/v2/callback";
        //测试环境
//        public static String refund_notify_url = "https://test-app-api.hqzhixing.com:60001/pay/wechat/v2/callback";
        //开发环境
        public static String refund_notify_url = "https://dev-app-api.hqzhixing.com/pay/wechat/v2/callback";
}
