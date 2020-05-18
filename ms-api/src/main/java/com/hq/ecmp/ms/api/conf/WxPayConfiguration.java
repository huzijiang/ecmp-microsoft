package com.hq.ecmp.ms.api.conf;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
//@ConditionalOnClass(WxPayService.class)
//@EnableConfigurationProperties(WxPayProperties.class)
public class WxPayConfiguration {
    //应用id
    @Value("${wx.appId}")
    private String appId;
    //商户id
    @Value("${wx.mchId}")
    private String mchId;
    //回调地址
    @Value("${wx.notify_url}")
    private String notifyUrl;
    //交易类型
    @Value("${wx.trade_type}")
    private String tradeType;
    //签名类型
    @Value("${wx.sign_type}")
    private String signType;
    //交易秘钥
    @Value("${wx.mchKey}")
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
