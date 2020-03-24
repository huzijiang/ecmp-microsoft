package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DispatchDriverInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.SendCarInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
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

    
    public DispatchDriverInfo queryReassignmentOrderInfo (Long orderId);
    
    public List<SendCarInfo> queryStateInfo(Long orderId);

    /**查询当前登录人改派消息通知*/
    MessageDto getTraceMessageForPassenger(@Param("userId") Long userId, @Param("state")String state);
    /**查询当前司机改派消息通知*/
    MessageDto getTraceMessageForDriver(@Param("driverId") Long driverId, @Param("state")String state);

    UserVO getOrderDispatcher(@Param("orderId") Long orderId, @Param("states") String states);
    
    public OrderStateTraceInfo queryJouneyCloseOrderIsCanle(Long jouneyId);
    
    public OrderStateTraceInfo queryPowerCloseOrderIsCanle(Long powerId);

    OrderStateTraceInfo getLatestInfoByOrderId(Long orderId);
}
