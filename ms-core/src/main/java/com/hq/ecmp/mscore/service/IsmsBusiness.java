package com.hq.ecmp.mscore.service;

import org.springframework.scheduling.annotation.Async;

public interface IsmsBusiness {

    /**
     * 网约车约车失败短信发送
     * @param orderId
     */
    public void sendSmsCallTaxiNetFail(Long orderId);

    /**
     * 网约车，约车成功短信通知
     */
    public void sendSmsCallTaxiNet(Long orderId);

    /**
     * 取消订单发送短信
     * @param orderId
     * @throws Exception
     */
    public void sendSmsCancelOrder(Long orderId);
}
