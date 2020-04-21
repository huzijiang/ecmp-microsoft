package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.EcmpMessageMapper;
import com.hq.ecmp.mscore.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;

import javax.annotation.Resource;

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
	@Autowired
	private EcmpMessageMapper ecmpMessageMapper;

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
	public DispatchDriverInfo queryReassignmentOrderInfo(Long orderId) {
		//获取订单改派的申请信息
		DispatchDriverInfo applyReassignment = orderStateTraceInfoMapper.queryApplyReassignmentOrderInfo(orderId);
		if(null !=applyReassignment){
			//获取改订单改派的最新状态
			DispatchDriverInfo dispatchDriverInfo = orderStateTraceInfoMapper.queryReassignmentOrderStatus(orderId);
			if(null !=dispatchDriverInfo){
				applyReassignment.setState(dispatchDriverInfo.getState());
				if(OrderStateTrace.TURNREASSIGNMENT.getState().equals(dispatchDriverInfo.getState())){
					//如果已经被驳回
					applyReassignment.setRejectReason(dispatchDriverInfo.getContent());
				}
			}
			
		}
		return applyReassignment;
	}

	@Override
	public List<SendCarInfo> queryStateInfo(Long orderId) {
		return orderStateTraceInfoMapper.queryStateInfo(orderId);
	}


	@Override
    public MessageDto getTraceMessage(Long userId,boolean flag,Long driverId) {
        if (flag){
            return orderStateTraceInfoMapper.getTraceMessageForDriver(driverId, OrderState.REASSIGNPASS.getState());
        }else{
            return orderStateTraceInfoMapper.getTraceMessageForPassenger(userId,OrderState.REASSIGNPASS.getState());
        }
    }

	@Override
	public UserVO getOrderDispatcher(String states, Long orderId) {
		return orderStateTraceInfoMapper.getOrderDispatcher(orderId,states);
	}

	@Override
	public boolean applyReassignment(Long userId, Long orderId, String applyReason) {
		OrderStateTraceInfo orderStateTraceInfo =new OrderStateTraceInfo();
		orderStateTraceInfo.setOrderId(orderId);
		orderStateTraceInfo.setCreateBy(userId.toString());
		orderStateTraceInfo.setCreateTime(new Date());
		orderStateTraceInfo.setContent(applyReason);
		orderStateTraceInfo.setState(OrderStateTrace.APPLYREASSIGNMENT.getState());
		int num= orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
		//插入改派通知
		if(num>0){
			EcmpMessage ecmpMessage = new EcmpMessage();
			//司机
			ecmpMessage.setConfigType(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
			//用户类型Id
			ecmpMessage.setEcmpId(userId);
			//消息类型
			ecmpMessage.setType(MsgTypeConstant.MESSAGE_TYPE_T001.getType());
			//消息状态
			ecmpMessage.setStatus(MsgStatusConstant.MESSAGE_STATUS_T002.getType());
			//内容
			ecmpMessage.setContent("您有一条任务被改派，请及时查看！");
			//消息类别
			ecmpMessage.setCategory(MsgConstant.MESSAGE_T004.getType());
			//对应的消息业务id
			ecmpMessage.setCategoryId(orderId);
			//创建人
			ecmpMessage.setCreateBy(userId);
			//创建时间
			ecmpMessage.setCreateTime(DateUtils.getNowDate());
			ecmpMessageMapper.insert(ecmpMessage);
		}
		return true;
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

	@Override
	  public OrderStateTraceInfo queryJourneyOrderIsCancel(Long journeyId){
		return orderStateTraceInfoMapper.queryJouneyCloseOrderIsCanle(journeyId);
		
		
	}

	@Override
	public OrderStateTraceInfo queryFirstDispatchIndo(Long orderId) {
		return orderStateTraceInfoMapper.queryFirstDispatchIndo(orderId);
	}

	@Override
	public OrderStateTraceInfo queryRecentlyDispatchInfo(Long orderId) {
		return orderStateTraceInfoMapper.queryRecentlyDispatchInfo(orderId);
	}
}
