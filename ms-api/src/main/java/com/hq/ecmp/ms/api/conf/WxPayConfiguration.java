package com.hq.ecmp.ms.api.conf;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Binary Wang
 */
@Configuration
//@ConditionalOnClass(WxPayService.class)
//@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    //应用id
    @Value("${appId}")
    private String appId;
    //商户id
    @Value("${mchId}")
    private String mchId;
    //回调地址
    @Value("${notify_url}")
    private String notifyUrl;
    //交易类型
    @Value("${trade_type}")
    private String tradeType;
    //签名类型
    @Value("${sign_type}")
    private String signType;
    //交易秘钥
    @Value("${mchKey}")
    private String mchKey;


    @Bean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setNotifyUrl(notifyUrl);
        payConfig.setTradeType(tradeType);
        payConfig.setSignType(signType);
        payConfig.setMchKey(mchKey);
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


}
