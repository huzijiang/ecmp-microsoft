package com.hq.ecmp.mscore.service;


import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.domain.OrderDriverListInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.dto.CallTaxiDto;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
import com.hq.ecmp.mscore.vo.DriverOrderInfoVO;
import com.hq.ecmp.mscore.vo.OrderStateVO;
import com.hq.ecmp.mscore.vo.OrderVO;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderInfoService {
    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderInfo selectOrderInfoById(Long orderId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderInfo(OrderInfo orderInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderInfo(OrderInfo orderInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] orderIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long orderId);

    /**
     * 我的行程订单列表
     *
     * @param userId
     * @return
     */
    public List<OrderListInfo> getOrderList(Long userId, int pageNum, int pageSize);

    /**
     * 订单轨迹表添加通用方法
     * @param orderId
     * @param updateState
     * @param userId
     * @return
     */
    public  int insertOrderStateTrace(String orderId,String updateState,String userId,String cancelReason);
    
    
    /**
     * 查询所有待调度的订单(包含待改派)
     * @return
     */
    public List<DispatchOrderInfo> queryWaitDispatchList(Long userId);
    
    /**
     * 查询所有已完成调度的订单
     * @return
     */
    public List<DispatchOrderInfo> queryCompleteDispatchOrder();

    /**
     *  通过用户id查询司机的任务列表
     * @param userId
     * @return
     */
    public  List<OrderDriverListInfo> getDriverOrderList(Long userId,int pageNum, int pageSize)throws Exception;

    /**
     * 查询待调单的订单详情(包含待改派的)
     * @param orderId
     * @return
     */
    public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId);

    /**
     * 查询已完成的订单详情(包含待改派的)
     * @param orderId
     * @return
     */
    public DispatchOrderInfo getCompleteDispatchOrderDetailInfo(Long orderId);

    /**
     * 获取订单详情
     * @param orderId
     * @return
     */
    OrderVO orderBeServiceDetail(Long orderId)throws Exception;

    /**
     * 获取服务提示语
     * @param orderId
     * @return
     */
    String orderHint(Long orderId);

    MessageDto getOrderMessage(Long userId,String states,Long driveId);

    /**
     * 网约车异步约车方法
     */
    void platCallTaxi(CallTaxiDto callTaxiDto, String enterpriseId, String licenseContent, String apiUrl,String userId);

   /**
    * 自有车派车
    * @param orderId
    * @param driverId
    * @param carId
    * @param userId
    * @return
    */
    public boolean ownCarSendCar(Long orderId,Long driverId,Long carId,Long userId);

    void initOrder(Long applyId, Long jouneyId, Long userId) throws Exception;


    /**
     * 获取驾驶员对现有车的两小时内的下一个任务
     * @param driverId
     */
    OrderDriverListInfo getNextTaskWithDriver(Long driverId);

    /**
     * 获取汽车对现有车的两小时内的下一个任务
     * @param carId
     */
    OrderDriverListInfo getNextTaskWithCar(Long carId);

    MessageDto getCancelOrderMessage(Long userId, String states);

    List<OrderDriverListInfo> driverOrderUndoneList(Long userId, Integer pageNum, Integer pageSize, int day)throws Exception;

    int driverOrderCount(Long userId)throws Exception;

    DriverOrderInfoVO driverOrderDetail(Long orderId);

    OrderStateVO getOrderState(Long orderId);

    /**
     * pc端获取订单列表
     * @param orderListBackDto
     * @return
     */
    List<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto);

    /**
     * PC端查询订单详情
     * @param orderNo
     */
    OrderDetailBackDto getOrderListDetail(String orderNo);

    //查询网约车状态
    JSONObject getTaxiOrderState(Long orderId)throws Exception;
    
    public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query);
    
    public Integer queryApplyDispatchListCount(ApplyDispatchQuery query);

}

