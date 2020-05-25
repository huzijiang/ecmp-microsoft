package com.hq.ecmp.mscore.service;

import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.ApplyDispatchQuery;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.OrderDriverListInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderInfoTwoService {

    CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason,Long loginUserId) throws Exception;

    List<RunningOrderVo> runningOrder(Long orderId);

    PageResult<DispatchVo> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser);

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
    PageResult<DispatchVo> queryDispatchOrder(LoginUser loginUser,ApplyDispatchQuery query);

    /**
     * 获取调度改派列表
     * @param query
     * @param loginUser
     * @return
     */
    PageResult<DispatchVo> queryDispatchReassignmentList(ApplyDispatchQuery query, LoginUser loginUser);

    /**
     * 用车申请列表
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    PageResult<UserApplySingleVo> getUseApplySearchList(UserApplySingleVo userApplySingleVo, LoginUser loginUser);

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
    PageResult<DispatchVo> queryDispatchListCharterCar(ApplyDispatchQuery query, LoginUser loginUser);

    /**
     * 佛山调度可用外部车队列表
     *      * @param orderId
     * @return
     */
    public List<CarGroupInfo> dispatcherCarGroupList(Long orderId, LoginUser loginUser);

    /**
     * 佛山内外调度派车
     * @param dispatchSendCarDto
     */
    void dispatcherSendCar(DispatchSendCarDto dispatchSendCarDto);

}
