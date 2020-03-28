package com.hq.ecmp.mscore.service;

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
     * 取消订单无取消费发送短信
     * @param orderId
     */
    public void sendSmsCancelOrder(Long orderId);

    /**
     * 取消订单有取消费发送短信
     * @param orderId
     * @param comAmount
     */
    public void sendSmsCancelOrderHaveFee(Long orderId,Double comAmount);

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

    /**
     * 取消订单消息通知
     * @param orderId
     * @param createId
     */
    public void sendMessageCancelOrder(Long orderId,Long createId);

    /**
     * 服务开始消息通知（申请人和乘车人）
     * @param orderId
     * @param createId
     */
    public void sendMessageServiceStart(Long orderId,Long createId);

    /**
     * 改派成功消息通知（申请人，乘车人，司机）
     * @param orderId
     * @param createId
     */
    public void sendMessageReassignSucc(Long orderId,Long createId);

    /**
     * 差旅自有车下单成功，给调度员发通知
     * @param orderId
     * @param createId
     */
    public void sendMessagePriTravelOrderSucc(Long orderId,Long createId);
    
    /**
     * 调度自有车成功后给司机,申请人发通知
     * @param orderId
     */
    public void sendMessageDispatchCarComplete(Long orderId,Long userId);
}
