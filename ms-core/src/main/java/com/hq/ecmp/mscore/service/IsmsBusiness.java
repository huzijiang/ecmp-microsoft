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
     * @param orderId
     */
    public void sendSmsCallTaxiNet(Long orderId);

    /**
     * 取消订单发送短信
     * @param orderId
     */
    public void sendSmsCancelOrder(Long orderId);

    /**
     * 自有车司机到达发送短信
     * @param orderId
     */
    public void sendSmsDriverArrivePrivate(Long orderId);

    /**
     * 司机开始服务发送短信
     * @param orderId
     */
    public void sendSmsDriverBeginService(Long orderId);

    /**
     * 司机服务结束发送短信
     * @param orderId
     */
    public void sendSmsDriverServiceComplete(Long orderId);
}
