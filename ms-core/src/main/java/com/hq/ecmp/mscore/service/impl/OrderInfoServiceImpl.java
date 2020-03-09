package com.hq.ecmp.mscore.service.impl;

import java.util.*;


import java.text.DateFormat;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;

import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderListInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;

import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.mscore.domain.*;

import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.OrderVO;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import com.hq.ecmp.mscore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderInfoServiceImpl implements IOrderInfoService
{
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Resource
    private IJourneyInfoService iJourneyInfoService;
    @Resource
    private IJourneyNodeInfoService iJourneyNodeInfoService;
    @Autowired
    private ICarInfoService carInfoService;
    @Autowired
    private ICarGroupInfoService carGroupInfoService;


    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Resource
    private IDriverInfoService iDriverInfoService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderInfo selectOrderInfoById(Long orderId)
    {
        return orderInfoMapper.selectOrderInfoById(orderId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo)
    {
        return orderInfoMapper.selectOrderInfoList(orderInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderInfo(OrderInfo orderInfo)
    {
        orderInfo.setCreateTime(DateUtils.getNowDate());
        return orderInfoMapper.insertOrderInfo(orderInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderInfo(OrderInfo orderInfo)
    {
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        return orderInfoMapper.updateOrderInfo(orderInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoByIds(Long[] orderIds)
    {
        return orderInfoMapper.deleteOrderInfoByIds(orderIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoById(Long orderId)
    {
        return orderInfoMapper.deleteOrderInfoById(orderId);
    }


    /**
     * 获取乘客订单列表
     * @param userId
     * @return
     */
    public List<OrderListInfo>  getOrderList(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<OrderListInfo> orderList = orderInfoMapper.getOrderList(userId);
        return orderList;
    }

    /**
     * 订单状态修改方法getOrderList
     * @param orderId
     * @param updateState
     * @return
     */
    public  int insertOrderStateTrace(String orderId,String updateState,String userId){
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(updateState);
        orderStateTraceInfo.setContent(null);
        orderStateTraceInfo.setCreateBy(userId);
        int i = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        return  i;
    }


	@Override
	public List<DispatchOrderInfo> queryWaitDispatchList() {
		List<DispatchOrderInfo> result=new ArrayList<DispatchOrderInfo>();
		//查询所有处于待派单的订单及关联的信息
		OrderInfo query = new OrderInfo();
		query.setState(OrderState.SENDINGCARS.getState());
		List<DispatchOrderInfo> waitDispatchOrder= orderInfoMapper.queryOrderRelateInfo(query);
		if(null !=waitDispatchOrder && waitDispatchOrder.size()>0){
			result.addAll(waitDispatchOrder);
		}
		//查询所有处于待改派(订单状态为已派车,已发起改派申请)的订单及关联的信息
		query.setState(OrderState.ALREADYSENDING.getState());
		query.setOrderTraceState(OrderStateTrace.APPLYREASSIGNMENT.getState());
		List<DispatchOrderInfo> reassignmentOrder = orderInfoMapper.queryOrderRelateInfo(query);
		if(null !=reassignmentOrder && reassignmentOrder.size()>0){
			for (DispatchOrderInfo dispatchOrderInfo : reassignmentOrder) {
				dispatchOrderInfo.setState(OrderState.REASSIGNMENT.getState());
			}
			result.addAll(reassignmentOrder);
		}
		return result;
	}

	@Override
	public List<DispatchOrderInfo> queryCompleteDispatchOrder() {
		//获取系统里已经完成调度的订单
		return orderInfoMapper.queryCompleteDispatchOrder();
	}

    /**
     * 获取司机的任务列表
     * @param userId
     * @return
     */
    @Override
    public List<OrderDriverListInfo> getDriverOrderList(Long userId,int pageNum, int pageSize) {
        DriverInfo driverInfoCondition = new DriverInfo();
        driverInfoCondition.setUserId(userId);
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(driverInfoCondition);
        DriverInfo driverInfo = driverInfos.get(0);
        Long driverId = driverInfo.getDriverId();
        PageHelper.startPage(pageNum,pageSize);
        List<OrderDriverListInfo> driverOrderList = orderInfoMapper.getDriverOrderList(driverId);
        return driverOrderList;
    }

	@Override
	public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId) {
		DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.getWaitDispatchOrderDetailInfo(orderId);
		//判断该订单是否改派过
		if(iOrderStateTraceInfoService.isReassignment(orderId)){
			//是改派过的单子  则查询改派详情
			DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryDispatchDriverInfo(orderId);
			dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
		}
		return dispatchOrderInfo;
	}

	@Override
	public DispatchOrderInfo getCompleteDispatchOrderDetailInfo(Long orderId) {
		DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.queryCompleteDispatchOrderDetail(orderId);
		if(iOrderStateTraceInfoService.isReassignment(orderId)){
			//是改派过的单子  则查询改派详情
			DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryDispatchDriverInfo(orderId);
			dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
		}
		//查询派车信息
		List<SendCarInfo> sendCarInfoList = iOrderStateTraceInfoService.queryStateInfo(orderId);
		dispatchOrderInfo.setSendCarInfoList(sendCarInfoList);
		return dispatchOrderInfo;
	}

    @Override
    public OrderVO orderBeServiceDetail(Long orderId) {
        OrderVO vo=new OrderVO();
        OrderInfo orderInfo = this.selectOrderInfoById(orderId);
        if (orderInfo==null){
            return null;
        }
        BeanUtils.copyProperties(orderInfo,vo);
        //查询车辆信息
        CarInfo carInfo = carInfoService.selectCarInfoById(orderInfo.getCarId());
        if (carInfo!=null){
            BeanUtils.copyProperties(carInfo,vo);
        }
        vo.setPowerType(CarPowerEnum.format(carInfo.getPowerType()));
        //TODO 是否需要车队信息
        //是否添加联系人
//        DriverInfo driverInfo = driverInfoService.selectDriverInfoById(orderInfo.getDriverId());
        vo.setDriverScore("4.5");
        vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
        vo.setState(orderInfo.getState());
        //TODO 客服电话暂时写死
        vo.setCustomerServicePhone("010-88888888");
        return vo;
    }

    /**
     * 轮询获取提示语
     * @param orderId
     * @return
     */
    @Override
    public String orderHint(Long orderId) {
        OrderInfo orderInfo = this.selectOrderInfoById(orderId);
        if (orderInfo==null){
            return null;
        }
        String format = HintEnum.format(orderInfo.getState());
        return String.format(format, DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,orderInfo.getActualSetoutTime()));
    }

    @Override
    public MessageDto getOrderMessage(Long userId,String states,Long driveId) {
        return orderInfoMapper.getOrderMessage(userId,states,driveId);
    }

    @Override
    @Async
    public void platCallTaxi(OrderInfo orderInfo,String enterpriseId,String licenseContent,String apiUrl) {
        Long orderId = orderInfo.getOrderId();
        try {
            //MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            //调用网约车约车接口参数
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            //调用查询订单状态的接口参数
            Map<String,String> queryOrderStateMap = new HashMap<>();
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", macAdd);
            for(;;){
//                String result = OkHttpUtil.postJson(apiUrl + "/service/applyPlatReceiveOrder", paramMap);
//                JSONObject jsonObject = JSONObject.parseObject(result);
//                if(!"0".equals(jsonObject.getString("code"))){
//                    throw new Exception("约车失败");
//                }
                for (int i = 0; i <3 ; i++) {
                    redisUtil.increment(CommonConstant.APPOINTMENT_NUMBER_PREFIX+orderId+"",1L);
                    Thread.sleep(60000);
                }

//                //调用查询订单状态的方法
//                String resultQuery = OkHttpUtil.postJson(apiUrl + "/service/applyPlatReceiveOrder", paramMap);
//                JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
//                if(!"0".equals(jsonObjectQuery.getString("code"))){
//                    throw new Exception("约车失败");
//                }
//                //判断状态,如果约到车修改状态为已派单
//                Object data = jsonObjectQuery.get("data");
//                if(data.equals("")){
//                    orderInfo.setState(OrderState.ALREADYSENDING.getState());
//                    int j = orderInfoMapper.updateOrderInfo(orderInfo);
//                    if (j != 1) {
//                        throw new Exception("约车失败");
//                    }
//                    break;
//                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            redisUtil.delete(CommonConstant.APPOINTMENT_NUMBER_PREFIX+orderId+"");
        }
    }
}
