package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.OrderUtils;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements IOrderInfoService
{
	@Autowired
	private CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Resource
    private IJourneyInfoService iJourneyInfoService;
    @Resource
    private IJourneyNodeInfoService iJourneyNodeInfoService;
    @Autowired
    private ICarInfoService carInfoService;
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
    @Resource
    private IOrderViaInfoService iOrderViaInfoService;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private ICarGroupDispatcherInfoService carGroupDispatcherInfoService;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private IJourneyPassengerInfoService journeyPassengerInfoService;
    @Resource
    private IOrderAddressInfoService iOrderAddressInfoService;
    @Resource
    private IJourneyPlanPriceInfoService iJourneyPlanPriceInfoService;
    @Resource
    private IDriverHeartbeatInfoService iDriverHeartbeatInfoService;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;
    @Resource
    ThirdService thirdService;
    @Resource
    private IEcmpConfigService ecmpConfigService;
    @Resource
    private DriverServiceAppraiseeInfoMapper driverServiceAppraiseeInfoMapper;
    @Resource
    private EcmpMessageService ecmpMessageService;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private IsmsBusiness ismsBusiness;


    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;
    @Value("${company.serviceMobile}")
    private String serviceMobile;

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
    public  int insertOrderStateTrace(String orderId,String updateState,String userId,String cancelReason){
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(updateState);
        orderStateTraceInfo.setContent(cancelReason);
        orderStateTraceInfo.setCreateBy(userId);
        int i = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        return  i;
    }


	@Override
	public List<DispatchOrderInfo> queryWaitDispatchList(Long userId) {
		List<DispatchOrderInfo> result=new ArrayList<DispatchOrderInfo>();
		//查询所有处于待派单(未改派)的订单及关联的信息
		OrderInfo query = new OrderInfo();
		query.setState(OrderState.WAITINGLIST.getState());
		List<DispatchOrderInfo> waitDispatchOrder= orderInfoMapper.queryOrderRelateInfo(query);
		if(null !=waitDispatchOrder && waitDispatchOrder.size()>0){
			//对用的前端状态都为待派车 -S200
			for (DispatchOrderInfo dispatchOrderInfo : waitDispatchOrder) {
				//去除改派的单子
				if(iOrderStateTraceInfoService.isReassignment(dispatchOrderInfo.getOrderId())){
					continue;
				}
				//正常申请的调度单取对应的用车申请单的通过时间来排序
				dispatchOrderInfo.setUpdateDate(dispatchOrderInfo.getApplyPassDate());
				dispatchOrderInfo.setState(OrderState.WAITINGLIST.getState());
				result.add(dispatchOrderInfo);
			}
		}
		//查询所有处于待改派(订单状态为已派车,已发起改派申请)的订单及关联的信息
		query.setState(OrderState.ALREADYSENDING.getState());
		query.setOrderTraceState(OrderStateTrace.APPLYREASSIGNMENT.getState());
		List<DispatchOrderInfo> reassignmentOrder = orderInfoMapper.queryOrderRelateInfo(query);
		if(null !=reassignmentOrder && reassignmentOrder.size()>0){
			for (DispatchOrderInfo dispatchOrderInfo : reassignmentOrder) {
				dispatchOrderInfo.setState(OrderState.APPLYREASSIGN.getState());
			}
			result.addAll(reassignmentOrder);
		}
		/*对用户进行订单可见校验
		自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的调度员能看到该订单
	      只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单*/
		List<DispatchOrderInfo> checkResult=new ArrayList<DispatchOrderInfo>();
		if(result.size()>0){
			for (DispatchOrderInfo dispatchOrderInfo : result) {
				//查询订单对应的上车地点时间,下车地点时间
				buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
				//查询订单对应制度的可用用车方式
				Long regimenId = dispatchOrderInfo.getRegimenId();
				if(null ==regimenId || ! regimeInfoService.findOwnCar(regimenId)){
					checkResult.add(dispatchOrderInfo);
					continue;
				}
				String cityId = dispatchOrderInfo.getCityId();
				if(StringUtil.isEmpty(cityId)){
					checkResult.add(dispatchOrderInfo);
					continue;
				}
				//查询上车地点对应城市有哪些车队
				List<Long> carGroupList = carGroupServeScopeInfoMapper.queryCarGroupByCity(cityId);
				if(null==carGroupList || carGroupList.size()==0){
					//上车点所在城市没有车队  则所有车队的所欲调度员都能看见这个单子
					checkResult.add(dispatchOrderInfo);
					continue;
				}
				//判断登陆用户是否属于这些车队
				List<Long> carGroupDispatcher = carGroupDispatcherInfoService.queryUserByCarGroup(carGroupList);
				if(null==carGroupDispatcher || !carGroupDispatcher.contains(userId)){
					//用户不属于这些车队的调度员  则不能看见这个订单
					continue;
				}
				checkResult.add(dispatchOrderInfo);
			}
		}
		return checkResult;
	}

	//// 查询订单对应的上车地点时间,下车地点时间  A000-上车     A999-下车
	private void buildOrderStartAndEndSiteAndTime(DispatchOrderInfo dispatchOrderInfo) {
		OrderAddressInfo startOrderAddressInfo = iOrderAddressInfoService
				.queryOrderStartAndEndInfo(new OrderAddressInfo("A000", dispatchOrderInfo.getOrderId()));
		if (null != startOrderAddressInfo) {
			dispatchOrderInfo.setStartSite(startOrderAddressInfo.getAddress());
			dispatchOrderInfo.setUseCarDate(startOrderAddressInfo.getActionTime());
		}
		OrderAddressInfo endOrderAddressInfo = iOrderAddressInfoService
				.queryOrderStartAndEndInfo(new OrderAddressInfo("A999", dispatchOrderInfo.getOrderId()));
		if (null != endOrderAddressInfo) {
			dispatchOrderInfo.setEndSite(endOrderAddressInfo.getAddress());
			dispatchOrderInfo.setEndDate(endOrderAddressInfo.getActionTime());
		}

	}

	@Override
	public List<DispatchOrderInfo> queryCompleteDispatchOrder() {
		//获取系统里已经完成调度的订单
		List<DispatchOrderInfo> list = orderInfoMapper.queryCompleteDispatchOrder();
		if(null !=list && list.size()>0){
			for (DispatchOrderInfo dispatchOrderInfo : list) {
				//对应的前端状态都为已处理-S299
				dispatchOrderInfo.setState(OrderState.ALREADYSENDING.getState());
				//查询订单对应的上车地点时间,下车地点时间
				buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
			}
		}
		return list;
	}

    /**
     * 获取司机的任务列表
     * @param userId
     * @return
     */
    @Override
    public List<OrderDriverListInfo> getDriverOrderList(Long userId,int pageNum, int pageSize) throws Exception{
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(new DriverInfo(userId));
        if (CollectionUtils.isEmpty(driverInfos)){
            throw new Exception("当前登录人不是司机");
        }
        DriverInfo driverInfo = driverInfos.get(0);
        Long driverId = driverInfo.getDriverId();
        int flag=0;
        if (pageNum==1){//首次刷新
            String states=OrderState.ALREADYSENDING.getState()+","+OrderState.REASSIGNMENT.getState()+","+OrderState.READYSERVICE.getState()+","+OrderState.INSERVICE.getState();
            int count=orderInfoMapper.getDriverOrderCount(driverId,states);
            if(count>20){
                flag=1;
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        List<OrderDriverListInfo> driverOrderList = orderInfoMapper.getDriverOrderList(driverId,flag);
        return driverOrderList;
    }

    @Override
    public Integer getDriverOrderListCount(Long userId) throws Exception{
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(new DriverInfo(userId));
        if (CollectionUtils.isEmpty(driverInfos)){
            throw new Exception("当前登录人不是司机");
        }
        DriverInfo driverInfo = driverInfos.get(0);
        Long driverId = driverInfo.getDriverId();
        String states=OrderState.ALREADYSENDING.getState()+","+OrderState.REASSIGNMENT.getState()+","+OrderState.READYSERVICE.getState()+","+OrderState.INSERVICE.getState()+","+OrderState.ORDERCLOSE.getState();
        return orderInfoMapper.getDriverOrderListCount(driverId,states);
    }

	@Override
	public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId) {
		DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.getWaitDispatchOrderDetailInfo(orderId);
		dispatchOrderInfo.setState(OrderState.WAITINGLIST.getState());
		//查询订单对应的上车地点时间,下车地点时间
		buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
		//判断该订单是否改派过
		if(iOrderStateTraceInfoService.isReassignment(orderId)){
			//是改派过的单子  则查询改派详情
			DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryReassignmentOrderInfo(orderId);
			dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
			//改派过的订单对应前端状态 待改派-S270
			dispatchOrderInfo.setState(OrderState.APPLYREASSIGN.getState());
		}
		return dispatchOrderInfo;
	}

	@Override
	public DispatchOrderInfo getCompleteDispatchOrderDetailInfo(Long orderId) {
		DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.queryCompleteDispatchOrderDetail(orderId);
		//对应前端状态都为已处理-S299 
		dispatchOrderInfo.setState(OrderState.ALREADYSENDING.getState());
		//查询订单对应的上车地点时间,下车地点时间
		buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
		if(iOrderStateTraceInfoService.isReassignment(orderId)){
			//是改派过的单子  则查询改派详情
			DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryReassignmentOrderInfo(orderId);
			dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
		}
		//查询派车信息
		List<SendCarInfo> sendCarInfoList = iOrderStateTraceInfoService.queryStateInfo(orderId);
		dispatchOrderInfo.setSendCarInfoList(sendCarInfoList);
		return dispatchOrderInfo;
	}

    @Override
    public OrderVO orderBeServiceDetail(Long orderId) throws Exception{
        OrderVO vo=new OrderVO();
        OrderInfo orderInfo = this.selectOrderInfoById(orderId);
        if (orderInfo==null){
            throw new Exception("该订单不存在");
        }
        JourneyNodeInfo nodeInfo = iJourneyNodeInfoService.selectJourneyNodeInfoById(orderInfo.getNodeId());
        BeanUtils.copyProperties(orderInfo,vo);
        String useCarTime=null;
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId, OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT));
        if (!CollectionUtils.isEmpty(orderAddressInfos)){
            useCarTime=DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,orderAddressInfos.get(0).getActionTime());
            vo.setUseCarTimestamp(orderAddressInfos.get(0).getActionTime().getTime());
        }
        vo.setUseCarTime(useCarTime);
        vo.setCreateTimestamp(orderInfo.getCreateTime().getTime());
        if (OrderState.WAITINGLIST.getState().equals(orderInfo.getState())||OrderState.GETARIDE.getState().equals(orderInfo.getState())){
            return vo;
        }
        if (OrderState.SENDINGCARS.getState().equals(orderInfo.getState())){
            vo.setHint(HintEnum.CALLINGCAR.join(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,nodeInfo.getPlanSetoutTime())));
            return vo;
        }
        if (OrderState.ORDEROVERTIME.getState().equals(orderInfo.getState())){
            vo.setHint(HintEnum.CALLCARFAILD.join(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,nodeInfo.getPlanSetoutTime())));
            return vo;
        }
        String states=OrderState.ALREADYSENDING.getState()+","+ OrderState.REASSIGNPASS.getState();
        UserVO str= orderStateTraceInfoMapper.getOrderDispatcher(orderId,states);
        vo.setCarGroupPhone(str.getUserPhone());
        vo.setOrderNumber(orderInfo.getOrderNumber());
        vo.setCarGroupName(str.getUserName());
        vo.setCustomerServicePhone(serviceMobile);
        vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        //服务结束时间
        OrderStateTraceInfo orderStateTraceInfo= orderStateTraceInfoMapper.getLatestInfoByOrderId(orderId);
        vo.setLabelState(orderStateTraceInfo.getState());
        if(orderStateTraceInfo!=null||OrderStateTrace.SERVICEOVER.getState().equals(orderStateTraceInfo.getState())){
            vo.setOrderEndTime(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,orderStateTraceInfo.getCreateTime()));
        }
       List<UserEmergencyContactInfo> contactInfos = userEmergencyContactInfoMapper.queryAll(new UserEmergencyContactInfo(journeyInfo.getUserId()));
        String isAddContact=CollectionUtils.isEmpty(contactInfos)?CommonConstant.SWITCH_ON:CommonConstant.SWITCH_OFF;
        vo.setIsAddContact(isAddContact);
        //TODO 查询企业配置是否自动行程确认/异议
        int orderConfirmStatus = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey());
        int isVirtualPhone = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigKey());//是否号码保护
        vo.setIsDisagree(orderConfirmStatus);
        vo.setIsVirtualPhone(isVirtualPhone);
        List<DriverServiceAppraiseeInfo> driverServiceAppraiseeInfos = driverServiceAppraiseeInfoMapper.queryAll(new DriverServiceAppraiseeInfo(orderInfo.getOrderId()));
        if (!CollectionUtils.isEmpty(driverServiceAppraiseeInfos)){
            vo.setDescription(driverServiceAppraiseeInfos.get(0).getContent());
            vo.setScore(driverServiceAppraiseeInfos.get(0).getScore()+"");
        }else{
            vo.setScore(null);
            vo.setDuration(null);
        }
        if(CarModeEnum.ORDER_MODE_HAVE.getKey().equals(orderInfo.getUseCarMode())){//自有车
            vo.setDriverId(orderInfo.getDriverId());
            vo.setCardId(orderInfo.getCarId());
            //查询车辆信息
            CarInfo carInfo = carInfoService.selectCarInfoById(orderInfo.getCarId());
            if (carInfo!=null){
                BeanUtils.copyProperties(carInfo,vo);
                vo.setPowerType(CarPowerEnum.format(carInfo.getPowerType()));
            }
            DriverInfo driverInfo = driverInfoService.selectDriverInfoById(orderInfo.getDriverId());
            vo.setDriverScore(driverInfo.getStar()+"");
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState())||OrderState.DISSENT.getState().equals(orderInfo.getState())){
                //服务结束后获取里程用车时长
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
                if (!CollectionUtils.isEmpty(orderSettlingInfos)){
                    vo.setDistance(orderSettlingInfos.get(0).getTotalMileage().stripTrailingZeros().toPlainString()+"公里");
                    vo.setDuration(DateFormatUtils.formatMinute(orderSettlingInfos.get(0).getTotalTime()));
                    vo.setAmount(orderSettlingInfos.get(0).getAmount().toPlainString());
                }
            }
        }else{
                vo.setCarLicense(orderInfo.getCarLicense());//车牌号
                vo.setCarColor(orderInfo.getCarColor());
                vo.setCarType(orderInfo.getCarModel());
                vo.setDriverScore(orderInfo.getDriverGrade());
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState())||OrderState.DISSENT.getState().equals(orderStateTraceInfo.getState())){
                OrderCostDetailVO orderCost = this.getOrderCost(orderId);
                vo.setOrderCostDetailVO(orderCost);
            }
        }
        vo.setState(orderInfo.getState());
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
        //TODO 杨军注释
