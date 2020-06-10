package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.PiclUpCarOrderVO;
import com.hq.ecmp.mscore.vo.RejectDispatcherUserVO;
import com.hq.ecmp.mscore.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface OrderStateTraceInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderStateTraceInfo selectOrderStateTraceInfoById(Long traceId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderStateTraceInfo> selectOrderStateTraceInfoList(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoById(Long traceId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoByIds(Long[] traceIds);


    public List<String> queryOrderAllState(Long orderId);

    
    public DispatchDriverInfo queryApplyReassignmentOrderInfo (Long orderId);
    
    public DispatchDriverInfo queryReassignmentOrderStatus(Long orderId);
    
    public List<SendCarInfo> queryStateInfo(Long orderId);

    /**查询当前登录人改派消息通知*/
    MessageDto getTraceMessageForPassenger(@Param("userId") Long userId, @Param("state")String state);
    /**查询当前司机改派消息通知*/
    MessageDto getTraceMessageForDriver(@Param("driverId") Long driverId, @Param("state")String state);

    UserVO getOrderDispatcher(@Param("orderId") Long orderId, @Param("states") String states);
    
    public OrderStateTraceInfo queryJouneyCloseOrderIsCanle(Long jouneyId);
    
    public OrderStateTraceInfo queryPowerCloseOrderIsCanle(Long powerId);

    OrderStateTraceInfo getLatestInfoByOrderId(Long orderId);

    /**
     * 根据订单id查询调度员的userId
     * @param orderId
     * @return
     */
    String selectDispatcherUserId(Long orderId);
    
    public OrderStateTraceInfo queryFirstDispatchIndo(Long orderId);
    
    
    public OrderStateTraceInfo queryRecentlyDispatchInfo(Long orderId);

    /**
     * 获取需要确认行程的数据
     * @return
     */
    public List<OrderStateTraceInfo> getExpiredConfirmOrder(@Param("timeout") int timeout);
    
    //查询发起改派申请的司机ID
    public Long queryApplyReassignmentDriver(@Param("orderId") Long orderId,@Param("state")String state);

    /**
     * 通过订单id和轨迹状态查找最近的一条信息
     * @param orderId
     * @param state
     * @return
     */
    OrderStateTraceInfo queryLatestInfoByOrderIdAndState(@Param("orderId") Long orderId,@Param("state") String state);

    /**
     * 通过订单id获取最近的改派状态
     * @param orderId
     * @return
     */
    String queryOrderLatestRessaignState(Long orderId);

    /**
     * 获取订单之前的调度员
     * @param orderId
     * @return
     */
    Long getOldDispatcher(Long orderId);

    /***
     * 查寻改派记录
     * @param state
     * @param orderId
     * @return
     */
    List<RejectDispatcherUserVO> reassignOrderList(@Param("orderId") Long orderId,@Param("state") String state);


    /***
     * 改派删除订单轨迹
     * add by liuzb
     * @param orderId
     * @return
     * @throws Exception
     */
    int deleteOrderStateTrace(@Param("orderId") Long orderId)throws Exception;

    int selectCountForAgainStrte(@Param("orderId") Long orderId,@Param("state")  String state);

    List<OrderStateTraceInfo> selectListByOrderState(@Param("orderId")Long orderId, @Param("state") String state, @Param("date") String date);

    List<PiclUpCarOrderVO> selectOrderListByState(String state);
}
