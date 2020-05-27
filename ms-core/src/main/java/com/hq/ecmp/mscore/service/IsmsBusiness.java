package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.UndoSMSTemplate;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;

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

    /**
     * 网约车司机已到达发短信
     * @param orderId
     */
    public void driverArriveMessage(Long orderId);

    /**
     * 开始服务网约车
     * @param orderId
     * @param createId
     */
    public void startService(Long orderId,Long createId);

    /**
     * 结束服务未确认行程短信
     * @param orderId
     */
    public void endServiceNotConfirm(Long orderId);

    /**
     * 更换车辆后通知调度员，申请员，乘车人
     * @param orderId
     */
    public void sendMessageReplaceCarComplete(Long orderId,Long userId);

    /**
     * 换车成功短信通知
     * @param orderId
     */
    public void sendSmsReplaceCar(Long orderId) throws Exception;

    void sendSmsDispatchReject(OrderInfo orderInfo,String rejectReason)throws Exception;

    void sendSmsInnerDispatcherReject(OrderInfo orderInfo, String rejectReason)throws Exception;

    /**
     * 用车申请---用车人短信
     * @param journeyId
     * @param applyId
     * @param officialCommitApply
     */
    void sendVehicleUserApply(Long journeyId, Long applyId, ApplyOfficialRequest officialCommitApply) throws Exception;

    /**
     * 用车申请---用车人短信
     * @param journeyId
     * @param applyId
     * @param officialCommitApply
     */
    void sendDispatcherApply(Long journeyId, Long applyId, ApplyOfficialRequest officialCommitApply) throws Exception;

    /**
     * 撤销未派单短信
     * @param undoSMSTemplate
     */
    void sendRevokeUndelivered(UndoSMSTemplate undoSMSTemplate) throws Exception;

    /**
     * 撤销已派单短信
     * @param undoSMSTemplate
     */
    void sendRevokealSentList(UndoSMSTemplate undoSMSTemplate) throws Exception;


    void sendSmsServiceStart(long orderId);

    void sendSmsDriverServiceEnd(long orderId);

    /**
     * 撤销待服务短信
     * @param undoSMSTemplate
     */
    void sendRevokeToBeServed(UndoSMSTemplate undoSMSTemplate) throws Exception;
}