//        return String.format(format, DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,orderInfo.getActualSetoutTime()));
        return null;
    }

    @Override
    public MessageDto getOrderMessage(Long userId,String states,Long driveId) {
        return orderInfoMapper.getOrderMessage(userId,states,driveId);
    }

    @Override
    @Async
    public void platCallTaxi(Long  orderId, String enterpriseId, String licenseContent, String apiUrl,String userId,String carLevel) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        try {
            boolean reassignment = iOrderStateTraceInfoService.isReassignment(orderId);
            //改派的订单需要操作改派同意
            if(reassignment){
                OrderInfo orderInfoRe = new OrderInfo();
                orderInfoRe.setState(OrderState.WAITINGLIST.getState());
                orderInfoRe.setUpdateBy(String.valueOf(userId));
                orderInfoRe.setOrderId(orderId);
                orderInfoRe.setUpdateTime(DateUtils.getNowDate());
                orderInfoMapper.updateOrderInfo(orderInfoRe);
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                orderStateTraceInfo.setCreateBy(String.valueOf(userId));
                orderStateTraceInfo.setState(ResignOrderTraceState.AGREE.getState());
                orderStateTraceInfo.setOrderId(orderId);
                iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
            }

            //MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            //调用网约车约车接口参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put("enterpriseOrderId",orderId+"");
            paramMap.put("payFlag", "0");
            //目前状态固定为S200
            paramMap.put("status","S200");
            OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setOrderId(orderId);
            long timeSt = 0L;
            //三字码
            String icaoCode = null;
            String serviceType = orderInfoOld.getServiceType();
            Long userIdOrder = orderInfoOld.getUserId();
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userIdOrder);
            paramMap.put("riderName",ecmpUser.getUserName());
            paramMap.put("riderPhone",ecmpUser.getPhonenumber());
            paramMap.put("groupIds","P001");
