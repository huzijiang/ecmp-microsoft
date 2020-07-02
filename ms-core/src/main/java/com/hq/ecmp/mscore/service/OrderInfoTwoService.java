package com.hq.ecmp.mscore.service;

import com.hq.api.system.domain.SysUser;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderInfoTwoService {

    CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason,Long loginUserId) throws Exception;

    List<RunningOrderVo> runningOrder(Long orderId);

    Map<String,Object> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser);

    OrderReassignVO reassignDetail(Long orderNo, Long driverId);

    void callBackOrderState(String jsonResult)throws Exception;
    /**
     * 查询过期的订单,修改状态和添加轨迹表记录
     * @return
     */
    void checkOrderIsExpired();
    void replaceCar(OrderInfo orderInfo, Long userId) throws Exception;

    Integer getDriverOrderListCount(LoginUser loginUser) throws Exception;
    /**
     *  通过用户id查询司机的任务列表
     * @return
     */
    List<OrderDriverListInfo> getDriverOrderList(LoginUser loginUser, int pageNum, int pageSize)throws Exception;

    /**
     * 获取直接调度列表
     * @param
     * @param
     * @return
     */
    Map<String,Object> queryDispatchOrder(LoginUser loginUser,ApplyDispatchQuery query);

    /**
     * 获取调度改派列表
     * @param query
     * @param loginUser
     * @return
     */
    Map<String,Object> queryDispatchReassignmentList(ApplyDispatchQuery query, LoginUser loginUser);

    /**
     * 用车申请列表
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    PageResult<UserApplySingleVo> getUseApplySearchList(UserApplySingleVo userApplySingleVo, LoginUser loginUser);

    /**
     * 用车申请列表
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    PageResult<UserApplySingleVo> getUseApplyList(UserApplySingleVo userApplySingleVo, LoginUser loginUser);

    /**
     * 获取当前业务员的待派车，已派车，已过期数量
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    List<UserApplySingleVo> getUseApplyCounts(UserApplySingleVo userApplySingleVo, LoginUser loginUser);

    /**
     * 获取首页业务员待确认订单
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    PageResult<UserApplySingleVo> getToBeConfirmedOrder(UserApplySingleVo userApplySingleVo, LoginUser loginUser);

    /**
     *  佛山后管申请单调度列表
     * @param query
     * @param loginUser
     * @return
     */
    Map<String,Object>  queryDispatchListCharterCar(ApplyDispatch query, LoginUser loginUser);

    /**
     *  佛山后管首页申请单调度列表
     * @param query
     * @param loginUser
     * @return
     */
    PageResult<DispatchVo> queryHomePageDispatchListCharterCar(ApplyDispatchQuery query, LoginUser loginUser);

    /**
     * 佛山调度可用外部车队列表
     *      * @param orderId
     * @return
     */
    public List<CarGroupInfo> dispatcherCarGroupList(Long orderId, LoginUser loginUser,String carGroupUserMode);

    /**
     * 佛山内外调度派车
     * @param dispatchSendCarDto
     */
    void dispatcherSendCar(DispatchSendCarDto dispatchSendCarDto);


    /**
     * 获取首页业务员待确认订单
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    int toSureToBeConfirmedOrder(UserApplySingleVo userApplySingleVo, LoginUser loginUser);
    void dismissedDispatch(ApplyDispatchQuery query, LoginUser loginUser)throws Exception;

    void pickUpTheCar(Long userId, Long orderId)throws Exception;

    void returnCar(Long userId, Long orderId)throws Exception;

    List<DispatchVo> queryDispatchListCharterCars(ApplyDispatchQuery applyDispatchQuery,LoginUser loginUser);

    /**
     * 外部车队列表
     * @param companyId
     * @return
     */
    List<CarGroupInfo> applySingleCarGroupList(Long companyId);

    List<CarGroupInfo> userDeptCarGroupList(Long deptId);

    /**
     * 调度员可看的调度列表（佛山调度列表优化）--优化中，暂未使用
     * @param query
     * @return
     */
    Map<String,Object> dispatchListCharterCarWithDispatcher(ApplyDispatch query,LoginUser loginUser);

    void updatePickupCarState();

    UserApplySingleVo getOrderInfoDetail(Long orderId, SysUser user,Long applyId)throws Exception;
    /**
     * 佛山后管申请单调度-获取用车单位列表
     * @param companyId
     * @return
     */
    List<EcmpOrg> getUseCarOrgList(Long companyId);


    /**
     * 外部调度员驳回
     * @param query
     * @param loginUser
     * @throws Exception
     */
    void dismissedOutDispatch(ApplyDispatchQuery query, LoginUser loginUser)throws Exception;

}
