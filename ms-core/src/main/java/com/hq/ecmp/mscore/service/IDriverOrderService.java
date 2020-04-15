package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.domain.OrderViaInfo;
import com.hq.ecmp.mscore.dto.ContactorDto;
import com.hq.ecmp.mscore.dto.IsContinueReDto;
import com.hq.ecmp.mscore.dto.OrderViaInfoDto;

import java.util.List;

public interface IDriverOrderService {

    /**
     *  处理订单和订单轨迹的相关变化
     * @param type
     * @param currentPoint
     * @param orderNo
     * @param userId
     * @throws Exception
     */
    public void  handleDriverOrderStatus(String type,String currentPoint,String orderNo,Long userId,String mileage,String travelTime) throws Exception;

    /**
     * 司机是否继续用车，或者还车
     * @param orderNo
     */
    public IsContinueReDto isContinue(String orderNo,String userId);

    /**
     * 司机开启等待和关闭等待的逻辑
     * @param orderNo
     * @param isFinish
     * @param currentPoint
     * @param userId
     * @param waitingId
     * @return
     * @throws Exception
     */
    public Long waitingOrder(String orderNo,String  isFinish,String currentPoint,String userId,String waitingId) throws Exception;

    /**
     * 获取乘车人的电话
     * @param orderId
     * @return
     */
    public List<JourneyPassengerInfo> getInfoWithPassenger(String orderId);

    /**
     * 获取车队座机和调度人电话
     * @param orderId
     */
    public List<ContactorDto> getInfoWithCarGroup(String orderId);

    /**
     * 获取订单途径地信息
     * @param orderId
     */
    public List<OrderViaInfoDto> getOrderViaInfos(String orderId);

}
