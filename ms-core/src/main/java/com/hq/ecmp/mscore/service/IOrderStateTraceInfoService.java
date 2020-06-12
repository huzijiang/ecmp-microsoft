package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.domain.DispatchDriverInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.ReassignInfo;
import com.hq.ecmp.mscore.domain.SendCarInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.dto.UseCarDataByGroupDto;
import com.hq.ecmp.mscore.dto.UseCarDataDto;
import com.hq.ecmp.mscore.dto.UserDeptUseCarDetailDto;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.StatisticsForAdminDetailVo;
import com.hq.ecmp.mscore.vo.UseCarDataVo;
import com.hq.ecmp.mscore.vo.UserVO;

import java.text.ParseException;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderStateTraceInfoService
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
     * 批量删除【请填写功能名称】
     *
     * @param traceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoByIds(Long[] traceIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderStateTraceInfoById(Long traceId);

    /**
     * 订单是否改派过
     * @param orderId
     * @return
     */
    public boolean isReassignment(Long orderId);

    /**
     * 查询订单所有的流转状态
     * @param orderId
     * @return
     */
    public List<String> queryOrderAllState(Long orderId);

    /**
     * 查询驾驶员发起改派的信息
     * @return
     */
    public DispatchDriverInfo queryReassignmentOrderInfo(Long orderId);
    
    /**
     * 查询调度已完成的订单派车信息
     * @param orderId
     * @return
     */
    List<SendCarInfo> queryStateInfo(Long orderId);


    /**
     * 司机申请改派
     * @param userId  司机编号
     * @param orderId
     * @param applyReason
     * @return
     */
    public boolean applyReassignment(Long userId,Long orderId,String applyReason);

    public List<ReassignInfo> queryReassignDetail(Long orderNo);
    /**
     *
     * @param userId 用户id
     * @param flag 角色(true 司机,false乘客端)
     * @return
     */
    MessageDto getTraceMessage(Long userId,boolean flag,Long driverId);

    UserVO getOrderDispatcher(String states, Long orderId);
    
    /**
     * 查询行程里订单关闭的订单
     * @param journeyId
     * @return
     */
    public OrderStateTraceInfo queryJourneyOrderIsCancel(Long journeyId);
    
    public OrderStateTraceInfo queryFirstDispatchIndo(Long orderId);
    
    public OrderStateTraceInfo queryRecentlyDispatchInfo(Long orderId);

    UseCarDataVo selectDeptUseCarData(UseCarDataDto useCarDataDto, LoginUser loginUser) throws ParseException;

    PageResult<UseCarDataVo> userDeptUseCarDataByCarGroup(UseCarDataByGroupDto useCarDataByGroupDto, LoginUser loginUser);

    PageResult<StatisticsForAdminDetailVo> userDeptUseCarDetail(UserDeptUseCarDetailDto userDeptUseCarDetailDto, LoginUser loginUser);
}
