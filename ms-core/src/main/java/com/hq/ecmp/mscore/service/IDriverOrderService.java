package com.hq.ecmp.mscore.service;

public interface IDriverOrderService {

    /**
     *  处理订单和订单轨迹的相关变化
     * @param type
     * @param currentPoint
     * @param orderNo
     * @param userId
     * @throws Exception
     */
    public void  handleDriverOrderStatus(String type,String currentPoint,String orderNo,Long userId) throws Exception;

    /**
     * 司机是否继续用车，或者还车
     */
    public void  isContinue();

    public void waitingOrder(String orderNo,String  isFinish,String currentPoint,String userId);
}
