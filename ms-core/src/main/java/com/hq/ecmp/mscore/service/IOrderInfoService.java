package com.hq.ecmp.mscore.service;


import com.alibaba.fastjson.JSONObject;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;
import java.util.Map;

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
     * @param user
     * @return
     */
    public PageResult<OrderListInfo> getOrderList(SysUser user, int pageNum, int pageSize, int isConfirmState);

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


    List<OrderDriverListInfo> driverOrderUndoneList(LoginUser loginUser, Integer pageNum, Integer pageSize, int day)throws Exception;

    int driverOrderCount(LoginUser loginUser)throws Exception;

    DriverOrderInfoVO driverOrderDetail(Long orderId) throws Exception;

    OrderStateVO getOrderState(Long orderId);

    /**
     * pc端获取订单列表
     * @param orderListBackDto
     * @return
     */
    PageResult<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto,LoginUser user);

    /**
     * PC端查询订单详情
     * @param orderNo
     */
    OrderDetailBackDto getOrderListDetail(String orderNo);

    //查询网约车状态
    JSONObject getThirdPartyOrderState(Long orderId)throws Exception;
    OrderStateVO getTaxiState(OrderStateVO orderVO,Long orderNo)throws Exception;

    List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query);

    Integer queryApplyDispatchListCount(ApplyDispatchQuery query);

    List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query);

    Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query);
    /**
     * 驳回改派申请
     * @param orderId
     * @param rejectReason
     * @param optUserId
     * @return
     */
    boolean rejectReassign(Long orderId,String rejectReason,Long optUserId);

    /**
     * 差旅申请派车
     * @param applyUseWithTravelDto
     * @return
     */
    Long applyUseCarWithTravel(ApplyUseWithTravelDto applyUseWithTravelDto,Long userId) throws Exception;


    /**
     * 得到订单的历史轨迹
     * @param orderId
     * @return
     */
    List<OrderHistoryTraceDto> getOrderHistoryTrace(Long orderId) throws Exception;


    /**
     * 取消订单
     * @param orderId
     * @param userId
     * @param cancelReason
     * @throws Exception
     */
    void cancelOrder(Long orderId,Long userId,String cancelReason) throws Exception;


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
    void reassign( String orderNo,String rejectReason,String status,Long userId,Long oldDriverId,Long oldCarId) throws Exception;

    /**
     * 网约车参数校验+调用
     * @param orderId
     * @param userId
     * @param carLevel
     * @throws Exception
     */
    void platCallTaxiParamValid(Long  orderId,String userId,String carLevel) throws Exception;

    /**
     * 获取网约车服务结束费用
     * @param orderId
     * @return
     */
    OrderCostDetailVO getOrderCost(Long orderId);

    DispatchSendCarPageInfo  getDispatchSendCarPageInfo(Long orderId);

    DispatchSendCarPageInfo getUserDispatchedOrder(Long orderId);

    boolean sendCarBeforeCreatePlanPrice(Long orderId,Long userId) throws Exception ;




    /**
     * 公务调度后选择网约车 生成返程的权限 和订单
     * @param orderId
     */
    void checkCreateReturnAuthority(Long orderId,Long optUserId)throws Exception;

    /**
     * 过12小时自动确认行程
     * @param timeout 超时时间
     */
    void confirmOrderJourneyAuto(int timeout);


    /***
     * 获取乘车信息
     * @param orderId
     * @return
     * @throws Exception
     */
    OrderInfoMessage getMessage(Long orderId)throws Exception;

    /***
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    int orderConfirm(Long userId ,Long orderId)throws Exception;


    /***
     *
     * @param data
     * @return
     */
    int updateTheOrder(Long userId,OrderServiceCostDetailRecordInfo data)throws Exception;

    /**
     * 订单改派
     * add by liuzb
     * @param userId
     * @param orderId
     * @return
     * @throws Exception
     */
    String orderReassignment(Long userId,Long orderId)throws Exception;


    /***
     * 根据订单获取内部调度员的电话
     * add by liuzb
     * @param orderId
     * @return
     * @throws Exception
     */
    String dispatcherPhone(Long orderId)throws Exception;


    /***
     * 
     * @param orderId
     * @return
     * @throws Exception
     */
    Map downloadOrderData(Long orderId)throws Exception;


    /***
     *
     * @param user
     * @return
     * @throws Exception
     */
    Map<String,Object> orderServiceCategory(LoginUser user)throws Exception;


    /***
     *
     * @param user
     * @return
     * @throws Exception
     */
    List<String> getUseTheCar(LoginUser user)throws Exception;


    /***
     *
     * @param orderInfoFSDto
     * @param user
     * @return
     */
    PageResult<OrderInfoFSDto> getOrderInfoList(OrderInfoFSDto orderInfoFSDto, LoginUser user)throws Exception;


    List<MoneyListDto> getMoneyList(ReckoningDto param);
    Map<String, Map<String,Integer>> selectOrderCarGroup(Long companyId);

    Map<String,Integer> selectNormalOrderReserveTime(Long companyId,String beginDate,String endDate);

    PayeeInfoDto getPayeeInfo(ReckoningDto param);

    PayeeInfoDto getCarGroupInfo(String carGroupId);

    String selectOrderApplyInfoByOrderNumber(OrderInfo orderInfo);
}

