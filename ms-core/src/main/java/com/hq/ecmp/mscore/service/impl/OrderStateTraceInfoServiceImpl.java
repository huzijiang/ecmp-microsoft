package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.DispatchDriverInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.ReassignInfo;
import com.hq.ecmp.mscore.domain.SendCarInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderStateTraceInfoServiceImpl implements IOrderStateTraceInfoService
{
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private IDriverInfoService iDriverInfoService;

    /**
     * 查询【请填写功能名称】
     *
     * @param traceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderStateTraceInfo selectOrderStateTraceInfoById(Long traceId)
    {
        return orderStateTraceInfoMapper.selectOrderStateTraceInfoById(traceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderStateTraceInfo> selectOrderStateTraceInfoList(OrderStateTraceInfo orderStateTraceInfo)
    {
        return orderStateTraceInfoMapper.selectOrderStateTraceInfoList(orderStateTraceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo)
    {
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        return orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderStateTraceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderStateTraceInfo(OrderStateTraceInfo orderStateTraceInfo)
    {
        orderStateTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return orderStateTraceInfoMapper.updateOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param traceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderStateTraceInfoByIds(Long[] traceIds)
    {
        return orderStateTraceInfoMapper.deleteOrderStateTraceInfoByIds(traceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param traceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderStateTraceInfoById(Long traceId)
    {
        return orderStateTraceInfoMapper.deleteOrderStateTraceInfoById(traceId);
    }

	@Override
	public boolean isReassignment(Long orderId) {
		List<String> list = queryOrderAllState(orderId);
		if(null !=list && list.size()>0){
			if(list.contains(OrderStateTrace.APPLYREASSIGNMENT.getState())){
				return true;
			}

		}
		return false;
	}

	@Override
	public List<String> queryOrderAllState(Long orderId) {

		return orderStateTraceInfoMapper.queryOrderAllState(orderId);
	}

	@Override
	public DispatchDriverInfo queryDispatchDriverInfo(Long orderId) {
		return orderStateTraceInfoMapper.queryDispatchDriverInfo(orderId);
	}

	@Override
	public List<SendCarInfo> queryStateInfo(Long orderId) {
		return orderStateTraceInfoMapper.queryStateInfo(orderId);
	}

    @Override
    public MessageDto getTraceMessage(Long userId) {
        return orderStateTraceInfoMapper.getTraceMessage(userId,"S279");
    }

	@Override
	public boolean applyReassignment(Long userId, Long orderId, String applyReason) {
		OrderStateTraceInfo orderStateTraceInfo =new OrderStateTraceInfo();
		orderStateTraceInfo.setOrderId(orderId);
		orderStateTraceInfo.setCreateBy(userId.toString());
		orderStateTraceInfo.setCreateTime(new Date());
		orderStateTraceInfo.setContent(applyReason);
		orderStateTraceInfo.setState(OrderStateTrace.APPLYREASSIGNMENT.getState());
		return orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo)>0;
	}

	@Override
	public List<ReassignInfo> queryReassignDetail(Long orderNo) {
		List<ReassignInfo> list=new ArrayList<ReassignInfo>();
		OrderStateTraceInfo orderStateTraceInfo =new OrderStateTraceInfo();
		orderStateTraceInfo.setOrderId(orderNo);
		orderStateTraceInfo.setState(OrderStateTrace.APPLYREASSIGNMENT.getState());
		List<OrderStateTraceInfo> orderStateTraceInfoList = selectOrderStateTraceInfoList(orderStateTraceInfo);
		if(null !=orderStateTraceInfoList && orderStateTraceInfoList.size()>0){
			for (OrderStateTraceInfo o: orderStateTraceInfoList) {
				ReassignInfo reassignInfo = new ReassignInfo();
				reassignInfo.setReassignReason(o.getContent());;
				reassignInfo.setApplyDate(o.getCreateTime());
				//查询司机信息
				DriverInfo driverInfo = iDriverInfoService.selectDriverInfoById(Long.valueOf(o.getCreateBy()));
				if(null !=driverInfo){
					reassignInfo.setDriverName(driverInfo.getDriverName());
					reassignInfo.setDriverPhone(driverInfo.getMobile());
				}
				list.add(reassignInfo);
			}
		}
		
		return list;
	}
}
