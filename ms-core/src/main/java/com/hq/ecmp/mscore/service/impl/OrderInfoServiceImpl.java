package com.hq.ecmp.mscore.service.impl;

import java.util.*;


import java.text.DateFormat;
import java.util.List;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
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

import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.OrderVO;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;
    @Resource
    private IDriverInfoService iDriverInfoService;
    @Resource
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserEmergencyContactInfoMapper userEmergencyContactInfoMapper;


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
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        List<UserEmergencyContactInfo> userEmergencyContactInfos = userEmergencyContactInfoMapper.queryAll(new UserEmergencyContactInfo(journeyInfo.getUserId()));
        if (CollectionUtils.isEmpty(userEmergencyContactInfos)){
            vo.setIsAddContact("是");
        }
        DriverInfo driverInfo = driverInfoService.selectDriverInfoById(orderInfo.getDriverId());
        vo.setDriverScore(driverInfo.getStar()+"");
        vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
        vo.setState(orderInfo.getState());
        //TODO 客服电话暂时写死
        vo.setCustomerServicePhone("010-88888888");
        JourneyNodeInfo nodeInfo = iJourneyNodeInfoService.selectJourneyNodeInfoById(orderInfo.getNodeId());
        vo.setUseCarTime(nodeInfo.getPlanSetoutTime());
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


    @Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean ownCarSendCar(Long orderId, Long driverId, Long carId,Long userId) {
		//查询司机信息
		DriverInfo driverInfo = driverInfoService.selectDriverInfoById(driverId);
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderId);
		orderInfo.setState(OrderState.ALREADYSENDING.getState());
		orderInfo.setDriverId(driverId);
		if(null !=driverInfo){
			orderInfo.setDriverName(driverInfo.getDriverName());
			orderInfo.setDriverMobile(driverInfo.getMobile());
		}
		orderInfo.setCarId(carId);
		orderInfo.setUpdateBy(String.valueOf(userId));
		orderInfo.setUpdateTime(new Date());
		//更新订单信息
		int updateFlag = updateOrderInfo(orderInfo);
		//新增订单状态流转记录
		 OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
		 orderStateTraceInfo.setCreateBy(String.valueOf(userId));
		 //判读该单子是否是改派单
		 if(iOrderStateTraceInfoService.isReassignment(orderId)){
			 orderStateTraceInfo.setState(OrderStateTrace.PASSREASSIGNMENT.getState());
		 } else{
			 orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
		 }

         orderStateTraceInfo.setOrderId(orderId);
         int insertFlag = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);

		return updateFlag>0&&insertFlag>0;
	}


    public void initOrder(Long applyId,Long jouneyId,Long userId){
        //获取用车权限记录
        JourneyUserCarPower journeyUserCarPowerChild = new JourneyUserCarPower();
        journeyUserCarPowerChild.setJourneyId(jouneyId);
        journeyUserCarPowerChild.setApplyId(applyId);
        List<JourneyUserCarPower> journeyUserCarPowers = journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPowerChild);
        //获取行程主表信息
        JourneyInfo journeyInfo = iJourneyInfoService.selectJourneyInfoById(jouneyId);
        //获取申请表信息
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        //插入订单初始信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setJourneyId(jouneyId);
        orderInfo.setDriverId(null);
        orderInfo.setCarId(null);
        //使用汽车的方式，自由和网约
        orderInfo.setUseCarMode(journeyInfo.getUseCarMode());
        orderInfo.setCreateBy(String.valueOf(userId));
        orderInfo.setCreateTime(new Date());
        String applyType = applyInfo.getApplyType();
        //如果是公务用车则状态直接为待派单，如果是差旅用车状态为初始化
        if ("A001".equals(applyType)) {
            orderInfo.setState(OrderState.WAITINGLIST.getState());
        } else {
            orderInfo.setState(OrderState.INITIALIZING.getState());
        }
        //有多少用车权限创建多少订单（注意往返以及差旅的室内用车）
        for (int i = 0; i < journeyUserCarPowers.size(); i++) {
            //通过行程节点与申请id以及行程id唯一确定用户权限id
            JourneyUserCarPower journeyUserCarPower = journeyUserCarPowers.get(i);
            String type = journeyUserCarPower.getType();
            //如果是市内用车跳过生成订单，在手动创建订单的时候生成
            if(CarConstant.CITY_USE_CAR.equals(type)){
                continue;
            }
            Long nodeId = journeyUserCarPower.getNodeId();
            Long powerId = journeyUserCarPower.getPowerId();
            orderInfo.setNodeId(nodeId);
            orderInfo.setPowerId(powerId);// TODO: 2020/3/3  权限表何时去创建？ 申请审批通过以后创建用车权限表记录，一个行程节点可能对应多个用车权限，比如往返超过固定时间
            orderInfoMapper.insertOrderInfo(orderInfo);
            //插入订单轨迹表
            this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId));
            if ("A001".equals(applyType)) {
                this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId));
            }
        }
    }
}
