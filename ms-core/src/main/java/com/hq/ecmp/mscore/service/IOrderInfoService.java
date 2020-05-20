package com.hq.ecmp.mscore.service;


import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.vo.*;

import java.text.ParseException;
import java.util.List;

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
    public PageResult<OrderListInfo> getOrderList(Long userId, int pageNum, int pageSize);

    /**
     * 订单轨迹表添加通用方法
     * @param orderId
     * @param updateState
     * @param userId
     * @param oldCarId  改派前的车辆id
     * @param oldDriverId 改派后的司机id
     * @return
     */
    public  int insertOrderStateTrace(String orderId,String updateState,String userId,String cancelReason,Long oldDriverId,Long oldCarId);


    /**
     * 查询所有待调度的订单(包含待改派)
     * @return
     */
    public List<DispatchOrderInfo> queryWaitDispatchList(Long userId);


    public List<DispatchOrderInfo>  queryAllWaitDispatchList(Long userId,Boolean isAutoDis,Long companyId);

    /**
     * 查询所有已完成调度的订单
     * @param userId  当前登录人id
     * @return
     */
    public List<DispatchOrderInfo> queryCompleteDispatchOrder(Long userId);

    /**
     *  通过用户id查询司机的任务列表
     * @return
     */
    public  List<OrderDriverListInfo> getDriverOrderList(LoginUser loginUser,int pageNum, int pageSize)throws Exception;

    /**
     * 查询待调单的订单详情(包含待改派的)
     * @param orderId
     * @return
     */
    public ApiResponse<DispatchOrderInfo> getWaitDispatchOrderDetailInfo(Long orderId, Long userId);

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
     * @throws Exception
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
     * @param orderId
     * @param enterpriseId
     * @param licenseContent
     * @param apiUrl
     * @param userId
     * @param carLevel
     */
    void platCallTaxi(Long orderId, String enterpriseId, String licenseContent, String apiUrl,String userId,String carLevel) throws Exception;

   /**
    * 自有车派车
    * @param orderId
    * @param driverId
    * @param carId
    * @param userId
    * @return
    */
    public boolean ownCarSendCar(Long orderId,Long driverId,Long carId,Long userId)throws Exception ;

    /**
     * 公务下单（网约车派车）
     * @param officialOrderReVo
     * @param userId
     * @return
     * @throws Exception
     */
    Long officialOrder(OfficialOrderReVo officialOrderReVo, Long userId) throws Exception;


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

    List<OrderDriverListInfo> driverOrderUndoneList(LoginUser loginUser, Integer pageNum, Integer pageSize, int day)throws Exception;

    int driverOrderCount(LoginUser loginUser)throws Exception;

    DriverOrderInfoVO driverOrderDetail(Long orderId);

    OrderStateVO getOrderState(Long orderId);

    /**
     * pc端获取订单列表
     * @param orderListBackDto
     * @return
     */
    PageResult<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto);

    /**
     * PC端查询订单详情
     * @param orderNo
     */
    OrderDetailBackDto getOrderListDetail(String orderNo);

    //查询网约车状态
    JSONObject getThirdPartyOrderState(Long orderId)throws Exception;
    JSONObject getDriverLocation(String driverPhone)throws Exception;
    OrderStateVO getTaxiState(OrderStateVO orderVO,Long orderNo)throws Exception;

    public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query);

    public Integer queryApplyDispatchListCount(ApplyDispatchQuery query);

    public List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query);

    public Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query);
    /**
     * 驳回改派申请
     * @param orderId
     * @param rejectReason
     * @param optUserId
     * @return
     */
    public boolean rejectReassign(Long orderId,String rejectReason,Long optUserId);

    /**
     * 差旅申请派车
     * @param applyUseWithTravelDto
     * @return
     */
    public Long applyUseCarWithTravel(ApplyUseWithTravelDto applyUseWithTravelDto,Long userId) throws Exception;


    /**
     * 得到订单的历史轨迹
     * @param orderId
     * @return
     */
    public List<OrderHistoryTraceDto> getOrderHistoryTrace(Long orderId) throws Exception;


    /**
     * 取消订单
     * @param orderId
     * @param userId
     * @param cancelReason
     * @throws Exception
     */
    public void cancelOrder(Long orderId,Long userId,String cancelReason) throws Exception;


    /**
     * 改派订单
     * @param orderNo
     * @param rejectReason
     * @param status
     * @param userId
     * @param oldDriverId 老司机id
     * @param oldCarId 老车id
     * @throws Exception
     */
    public void reassign( String orderNo,String rejectReason,String status,Long userId,Long oldDriverId,Long oldCarId) throws Exception;

    Integer getDriverOrderListCount(LoginUser loginUser) throws Exception;

    /**
     * 网约车参数校验+调用
     * @param orderId
     * @param userId
     * @param carLevel
     * @throws Exception
     */
    public void platCallTaxiParamValid(Long  orderId,String userId,String carLevel) throws Exception;

    /**
     * 获取网约车服务结束费用
     * @param orderId
     * @return
     */
    OrderCostDetailVO getOrderCost(Long orderId);

    public DispatchSendCarPageInfo  getDispatchSendCarPageInfo(Long orderId);

    public DispatchSendCarPageInfo getUserDispatchedOrder(Long orderId);
    //回调修改订单状态
    void callBackOrderState(String jsonResult)throws Exception;

    public boolean sendCarBeforeCreatePlanPrice(Long orderId,Long userId) throws Exception ;

    /**
     * 查询过期的订单,修改状态和添加轨迹表记录
     * @return
     */
    public  void checkOrderIsExpired();


    /**
     * 公务调度后选择网约车 生成返程的权限 和订单
     * @param orderId
     */
    public void checkCreateReturnAuthority(Long orderId,Long optUserId)throws Exception;

    /**
     * 过12小时自动确认行程
     * @param timeout 超时时间
     */
    void confirmOrderJourneyAuto(int timeout);

    void replaceCar(OrderInfo orderInfo,Long userId) throws Exception;


    /***
     * 获取乘车信息
     * @param orderId
     * @return
     * @throws Exception
     */
    OrderInfoMessage getMessage(Long orderId)throws Exception;

    /**
     * 获取申请调度列表
     * @param query
     * @return
     */
    PageResult<DispatchVo> queryDispatchList(ApplyDispatchQuery query,LoginUser loginUser);

    /**
     * 获取直接调度列表
     * @param
     * @param
     * @return
     */
    PageResult<DispatchVo> queryDispatchOrder(LoginUser loginUser,ApplyDispatchQuery query);

    /**
     * 获取调度改派列表
     * @param query
     * @param loginUser
     * @return
     */
    PageResult<DispatchVo> queryDispatchReassignmentList(ApplyDispatchQuery query, LoginUser loginUser);
}

