package com.hq.ecmp.mscore.mapper;


import com.hq.ecmp.mscore.bo.OrderTaskClashBo;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.domain.OrderDriverListInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.vo.ApplyDispatchVo;
import com.hq.ecmp.mscore.vo.DriverOrderInfoVO;
import com.hq.ecmp.mscore.vo.OrderStateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface OrderInfoMapper {
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
     * 删除【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderInfoById(Long orderId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderInfoByIds(Long[] orderIds);

    /**
     * 获取乘客端我的行程订单列表
     *
     * @param userId
     * @return
     */
    public List<OrderListInfo> getOrderList(Long userId);


    public List<DispatchOrderInfo> queryOrderRelateInfo(OrderInfo orderInfo);

    /**
     * 查询已完成调度的订单
     *
     * @return
     */
    public List<DispatchOrderInfo> queryCompleteDispatchOrder();

    /**
     * 通过司机id获取司机的任务列表
     *
     * @param driverId
     * @return
     */
    public List<OrderDriverListInfo> getDriverOrderList(@Param("driverId") long driverId, @Param("flag") int flag);
    Integer getDriverOrderListCount(@Param("driverId") Long driverId,@Param("states")  String states);
    public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId);

    public DispatchOrderInfo queryCompleteDispatchOrderDetail(Long orderId);

    /**
     * 获取调度员派车通知
     */
    MessageDto getOrderMessage(@Param("userId") Long userId, @Param("states") String states, @Param("driveId") Long driveId);


    /**
     * 通过司机id获取两小时内司机的下一个任务数据
     *
     * @param driverId
     * @return
     */
    OrderDriverListInfo getNextTaskWithDriver(@Param("driverId") Long driverId);

    /**
     * 通过汽车id获取两小时内司机的下一个任务数据
     *
     * @param carId
     * @return
     */
    OrderDriverListInfo getNextTaskWithCar(@Param("carId") Long carId);

    MessageDto getCancelOrderMessage(@Param("driverId") Long driverId, @Param("state") String state);

    //查询司机未完成的任务数量
    int getDriverOrderCount(@Param("driverId") Long driverId, @Param("states") String states);

    List<OrderDriverListInfo> driverOrderUndoneList(@Param("driverId") long driverId, @Param("day") int day);

    DriverOrderInfoVO selectOrderDetail(Long orderId);

    OrderStateVO getOrderState(@Param("orderId")Long orderId,@Param("regimenType")String regimenType);

    /**
     * pc端获取订单列表
     *
     * @param orderListBackDto
     * @return
     */
    List<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto);

    /**
     * 根据订单编号获取订单详情数据
     *
     * @param orderNo
     * @return
     */
    OrderDetailBackDto getOrderListDetail(@Param("orderId") String orderNo);


    /**
     * pc端分页获取申请调派订单
     * @param query
     * @return
     */
    public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query);

    public Integer queryApplyDispatchListCount(ApplyDispatchQuery query);

    public List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query);

    public Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query);

    OrderInfo selectDriverOrder(@Param("driverId")Long driverId,@Param("state") String state);

    /**
     * 查询指定行程下的所有订单状态
     * @param journeyId
     * @return
     */
    public List<String> queryAllOrderStatusByJourneyId(Long journeyId);

    /**
     * 查询指定权限下的有效订单状态
     * @param powerId
     * @return
     */
    public String queryVaildOrderStatusByPowerId(Long powerId);

    /**
     * 查询权限下的有效订单
     * @param powerId
     * @return
     */
    public Long queryVaildOrderIdByPowerId(Long powerId);

    public List<String> queryAllOrderStatusByPowerId(Long powerId);

    public List<Long> queryOrderIdListByPowerId(Long powerId);

    List<String> queryUseCarMode(Long powerId);

    /**
     * 通過用車權限id查詢是否有有效的訂單
     * @param powerId
     * @return
     */
    List<OrderInfo> getValidOrderByPowerId(Long powerId);



    /**
     * 获取 指定车辆 与出发时间冲突的任务
     * @return
     */
    public List<OrderInfo> getSetOutClashTask(OrderTaskClashBo carTaskClashBo);

    /**
     * 获取 指定车辆 与 到达时间冲突的任务
     * @return
     */
    public List<OrderInfo> getArrivalClashTask(OrderTaskClashBo carTaskClashBo);


    /**
     * 获取 指定车辆 与出发时间 不冲突 的订单任务
     * @return
     */
    public List<OrderInfo> getSetOutBeforeTaskForCar(OrderTaskClashBo carTaskClashBo);

    /**
     * 获取 指定车辆 与到达时间 不冲突 的订单任务
     * @return
     */
    public List<OrderInfo> getArrivalAfterTaskForCar(OrderTaskClashBo carTaskClashBo);



}