//            if(carLevel != null){
//                paramMap.put("groupIds",carLevel);
//            }else{
//                paramMap.put("groupIds",orderInfoOld.getDemandCarLevel());
//            }
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfo);
            for (int i = 0; i < orderAddressInfos.size() ; i++) {
                OrderAddressInfo orderAddressInfo1 = orderAddressInfos.get(i);
                //出发地址
                if(orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)){
                    if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP)){
                        icaoCode = orderAddressInfo1.getIcaoCode();
                    }
                    timeSt = orderAddressInfo1.getActionTime().getTime();
                    paramMap.put("cityId",orderAddressInfo1.getCityPostalCode());
                    paramMap.put("bookingDate",(orderAddressInfo1.getActionTime().getTime()+"").substring(0,10));
                    paramMap.put("bookingStartPointLo",orderAddressInfo1.getLongitude()+"");
                    paramMap.put("bookingStartPointLa",orderAddressInfo1.getLatitude()+"");
                    paramMap.put("bookingStartAddr", URLEncoder.encode(orderAddressInfo1.getAddress()));
                }else if(orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)){//到达地址
                    paramMap.put("bookingEndPointLo",orderAddressInfo1.getLongitude()+"");
                    paramMap.put("bookingEndPointLa",orderAddressInfo1.getLatitude()+"");
                    paramMap.put("bookingEndAddr", URLEncoder.encode(orderAddressInfo1.getAddress()));
                }
            }


            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            journeyPlanPriceInfo.setJourneyId(orderInfoOld.getJourneyId());
            journeyPlanPriceInfo.setNodeId(orderInfoOld.getNodeId());
            List<JourneyPlanPriceInfo> journeyPlanPriceInfos = iJourneyPlanPriceInfoService.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
            if(journeyPlanPriceInfos.size()>0){
                paramMap.put("estimatedAmount",journeyPlanPriceInfos.get(0).getPrice()+"");
            }


            //调用查询订单状态的接口参数
            Map<String,Object> queryOrderStateMap = new HashMap<>();
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", macAdd);
            queryOrderStateMap.put("enterpriseOrderId",orderId+"");
            queryOrderStateMap.put("status","S200");
            for(;;){
                if((DateUtils.getNowDate().getTime()/1000)>=timeSt){
                    //订单超时退出循环
                    orderInfo.setState(OrderState.ORDERCLOSE.getState());
                    int j = orderInfoMapper.updateOrderInfo(orderInfo);
                    OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                    orderStateTraceInfo.setOrderId(orderId);
                    orderStateTraceInfo.setState(OrderStateTrace.ORDEROVERTIME.getState());
                    orderStateTraceInfo.setCreateBy(userId);
                    iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
                    if (j != 1) {
                        throw new Exception("约车失败");
                    }
                    //超时短信通知
                    ismsBusiness.sendSmsCallTaxiNetFail(orderId);
                    break;
                }
                OrderInfo orderInfoPre = orderInfoMapper.selectOrderInfoById(orderId);
                String state = orderInfoPre.getState();
                //订单取消/超时/关闭 则退出循环
                if(state.equals(OrderState.ORDERCLOSE.getState())){
                    break;
                }

                //发起约车

                //订单类型,1:随叫随到;2:预约用车;3:接机;5:送机
                String result = null;

                if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState())|| serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())){
                     paramMap.put("serviceType","2000");
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceiveOrder", paramMap);
                }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
                    paramMap.put("serviceType","3000");
                    if(icaoCode!=null && icaoCode.contains("\\,")){
                        String[] split = icaoCode.split("\\,|\\，");
                        paramMap.put("depCode",split[0]);
                        paramMap.put("arrCode",split[1]);
                    }
                    paramMap.put("airlineNum",orderInfoOld.getFlightNumber());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = simpleDateFormat.format(orderInfoOld.getFlightPlanTakeOffTime());
                    paramMap.put("planDate",formatDate);
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceivePickUpOrder", paramMap);
                }else if(serviceType.equals((OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState()))){
                    paramMap.put("serviceType","4000");
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceiveSendToOrder", paramMap);
                }else{
                    break;
                }

                log.info("下单参数，{}",paramMap);
                log.info("下单结果，{}",result);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if(!"0".equals(jsonObject.getString("code"))){
                    throw new Exception("约车失败");
                }

                redisUtil.increment(CommonConstant.APPOINTMENT_NUMBER_PREFIX+orderId+"",1L);
                log.debug("订单【"+orderId+"】次数加一");
                Thread.sleep(60000*3);

                //调用查询订单状态的方法
                log.info("订单查询参数，{}",queryOrderStateMap);
                String resultQuery = OkHttpUtil.postForm(apiUrl + "/service/getOrderState", queryOrderStateMap);
                log.info("订单查询结果，{}",resultQuery);
                JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
                if(!"0".equals(jsonObjectQuery.getString("code"))){
                    throw new Exception("约车失败");
                }
                //判断状态,如果约到车修改状态为已派单
                JSONObject data = jsonObjectQuery.getJSONObject("data");
                if(data.getString("status").equals(OrderState.ALREADYSENDING.getState())){
                    orderInfo.setState(OrderState.ALREADYSENDING.getState());
                    String json = data.getString("driverInfoDTO");
                    DriverCloudDto driverCloudDto = JSONObject.parseObject(json, DriverCloudDto.class);
                    orderInfo.setDriverName(driverCloudDto.getDriverName());
                    orderInfo.setDriverMobile(driverCloudDto.getPhone());
                    orderInfo.setDriverGrade(driverCloudDto.getDriverRate());
                    orderInfo.setCarLicense(driverCloudDto.getLicensePlates());
                    orderInfo.setCarColor(driverCloudDto.getVehicleColor());
                    orderInfo.setCarModel(driverCloudDto.getModelName());
                    orderInfo.setDemandCarLevel(driverCloudDto.getGroupName());
                    orderInfo.setTripartiteOrderId(data.getString("orderNo"));
                    int j = orderInfoMapper.updateOrderInfo(orderInfo);
                    if (j != 1) {
                        throw new Exception("约车失败");
                    }
                    //发送短信
                    ismsBusiness.sendSmsCallTaxiNet(orderId);
                    break;
                }
            }
            log.debug("订单【"+orderId+"】约车成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.debug("订单【"+orderId+"】约车次数删除");
            redisUtil.delKey(CommonConstant.APPOINTMENT_NUMBER_PREFIX+orderId+"");
            log.debug("订单【"+orderId+"】约车次数删除成功");
        }
    }


	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean ownCarSendCar(Long orderId, Long driverId, Long carId, Long userId) {
		// 新增订单状态流转记录
		OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
		OrderInfo orderInfo = new OrderInfo();
		/*
		 * // 判读该单子是否是改派单 if
		 * (iOrderStateTraceInfoService.isReassignment(orderId)) { // 是改派单
		 * orderStateTraceInfo.setState(OrderStateTrace.PASSREASSIGNMENT.
		 * getState()); orderInfo.setState(OrderState.REASSIGNPASS.getState());
		 * } else { //申请单
		 * orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
		 * orderInfo.setState(OrderState.ALREADYSENDING.getState()); }
		 */
		orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
		orderInfo.setState(OrderState.ALREADYSENDING.getState());
		// 查询司机信息
		DriverInfo driverInfo = driverInfoService.selectDriverInfoById(driverId);
		orderInfo.setOrderId(orderId);
		orderInfo.setDriverId(driverId);
		if (null != driverInfo) {
			orderInfo.setDriverName(driverInfo.getDriverName());
			orderInfo.setDriverMobile(driverInfo.getMobile());
		}
		//查询车辆信息
		CarInfo carInfo = carInfoService.selectCarInfoById(carId);
		if(null !=carInfo){
			orderInfo.setCarLicense(carInfo.getCarLicense());
			orderInfo.setCarModel(carInfo.getCarType());
			orderInfo.setCarColor(carInfo.getCarColor());
		}
		orderInfo.setCarId(carId);
		orderInfo.setUpdateBy(String.valueOf(userId));
		orderInfo.setUpdateTime(new Date());
		// 更新订单信息
		int updateFlag = updateOrderInfo(orderInfo);

		orderStateTraceInfo.setCreateBy(String.valueOf(userId));
		orderStateTraceInfo.setOrderId(orderId);
		// 新增订单状态流转记录
		int insertFlag = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
		// 发送短信
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
                    ismsBusiness.sendSmsCallTaxiNet(orderId);
				} catch (Exception e) {
					log.error("订单号{}调度自有车,发送短信失败!",orderId);
					e.printStackTrace();
				}
			}
		}).start();
		return updateFlag > 0 && insertFlag > 0;
	}

	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Long officialOrder(OfficialOrderReVo officialOrderReVo,Long userId) throws Exception {
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(officialOrderReVo.getPowerId());
        if(journeyUserCarPower == null){
            throw new Exception("用车权限不存在");
        }
        List<OrderInfo> validOrderByPowerId = orderInfoMapper.getValidOrderByPowerId(officialOrderReVo.getPowerId());
        if(validOrderByPowerId!=null && validOrderByPowerId.size()>0){
            throw new Exception("此用车权限已存在有效订单");
        }
        Long journeyId = journeyUserCarPower.getJourneyId();
        Long applyId = journeyUserCarPower.getApplyId();
        //获取行程主表信息
        JourneyInfo journeyInfo = iJourneyInfoService.selectJourneyInfoById(journeyId);
        String serviceType = journeyInfo.getServiceType();
        //获取申请表信息
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        //插入订单初始信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNumber(OrderUtils.getOrderNum());
        orderInfo.setServiceType(serviceType);
        if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
            orderInfo.setFlightNumber(journeyInfo.getFlightNumber());
            orderInfo.setFlightPlanTakeOffTime(journeyInfo.getFlightPlanTakeOffTime());
        }
        orderInfo.setJourneyId(journeyId);
        orderInfo.setDriverId(null);
        orderInfo.setCarId(null);
        orderInfo.setUserId(journeyInfo.getUserId());
        //使用汽车的方式，自由和网约
        orderInfo.setUseCarMode(journeyInfo.getUseCarMode());
        orderInfo.setCreateBy(String.valueOf(userId));
        orderInfo.setCreateTime(new Date());
        String applyType = applyInfo.getApplyType();
        //如果是公务用车且走调度则状态直接为待派单,走网约车则变为约车中
        if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(applyType)) {
            if(officialOrderReVo.getIsDispatch() == 2){
                orderInfo.setState(OrderState.SENDINGCARS.getState());
            }else{
                orderInfo.setState(OrderState.WAITINGLIST.getState());
            }
        } else {
            throw new Exception("下单失败,只支持公务下单");
        }
        String type = journeyUserCarPower.getType();
        Long nodeId = journeyUserCarPower.getNodeId();
        Long powerId = journeyUserCarPower.getPowerId();
        orderInfo.setNodeId(nodeId);
        orderInfo.setPowerId(powerId);// TODO: 2020/3/3  权限表何时去创建？ 申请审批通过以后创建用车权限表记录，一个行程节点可能对应多个用车权限，比如往返超过固定时间
        orderInfoMapper.insertOrderInfo(orderInfo);
        //如果是公务用车，插入订单途经点信息表
        JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
        journeyNodeInfo.setJourneyId(journeyId);
        List<JourneyNodeInfo> journeyNodeInfoList = iJourneyNodeInfoService.selectJourneyNodeInfoList(journeyNodeInfo);
        if(journeyNodeInfoList !=null && journeyNodeInfoList.size()>0){
            List<OrderViaInfo> orderViaInfos = new ArrayList<>();
            for (int j=0;j<journeyNodeInfoList.size();j++) {
                JourneyNodeInfo journeyNodeInfoCh = journeyNodeInfoList.get(j);
                Integer number = journeyNodeInfoCh.getNumber();
                OrderViaInfo orderViaInfo = new OrderViaInfo();
                orderViaInfo.setOrderId(orderInfo.getOrderId());
                orderViaInfo.setItIsPassed(CommonConstant.NO_PASS);
                orderViaInfo.setArrivedTime(null);
                orderViaInfo.setDuration(null);
                orderViaInfo.setLeaveTime(null);
                orderViaInfo.setCreateBy(String.valueOf(userId));
                orderViaInfo.setCreateTime(DateUtils.getNowDate());
                orderViaInfo.setUpdateTime(null);
                orderViaInfo.setUpdateBy(null);
                if(number == 1){
                    orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongitude()));
                    orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                    orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanBeginLongAddress());
                    orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanBeginAddress());
                    orderViaInfo.setSortNumber(1);
                }else{
                    orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude()));
                    orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                    orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanEndLongAddress());
                    orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanEndAddress());
                    orderViaInfo.setSortNumber(journeyNodeInfoCh.getNumber());
                }
                orderViaInfos.add(orderViaInfo);

                //将公务计划时间地点信息插入订单地址表
                OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
                orderAddressInfo.setOrderId(orderInfo.getOrderId());
                orderAddressInfo.setJourneyId(journeyId);
                orderAddressInfo.setNodeId(nodeId);
                orderAddressInfo.setPowerId(powerId);
                orderAddressInfo.setUserId(journeyInfo.getUserId()+"");
                orderAddressInfo.setCreateBy(userId+"");
                if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
                    SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = si.format(journeyInfo.getFlightPlanTakeOffTime());
                    FlightInfoVo flightInfoVo = thirdService.loadDepartment(journeyInfo.getFlightNumber(), format);
                    if(flightInfoVo  != null){
                        String flightDepcode = flightInfoVo.getFlightDepcode();
                        String flightArrcode = flightInfoVo.getFlightArrcode();
                        orderAddressInfo.setIcaoCode(flightDepcode+","+flightArrcode);
                    }
                }
                //起点
                if(j == 1){
                    orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
                    orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanSetoutTime());
                    orderAddressInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongitude()));
                    orderAddressInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                    orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanBeginAddress());
                    orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanBeginLongAddress());
                    orderAddressInfo.setCityPostalCode(journeyNodeInfoCh.getPlanBeginCityCode());
                    iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                }
                //终点
                if(j==journeyNodeInfoList.size()-1){
                    orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
                    orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanArriveTime());
                    orderAddressInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude()));
                    orderAddressInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                    orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanEndAddress());
                    orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanEndLongAddress());
                    orderAddressInfo.setCityPostalCode(journeyNodeInfoCh.getPlanEndCityCode());
                    iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                }
            }
            if(orderViaInfos.size()>0){
                iOrderViaInfoService.insertOrderViaInfoBatch(orderViaInfos);
            }
        }
        //插入订单轨迹表
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId),null);
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId),null);
        //用车权限次数变化
        journeyUserCarCountOp(powerId,1);
        //走网约车约车接口
        if(officialOrderReVo.getIsDispatch() == 2){
            this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.SENDINGCARS.getState(), String.valueOf(userId),null);
            ((IOrderInfoService)AopContext.currentProxy()).platCallTaxi(orderInfo.getOrderId(),enterpriseId,licenseContent,apiUrl,String.valueOf(userId),officialOrderReVo.getCarLevel());
        }
        return orderInfo.getOrderId();
    }

    @Override
    public OrderDriverListInfo getNextTaskWithDriver(Long driverId) {
        return orderInfoMapper.getNextTaskWithDriver(driverId);
    }

    @Override
    public OrderDriverListInfo getNextTaskWithCar(Long carId) {
        return orderInfoMapper.getNextTaskWithCar(carId);
    }
    public MessageDto getCancelOrderMessage(Long userId, String states) {
        return orderInfoMapper.getCancelOrderMessage(userId,states);
    }

    @Override
    public List<OrderDriverListInfo> driverOrderUndoneList(Long userId, Integer pageNum, Integer pageSize, int day) throws Exception {
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(new DriverInfo(userId));
        if (CollectionUtils.isEmpty(driverInfos)){
            throw new Exception("当前登录人不是司机");
        }
        DriverInfo driverInfo = driverInfos.get(0);
        Long driverId = driverInfo.getDriverId();
        List<OrderDriverListInfo> lsit=orderInfoMapper.driverOrderUndoneList(driverId,day);
        return lsit;
    }

    @Override
    public int driverOrderCount(Long userId) throws Exception{
        List<DriverInfo> driverInfos = iDriverInfoService.selectDriverInfoList(new DriverInfo(userId));
        if (CollectionUtils.isEmpty(driverInfos)){
            throw new Exception("当前登录人不是司机");
        }
        String states=OrderState.ALREADYSENDING.getState()+","+OrderState.READYSERVICE.getState();
        return orderInfoMapper.getDriverOrderCount(driverInfos.get(0).getDriverId(),states);
    }

    //获取司机任务详情
    @Override
    public DriverOrderInfoVO driverOrderDetail(Long orderId) {
        DriverOrderInfoVO vo= orderInfoMapper.selectOrderDetail(orderId);
//        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(vo.getUserId());
        // TODO 获取车队电话和乘客电话有单独的接口(好像)
        vo.setCustomerServicePhone(serviceMobile);
//        List<PassengerInfoVO> list=new ArrayList();
//        list.add(new PassengerInfoVO(ecmpUser.getNickName(),ecmpUser.getPhonenumber(),"申请人"));
//        List<JourneyPassengerInfo> journeyPassengerInfos = passengerInfoMapper.selectJourneyPassengerInfoList(new JourneyPassengerInfo(vo.getJourneyId()));
//        if (!CollectionUtils.isEmpty(journeyPassengerInfos)){
//            for (JourneyPassengerInfo info:journeyPassengerInfos){
//                if ("00".equals(info.getItIsPeer())){
//                    list.add(new PassengerInfoVO(info.getName(),info.getMobile(),"乘车人"));
//                }else{
//                    list.add(new PassengerInfoVO(info.getName(),info.getMobile(),"同行人"));
//                }
//
//            }
//        }

        return vo;
    }

    @Override
    public OrderStateVO getOrderState(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(orderInfo.getPowerId());
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyUserCarPower.getApplyId());
        OrderStateVO orderState = orderInfoMapper.getOrderState(orderId,applyInfo.getApplyType());
        orderState.setApplyType(applyInfo.getApplyType());
        orderState.setCharterCarType(CharterTypeEnum.format(orderState.getCharterCarType()));
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos = iJourneyPlanPriceInfoService.selectJourneyPlanPriceInfoList(new JourneyPlanPriceInfo(orderId));
        if(!CollectionUtils.isEmpty(journeyPlanPriceInfos)){
            orderState.setPlanPrice(journeyPlanPriceInfos.get(0).getPrice().toPlainString());
        }
    return orderState;
    }

    @Override
    public List<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto) {
        PageHelper.startPage(orderListBackDto.getPageNum(),orderListBackDto.getPageSize());
        return orderInfoMapper.getOrderListBackDto(orderListBackDto);
    }

    @Override
    public OrderDetailBackDto getOrderListDetail(String orderNo) {
        return orderInfoMapper.getOrderListDetail(orderNo);
    }

    @Override
    public JSONObject getTaxiOrderState(Long orderId) throws  Exception{
        JSONObject data = getThirdPartyOrderState(orderId);
        String status = data.getString("status");
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        int newState = Integer.parseInt(status.substring(1));
        int i = Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1));
        if(!status.equals(orderInfo.getState())&&newState>i){
            OrderInfo newOrderInfo = new OrderInfo(orderId,status);
            Map<String,String> queryOrderStateMap = new HashMap<>();
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", MacTools.getMacList().get(0));
            queryOrderStateMap.put("driverPhone",orderInfo.getDriverMobile());
            String resultJson = OkHttpUtil.postJson(apiUrl + "/service/driverLocation", queryOrderStateMap);
            JSONObject resultObject = JSONObject.parseObject(resultJson);
            if (!"0".equals(resultObject.getString("code"))){
                throw new Exception("获取网约车司机位置异常");
            }
            Double driverLongitude=null;
            Double driverLatitude=null;
            if (!OrderState.ORDEROVERTIME.getState().equals(status)&&!OrderState.ORDERCANCEL.getState().equals(status)){//派车成功后所有状态
                //TODO 将网约车的车辆信息都存在车牌号字段中.格式(车牌号,车辆颜色,车辆名字,司机评分)
                if (OrderState.READYSERVICE.getState().equals(status)||OrderState.STOPSERVICE.getState().equals(status)){//乘客一上车
                    JSONObject data1 = resultObject.getJSONObject("data");
                    driverLongitude=Double.parseDouble(data1.getString("x"));
                    driverLatitude=Double.parseDouble(data1.getString("y"));
                    //TODO 杨军注释
                    if (OrderState.READYSERVICE.getState().equals(status)){

                        List<OrderAddressInfo> orderAddressInfos=orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId,OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT));
                        if(CollectionUtils.isEmpty(orderAddressInfos)){
                            OrderAddressInfo orderAddressInfo=new OrderAddressInfo();
                            BeanUtils.copyProperties(orderInfo,orderAddressInfo);
                            orderAddressInfo.setLatitude(Double.parseDouble(data1.getString("y")));
                            orderAddressInfo.setLongitude(Double.parseDouble(data1.getString("x")));
                            orderAddressInfo.setActionTime(new Date());
                            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
                            orderAddressInfoMapper.insertOrderAddressInfo(orderAddressInfo);
                        }else{
                            OrderAddressInfo orderAddressInfo = orderAddressInfos.get(0);
                            orderAddressInfo.setLatitude(Double.parseDouble(data1.getString("y")));
                            orderAddressInfo.setLongitude(Double.parseDouble(data1.getString("x")));
                            orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfo);
                        }
                    }else {
                        List<OrderAddressInfo> orderAddressInfos=orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId,OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE));
                        if(CollectionUtils.isEmpty(orderAddressInfos)){
                            OrderAddressInfo orderAddressInfo=new OrderAddressInfo();
                            BeanUtils.copyProperties(orderInfo,orderAddressInfo);
                            orderAddressInfo.setLatitude(Double.parseDouble(data1.getString("y")));
                            orderAddressInfo.setLongitude(Double.parseDouble(data1.getString("x")));
                            orderAddressInfo.setActionTime(new Date());
                            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
                            orderAddressInfoMapper.insertOrderAddressInfo(orderAddressInfo);
                        }else{
                            OrderAddressInfo orderAddressInfo = orderAddressInfos.get(0);
                            orderAddressInfo.setLatitude(Double.parseDouble(data1.getString("y")));
                            orderAddressInfo.setLongitude(Double.parseDouble(data1.getString("x")));
                            orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfo);
                        }
                    }

                }
            }
            orderInfoMapper.updateOrderInfo(newOrderInfo);
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(new OrderStateTraceInfo(orderId,status,driverLongitude,driverLatitude));
            if (OrderState.STOPSERVICE.getState().equals(status)||OrderState.ORDERCLOSE.getState().equals(status)||OrderState.DISSENT.getState().equals(status)) {//服务结束
                //TODO 调财务结算模块
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
                if (CollectionUtils.isEmpty(orderSettlingInfos)) {
                    JSONObject feeInfoBean = data.getJSONObject("feeInfoBean");
                    String amount = feeInfoBean.getString("customerPayPrice");
                    String distance = feeInfoBean.getString("mileage");//里程
                    String duration = feeInfoBean.getString("min");//时长
                    OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
                    orderSettlingInfo.setOrderId(orderId);
                    orderSettlingInfo.setTotalMileage(new BigDecimal(distance).stripTrailingZeros());
                    orderSettlingInfo.setTotalTime(Integer.parseInt(duration));
                    orderSettlingInfo.setAmount(new BigDecimal(amount).stripTrailingZeros());
                    orderSettlingInfo.setAmountDetail(feeInfoBean.toString());
                    orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
                }

            }
            return resultObject;
        }else{
            return null;
        }
    }

    private JSONObject getThirdPartyOrderState(Long orderId)throws Exception{
        Map<String,String> queryOrderStateMap = new HashMap<>();
        queryOrderStateMap.put("enterpriseId", enterpriseId);
        queryOrderStateMap.put("licenseContent", licenseContent);
        queryOrderStateMap.put("mac", MacTools.getMacList().get(0));
        queryOrderStateMap.put("enterpriseOrderId",orderId+"");
        queryOrderStateMap.put("status","S200");
        String resultQuery = OkHttpUtil.postJson(apiUrl + "/service/getOrderState", queryOrderStateMap);
        JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
        JSONObject data = jsonObjectQuery.getJSONObject("data");
        if(!"0".equals(jsonObjectQuery.getString("code"))){
            throw new Exception("获取网约车信息失败");
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Long applyUseCarWithTravel(ApplyUseWithTravelDto applyUseWithTravelDto, Long userId) throws Exception {
        JourneyUserCarPower journeyUserCarPower = iJourneyUserCarPowerService.selectJourneyUserCarPowerById(applyUseWithTravelDto.getTicketId());
        if(journeyUserCarPower == null){
            throw new Exception("用车权限不存在");
        }
        List<OrderInfo> validOrderByPowerId = orderInfoMapper.getValidOrderByPowerId(applyUseWithTravelDto.getTicketId());
        if(validOrderByPowerId!=null && validOrderByPowerId.size()>0){
            throw new Exception("此用车权限已存在有效订单");
        }
        JourneyInfo journeyInfo = iJourneyInfoService.selectJourneyInfoById(journeyUserCarPower.getJourneyId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNumber(OrderUtils.getOrderNum());
        orderInfo.setPowerId(journeyUserCarPower.getPowerId());
        orderInfo.setJourneyId(journeyUserCarPower.getJourneyId());
        orderInfo.setNodeId(journeyUserCarPower.getNodeId());
        orderInfo.setUserId(journeyInfo.getUserId());
        //直接约车，约车中。走调度，待派单
        if(applyUseWithTravelDto.getIsDispatch() == 2){
            orderInfo.setState(OrderState.SENDINGCARS.getState());
            orderInfo.setUseCarMode(CarConstant.USE_CAR_TYPE_TRAVEL);
        }else{
            orderInfo.setState(OrderState.WAITINGLIST.getState());
        }
        String type = applyUseWithTravelDto.getType();
        orderInfo.setDemandCarLevel(applyUseWithTravelDto.getGroupId());
        if(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getPrState().equals(type) || OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getPrState().equals(type)){
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState());
        }else if(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getPrState().equals(type)){
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState());
            orderInfo.setFlightNumber(applyUseWithTravelDto.getAirlineNum());
            SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date planDate = si.parse(applyUseWithTravelDto.getPlanDate());
            orderInfo.setFlightPlanTakeOffTime(planDate);
        }else if(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getPrState().equals(type)){
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState());
        }else{
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState());
        }
        orderInfo.setCreateTime(DateUtils.getNowDate());
        orderInfo.setCreateBy(userId+"");
        orderInfoMapper.insertOrderInfo(orderInfo);
        //订单轨迹
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId),null);
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId),null);
        //订单地址表
        String startPoint = applyUseWithTravelDto.getStartPoint();
        String endPoint = applyUseWithTravelDto.getEndPoint();
        String[] start = startPoint.split("\\,");
        String[] end = endPoint.split("\\,");

        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderInfo.getOrderId());
        orderAddressInfo.setJourneyId(orderInfo.getJourneyId());
        orderAddressInfo.setNodeId(orderInfo.getNodeId());
        orderAddressInfo.setPowerId(orderInfo.getPowerId());
        orderAddressInfo.setUserId(orderInfo.getUserId()+"");
        orderAddressInfo.setCityPostalCode(applyUseWithTravelDto.getCityId());
        if(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getPrState().equals(type)){
            String depCode = applyUseWithTravelDto.getDepCode();
            String arrCode = applyUseWithTravelDto.getArrCode();
            orderAddressInfo.setIcaoCode(arrCode+","+depCode);
        }
        orderAddressInfo.setCreateBy(userId+"");
        //起点
        orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(applyUseWithTravelDto.getBookingDate());
        orderAddressInfo.setActionTime(date);
        orderAddressInfo.setLongitude(Double.parseDouble(start[0]));
        orderAddressInfo.setLatitude(Double.parseDouble(start[1]));
        orderAddressInfo.setAddress(applyUseWithTravelDto.getStartAddr());
        orderAddressInfo.setAddressLong(applyUseWithTravelDto.getStarAddrLong());


        iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
        //终点
        orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
        orderAddressInfo.setActionTime(null);
        orderAddressInfo.setLongitude(Double.parseDouble(end[0]));
        orderAddressInfo.setLatitude(Double.parseDouble(end[1]));
        orderAddressInfo.setAddress(applyUseWithTravelDto.getEndAddr());
        orderAddressInfo.setAddressLong(applyUseWithTravelDto.getEndAddrLong());
        iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
        //用车权限次数变化
        journeyUserCarCountOp(applyUseWithTravelDto.getTicketId(),1);
        //走网约车约车接口
        if(applyUseWithTravelDto.getIsDispatch() == 2){
            this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.SENDINGCARS.getState(), String.valueOf(userId),null);
            ((IOrderInfoService)AopContext.currentProxy()).platCallTaxi(orderInfo.getOrderId(),enterpriseId,licenseContent,apiUrl,String.valueOf(userId),applyUseWithTravelDto.getGroupId());
        }else{
            ismsBusiness.sendMessagePriTravelOrderSucc(orderInfo.getOrderId(),userId);
        }
        return orderInfo.getOrderId();
    }

	@Override
	public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query) {
		List<ApplyDispatchVo> applyDispatchVoList = orderInfoMapper.queryApplyDispatchList(query);
		if(null !=applyDispatchVoList && applyDispatchVoList.size()>0){
			for (ApplyDispatchVo applyDispatchVo : applyDispatchVoList) {
				Long orderId = applyDispatchVo.getOrderId();
				Long journeyId = applyDispatchVo.getJourneyId();
                //查询乘车人
				applyDispatchVo.setUseCarUser(journeyPassengerInfoService.getPeerPeople(journeyId));
				//查询同行人数
				applyDispatchVo.setPeerUserNum(journeyPassengerInfoService.queryPeerCount(journeyId));
				//查询出发时间地点    目的地时间地点
				DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
				dispatchOrderInfo.setOrderId(orderId);
				buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
				applyDispatchVo.parseOrderStartAndEndSiteAndTime(dispatchOrderInfo);
				//转化状态
				applyDispatchVo.parseApplyDispatchStatus();
			}
		}
		return applyDispatchVoList;
	}

	@Override
	public Integer queryApplyDispatchListCount(ApplyDispatchQuery query) {
		return orderInfoMapper.queryApplyDispatchListCount(query);
	}

	@Override
	public List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query) {
		List<ApplyDispatchVo> reassignmentDispatchList = orderInfoMapper.queryReassignmentDispatchList(query);
		if(null !=reassignmentDispatchList && reassignmentDispatchList.size()>0){
			for (ApplyDispatchVo applyDispatchVo : reassignmentDispatchList) {
				Long orderId = applyDispatchVo.getOrderId();
				Long journeyId = applyDispatchVo.getJourneyId();
            //查询乘车人
				applyDispatchVo.setUseCarUser(journeyPassengerInfoService.getPeerPeople(journeyId));
				//查询同行人数
				applyDispatchVo.setPeerUserNum(journeyPassengerInfoService.queryPeerCount(journeyId));
				//查询出发时间地点    目的地时间地点
				DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
				dispatchOrderInfo.setOrderId(orderId);
				buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
				applyDispatchVo.parseOrderStartAndEndSiteAndTime(dispatchOrderInfo);
				//转化状态
				applyDispatchVo.parseReassignmentDispatchStatus();
			}
		}
		return reassignmentDispatchList;
	}

	@Override
	public Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query) {
		return orderInfoMapper.queryReassignmentDispatchListCount(query);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean rejectReassign(Long orderId, String rejectReason, Long optUserId) {
		// 生成订单状态流转记录
		OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
		orderStateTraceInfo.setCreateBy(String.valueOf(optUserId));
		orderStateTraceInfo.setState(ResignOrderTraceState.DISAGREE.getState());
		orderStateTraceInfo.setOrderId(orderId);
		orderStateTraceInfo.setContent(rejectReason);
		iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
		// 更改订单状态
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(orderId);
		orderInfo.setState(OrderState.REASSIGNREJECT.getState());
		orderInfo.setUpdateBy(String.valueOf(orderId));
		orderInfo.setUpdateTime(new Date());
		return updateOrderInfo(orderInfo)>0;
	}


    @Override
    public List<OrderHistoryTraceDto> getOrderHistoryTrace(Long orderId) throws Exception {
        List<OrderHistoryTraceDto> orderHistoryTraceDtos = new ArrayList<>();
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        String useCarMode = orderInfo.getUseCarMode();
        if(useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)){
            DriverHeartbeatInfo driverHeartbeatInfo = new DriverHeartbeatInfo();
            driverHeartbeatInfo.setOrderId(orderId);
            List<DriverHeartbeatInfo> driverHeartbeatInfos = iDriverHeartbeatInfoService.selectDriverHeartbeatInfoList(driverHeartbeatInfo);
            for (DriverHeartbeatInfo driverHeartbeatInfo1:
            driverHeartbeatInfos) {
                OrderHistoryTraceDto orderHistoryTraceDto = new OrderHistoryTraceDto();
                BeanUtils.copyProperties(driverHeartbeatInfo1,orderHistoryTraceDto);
                orderHistoryTraceDtos.add(orderHistoryTraceDto);
            }
            System.out.println(orderHistoryTraceDtos);
        }else if(useCarMode.equals(CarConstant.USR_CARD_MODE_NET)){
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("enterPriseOrderNo",orderId+"");
            paramsMap.put("enterpriseId",enterpriseId);
            paramsMap.put("licenseContent",licenseContent);
            String postForm = OkHttpUtil.postForm(apiUrl + "/service/orderTrack", paramsMap);
            JSONObject jsonObject = JSONObject.parseObject(postForm);
            if(jsonObject.getInteger("code")!= ApiResponse.SUCCESS_CODE){
                throw new Exception("获取轨迹失败");
            }
            String data = jsonObject.getString("data");
            List<OrderTraceDto> resultList = JSONObject.parseArray(data, OrderTraceDto.class);
            if(resultList.size()>0){
                for (OrderTraceDto orderTraceDto:
                resultList) {
                    OrderHistoryTraceDto orderHistoryTraceDto = new OrderHistoryTraceDto();
                    orderHistoryTraceDto.setOrderId(orderId+"");
                    orderHistoryTraceDto.setLongitude(orderTraceDto.getX());
                    orderHistoryTraceDto.setLatitude(orderTraceDto.getY());
                    Date date = new Date(Long.parseLong(orderTraceDto.getPt()));
                    orderHistoryTraceDto.setCreateTime(date);
                    orderHistoryTraceDtos.add(orderHistoryTraceDto);
                }
            }
        }else{
            throw new Exception("用车方式有误");
        }
        return orderHistoryTraceDtos;
    }

	

	private OrderCostDetailVO getOrderCost(Long orderId){
        OrderCostDetailVO result=new OrderCostDetailVO();
        List<OtherCostVO> list=new ArrayList<>();
        result.setCouponSettleAmout("22");
        result.setActualPayAmount("129");
        result.setCustomerPayPrice("107");
        result.setMileage("27公里");
        result.setMin("63分钟");
        list.add(new OtherCostVO("起步价","15","3公里","12分钟","含时长12分钟,含里程3公里"));
        list.add(new OtherCostVO("超时长费","15","平峰时长:3分钟,高峰时长:13分钟,累计超时时长:26分钟"));
        list.add(new OtherCostVO("超里程费","14","平峰里程:3公里,高峰里程:1公里,累计超时里程:4公里"));
        list.add(new OtherCostVO("夜间时长费","13","夜间服务费（里程）单价:1.3,夜间服务里程10公里"));
        list.add(new OtherCostVO("夜间时长费","12","夜间服务费（时长）单价:1.2,夜间服务时长10分钟"));
        list.add(new OtherCostVO("等待费","10","等待时长为15分钟"));
        list.add(new OtherCostVO("长途费","40","长途单价:4元/公里,长途里程10公里"));
        list.add(new OtherCostVO("其他费用","10","包含停车费、高速费、机场服务费"));
//        List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
//        OrderFeeDetailVO orderFeeDetailVO=null;
//        if (!CollectionUtils.isEmpty(orderSettlingInfos)){
//            //TODO 生产记得放开
//            OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
//            String amountDetail = orderSettlingInfo.getAmountDetail();
//            orderFeeDetailVO = GsonUtils.jsonToBean(amountDetail, new TypeToken<OrderFeeDetailVO>() {
//            }.getType());
//        }
//        if (orderFeeDetailVO==null){
//            return null;
//        }
//        BeanUtils.copyProperties(orderFeeDetailVO,result);
//        //基础套餐费：包含基础时长和基础里程，超出另外计费
//        String basePrice = orderFeeDetailVO.getBasePrice();
//        String includeMileage = orderFeeDetailVO.getIncludeMileage();
//        String includeMinute = orderFeeDetailVO.getIncludeMinute();
//        if(BigDecimal.ZERO!=new BigDecimal(basePrice)){
//            list.add(new OtherCostVO("起步价",basePrice,includeMileage,includeMinute,"含时长"+includeMinute+"分钟,含里程"+includeMileage+"公里"));
//        }
//        //超时长费：超出基础时长时计算，同时会区分高峰和平峰时段
//        String overTimeNumTotal = orderFeeDetailVO.getOverTimeNumTotal();//超时长费:高峰时长费+平超时长费
//        String hotDuration = orderFeeDetailVO.getHotDuration();//高峰时长
//        String hotDurationFees = orderFeeDetailVO.getHotDurationFees();//高峰时长费
//        String peakPriceTime = orderFeeDetailVO.getPeakPriceTime();//高峰单价(时长)
//        String overTimeNum = orderFeeDetailVO.getOverTimeNum();//平超时长
//        String overTimePrice = orderFeeDetailVO.getOverTimePrice();//平超时长费用
//        String allTime=new BigDecimal(overTimeNum).add(new BigDecimal(hotDuration)).stripTrailingZeros().toPlainString();
//        if(BigDecimal.ZERO!=new BigDecimal(overTimeNumTotal)){
//            list.add(new OtherCostVO("超时长费",overTimeNumTotal,"平峰时长:"+overTimeNum+"分钟,高峰时长:"+hotDuration+"分钟,累计超时时长:"+allTime+"分钟"));
//        }
//        //超里程费：超出基础里程时计算，同时会区分高峰和平峰时段
//        String overMilageNumTotal = orderFeeDetailVO.getOverMilageNumTotal();//超里程费用:高峰里程费+平超里程费
//        String overMilagePrice = orderFeeDetailVO.getOverMilagePrice();//平超里程费
//        String overMilageNum = orderFeeDetailVO.getOverMilageNum();//平超里程数
//        String hotMileageFees = orderFeeDetailVO.getHotMileageFees();//高峰里程费
//        String hotMileage = orderFeeDetailVO.getHotMileage();//高峰里程
//        String peakPrice = orderFeeDetailVO.getPeakPrice();//高峰里程单价
//        String allMileage=new BigDecimal(hotMileage).add(new BigDecimal(overMilageNum)).stripTrailingZeros().toPlainString();
//        if(BigDecimal.ZERO!=new BigDecimal(overMilageNumTotal)){
//            list.add(new OtherCostVO("超里程费",overTimeNumTotal,"平峰里程:"+overMilageNum+"公里,高峰里程:"+hotMileage+"公里,累计超时里程:"+allMileage+"公里"));
//        }
//        //夜间里程费：用车过程在设定为夜间服务时间的，加收夜间服务费。
//        String nightDistancePrice = orderFeeDetailVO.getNightDistancePrice();
//        String nightDistanceNum	 = orderFeeDetailVO.getNightDistanceNum();
//        String nightPrice = orderFeeDetailVO.getNightPrice();
//        if(BigDecimal.ZERO!=new BigDecimal(nightDistancePrice)){
//            list.add(new OtherCostVO("夜间时长费",nightDistancePrice,"夜间服务费（里程）单价:"+nightPrice+",夜间服务里程"+nightDistanceNum+"公里"));
//        }
//        //夜间时长费：用车过程在设定为夜间服务时间的，加收夜间服务费。
//        String nighitDurationFees = orderFeeDetailVO.getNighitDurationFees();
//        String nighitDuration = orderFeeDetailVO.getNighitDuration();
//        String nightPriceTime = orderFeeDetailVO.getNightPriceTime();
//        if(BigDecimal.ZERO!=new BigDecimal(nighitDurationFees)){
//            list.add(new OtherCostVO("夜间时长费",nighitDurationFees,"夜间服务费（时长）单价:"+nightPriceTime+",夜间服务时长"+nighitDuration+"分钟"));
//        }
//        //等待费
//        String waitingFee = orderFeeDetailVO.getWaitingFee();
//        String waitingMinutes = orderFeeDetailVO.getWaitingMinutes();
//        if(BigDecimal.ZERO!=new BigDecimal(waitingFee)){
//            list.add(new OtherCostVO("等待费",waitingFee,"等待时长为"+waitingMinutes+"分钟"));
//        }
//        //长途费：超过长途费起始里程时计算
//        String longDistancePrice = orderFeeDetailVO.getLongDistancePrice();
//        String longDistanceNum = orderFeeDetailVO.getLongDistanceNum();
//        String longPrice = orderFeeDetailVO.getLongPrice();
//        if(BigDecimal.ZERO!=new BigDecimal(longDistancePrice)){
//            list.add(new OtherCostVO("长途费",longDistancePrice,"长途单价:"+longPrice+",长途里程"+longDistanceNum+"公里"));
//        }
//        //价外税:如高速费和停车费
//        List<OtherFeeDetailVO> otherCost = orderFeeDetailVO.getOtherCost();
//        Double otherFee=0.0;
//        if (!CollectionUtils.isEmpty(otherCost)){
//            otherFee = otherCost.stream().map(OtherFeeDetailVO::getCostFee).collect(Collectors.reducing(Double::sum)).get();
//        }
//        if(BigDecimal.ZERO!=BigDecimal.valueOf(otherFee)){
//            list.add(new OtherCostVO("其他费用",String.valueOf(otherFee),"包含停车费、高速费、机场服务费"));
//        }
        result.setOtherCost(list);
        return result;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId,Long userId,String cancelReason) throws Exception {
        OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
        Double cancelFee = 0d;
        if (orderInfoOld == null) {
            throw new Exception("未查询到订单号【" + orderId+ "】对应的订单信息");
        }
        String useCarMode = orderInfoOld.getUseCarMode();
        String state = orderInfoOld.getState();
        //消息发送使用
        Long driverId = orderInfoOld.getDriverId();
        //状态为约到车未服务的状态，用车方式为网约车，调用三方取消订单接口
        if (OrderState.getContractedCar().contains(state) && useCarMode.equals(CarConstant.USR_CARD_MODE_NET)) {
            //TODO 调用网约车的取消订单接口
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("enterpriseOrderId", String.valueOf(orderId));
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put("reason", cancelReason);
            String result = OkHttpUtil.postJson(apiUrl + "/service/cancelOrder", paramMap);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!"0".equals(jsonObject.get("CODE"))) {
                throw new Exception("调用三方取消订单服务-》取消失败");
            }else{
                JSONObject data = jsonObject.getJSONObject("data");
                cancelFee = data.getDouble("cancelFee");
            }
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        //自有车，且状态变更成功
        if (suc == 1 && useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
            //TODO 调用消息通知接口，给司机发送乘客取消订单的消息
            ismsBusiness.sendMessageCancelOrder(orderId,userId);
        }
        //取消订单短信发送
        if(cancelFee == 0d){
            ismsBusiness.sendSmsCancelOrder(orderId);
        }else{
            ismsBusiness.sendSmsCancelOrderHaveFee(orderId,cancelFee);
        }

        //插入订单轨迹表
        this.insertOrderStateTrace(String.valueOf(orderId), OrderStateTrace.CANCEL.getState(), String.valueOf(userId),cancelReason);
        //用车权限次数做变化
        journeyUserCarCountOp(orderInfoOld.getPowerId(),2);
    }



    /**
     * 订单取消或者下单成功，用车权限的次数需要做变动
     * @param powerId
     * @param opType 1-下单成功 2-订单取消
     */
    private void journeyUserCarCountOp(Long powerId,Integer opType){
        iJourneyUserCarPowerService.updatePowerSurplus(powerId,opType);
    }

    /**
     * 改派订单
     * @param orderNo
     * @param rejectReason
     * @param status
     * @param userId
     * @throws Exception
     */
    @Transactional
    public void reassign( String orderNo,String rejectReason,String status,Long userId) throws Exception {
        if ("1".equals(status)) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setState(OrderState.WAITINGLIST.getState());
            orderInfo.setUpdateBy(String.valueOf(userId));
            orderInfo.setOrderId(Long.parseLong(orderNo));
            orderInfo.setUpdateTime(DateUtils.getNowDate());
            orderInfoMapper.updateOrderInfo(orderInfo);
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setCreateBy(String.valueOf(userId));
            orderStateTraceInfo.setState(ResignOrderTraceState.AGREE.getState());
            orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            //改派订单消息通知
            ismsBusiness.sendMessageReassignSucc(Long.parseLong(orderNo),userId);
        } else if ("2".equals(status)) {
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setCreateBy(String.valueOf(userId));
            orderStateTraceInfo.setState(ResignOrderTraceState.DISAGREE.getState());
            orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
            orderStateTraceInfo.setContent(rejectReason);
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        } else {
            throw new Exception("操作异常");
        }
    }


}
