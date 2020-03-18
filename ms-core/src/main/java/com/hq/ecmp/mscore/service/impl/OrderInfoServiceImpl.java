package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyUseWithTravelDto;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.text.ParseException;
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
    @Resource
    private IOrderViaInfoService iOrderViaInfoService;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private ICarGroupDispatcherInfoService carGroupDispatcherInfoService;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private JourneyPassengerInfoMapper passengerInfoMapper;
    @Autowired
    private IJourneyPassengerInfoService journeyPassengerInfoService;
    @Resource
    private IOrderAddressInfoService iOrderAddressInfoService;
    @Resource
    private IJourneyPlanPriceInfoService iJourneyPlanPriceInfoService;


    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;

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
            String states=OrderState.ALREADYSENDING.getState()+","+OrderState.READYSERVICE.getState();
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
	public DispatchOrderInfo getWaitDispatchOrderDetailInfo(Long orderId) {
		DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.getWaitDispatchOrderDetailInfo(orderId);
		//查询订单对应的上车地点时间,下车地点时间
		buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
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
		//查询订单对应的上车地点时间,下车地点时间
		buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
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
    public OrderVO orderBeServiceDetail(Long orderId) throws Exception{
        OrderVO vo=new OrderVO();
        OrderInfo orderInfo = this.selectOrderInfoById(orderId);
        if (orderInfo==null){
            throw new Exception("该订单不存在");
        }
        JourneyNodeInfo nodeInfo = iJourneyNodeInfoService.selectJourneyNodeInfoById(orderInfo.getNodeId());
        //TODO 杨军注释
        BeanUtils.copyProperties(orderInfo,vo);
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
        UserVO str= iOrderStateTraceInfoService.getOrderDispatcher(states,orderId);
        vo.setCarGroupPhone(str.getUserPhone());
        vo.setCarGroupName(str.getUserName());
        vo.setCustomerServicePhone(serviceMobile);
        vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        String useCarTime=null;
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId, OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT));
        if (!CollectionUtils.isEmpty(orderAddressInfos)){
            useCarTime=DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,journeyInfo.getUseCarTime());
        }else{
            useCarTime=DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,orderAddressInfos.get(0).getActionTime());
        }
        vo.setUseCarTime(useCarTime);
        List<UserEmergencyContactInfo> contactInfos = userEmergencyContactInfoMapper.queryAll(new UserEmergencyContactInfo(journeyInfo.getUserId()));
        String isAddContact=CollectionUtils.isEmpty(contactInfos)?"否":"是";
        vo.setIsAddContact(isAddContact);
        if(CarModeEnum.ORDER_MODE_HAVE.getKey().equals(orderInfo.getState())){//自有车
            //查询车辆信息
            CarInfo carInfo = carInfoService.selectCarInfoById(orderInfo.getCarId());
            if (carInfo!=null){
                BeanUtils.copyProperties(carInfo,vo);
            }
            vo.setPowerType(CarPowerEnum.format(carInfo.getPowerType()));
            DriverInfo driverInfo = driverInfoService.selectDriverInfoById(orderInfo.getDriverId());
            vo.setDriverScore(driverInfo.getStar()+"");
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState())||OrderState.DISSENT.getState().equals(orderInfo.getState())){
                //服务结束后获取里程用车时长
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo());
                if (!CollectionUtils.isEmpty(orderSettlingInfos)){
                    vo.setDistance(orderSettlingInfos.get(0).getTotalMileage().stripTrailingZeros().toPlainString()+"公里");
                    vo.setDuration(DateFormatUtils.formatMinute(orderSettlingInfos.get(0).getTotalTime()));
                }
            }
        }else{
            if (StringUtils.isNotEmpty(orderInfo.getCarLicense())){
                String[] split = orderInfo.getCarLicense().split(",");
                vo.setCarLicense(split[0]);//车牌号
                vo.setCarColor(split[1]);
                vo.setCarType(split[2]);
                vo.setDriverScore(split[3]);
            }
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState())||OrderState.DISSENT.getState().equals(orderInfo.getState())){
//                JSONObject data = getThirdPartyOrderState(orderId);
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
                if (!CollectionUtils.isEmpty(orderSettlingInfos)){
                    OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
                    JSONObject jsonObject = JSONObject.parseObject(orderSettlingInfo.getAmountDetail());
                    String disMoney = jsonObject.getString("disMoney");//原价
                    String distance = jsonObject.getString("distance");//里程
                    String distanceFee = jsonObject.getString("distanceFee");//里程费
                    String duration = jsonObject.getString("duration");//时长(分钟)
                    String durationFee = jsonObject.getString("durationFee");//时长费
                    String overDistancePrice = jsonObject.getString("overDistancePrice");//每公里单据
                    vo.setAmount(orderSettlingInfo.getAmount().stripTrailingZeros().toPlainString());
                    vo.setDisMoney(disMoney);
                    vo.setDistance(distance+"公里");
                    vo.setDistanceFee(distanceFee);
                    vo.setDuration(DateFormatUtils.formatMinute(StringUtils.isNotEmpty(duration)?Integer.parseInt(duration):0));
                    vo.setDurationFee(durationFee);
                    vo.setOverDistancePrice(overDistancePrice);
                }
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
    public void platCallTaxi(Long  orderId, String enterpriseId, String licenseContent, String apiUrl,String userId) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        try {
            //MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            //调用网约车约车接口参数
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put("enterpriseOrderId",orderId+"");
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
            paramMap.put("groupIds",orderInfoOld.getDemandCarLevel());
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
                    paramMap.put("bookingDate",orderAddressInfo1.getActionTime().getTime()+"");
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
            Map<String,String> queryOrderStateMap = new HashMap<>();
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
                     paramMap.put("serviceType","2");
                     OkHttpUtil.postJson(apiUrl + "/service/applyPlatReceiveOrder", paramMap);
                }else if(serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP)){
                    paramMap.put("serviceType","3");
                    if(icaoCode!=null && icaoCode.contains("\\,")){
                        String[] split = icaoCode.split("\\,|\\，");
                        paramMap.put("depCode",split[0]);
                        paramMap.put("arrCode",split[1]);
                    }
                    paramMap.put("airlineNum",orderInfoOld.getFlightNumber());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = simpleDateFormat.format(orderInfoOld.getFlightPlanTakeOffTime());
                    paramMap.put(" planDate",formatDate);
                    OkHttpUtil.postJson(apiUrl + "/service/applyPlatReceivePickUpOrder", paramMap);
                }else if(serviceType.equals((OrderServiceType.ORDER_SERVICE_TYPE_SEND))){
                    paramMap.put("serviceType","5");
                    OkHttpUtil.postJson(apiUrl + "/service/applyPlatReceiveSendToOrder", paramMap);
                }else{
                    break;
                }


                JSONObject jsonObject = JSONObject.parseObject(result);
                if(!"0".equals(jsonObject.getString("code"))){
                    throw new Exception("约车失败");
                }

                redisUtil.increment(CommonConstant.APPOINTMENT_NUMBER_PREFIX+orderId+"",1L);
                log.debug("订单【"+orderId+"】次数加一");
                Thread.sleep(60000*2);


                //调用查询订单状态的方法
                String resultQuery = OkHttpUtil.postJson(apiUrl + "/service/getOrderState", queryOrderStateMap);
                JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
                if(!"0".equals(jsonObjectQuery.getString("code"))){
                    throw new Exception("约车失败");
                }
                //判断状态,如果约到车修改状态为已派单
                JSONObject data = jsonObjectQuery.getJSONObject("data");
                if(data.getString("status").equals(OrderState.ALREADYSENDING.getState())){
                    orderInfo.setState(OrderState.ALREADYSENDING.getState());
                    int j = orderInfoMapper.updateOrderInfo(orderInfo);
                    if (j != 1) {
                        throw new Exception("约车失败");
                    }
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
		// 判读该单子是否是改派单
		if (iOrderStateTraceInfoService.isReassignment(orderId)) {
			// 是改派单
			orderStateTraceInfo.setState(OrderStateTrace.PASSREASSIGNMENT.getState());
			orderInfo.setState(OrderState.REASSIGNPASS.getState());
		} else {
			//申请单
			orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
			orderInfo.setState(OrderState.ALREADYSENDING.getState());
		}
		// 查询司机信息
		DriverInfo driverInfo = driverInfoService.selectDriverInfoById(driverId);
		orderInfo.setOrderId(orderId);
		orderInfo.setDriverId(driverId);
		if (null != driverInfo) {
			orderInfo.setDriverName(driverInfo.getDriverName());
			orderInfo.setDriverMobile(driverInfo.getMobile());
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
		return updateFlag > 0 && insertFlag > 0;
	}

    public void initOrder(Long applyId,Long jouneyId,Long userId) throws Exception {
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
        if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(applyType)) {
            orderInfo.setState(OrderState.WAITINGLIST.getState());
        } else {
            orderInfo.setState(OrderState.INITIALIZING.getState());
        }
        //有多少用车权限创建多少订单（跳过差旅的市内用车）
        if(journeyUserCarPowers.size()<=0){
            log.error("行程id【"+jouneyId+"】,申请单id【"+applyId+"】,请配置用车权限");
            throw new Exception("请配置用车权限");
        }
        for (int i = 0; i < journeyUserCarPowers.size(); i++) {
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
            //如果是公务用车，插入订单途经点信息表
            if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(applyType)){
                JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
                journeyNodeInfo.setJourneyId(jouneyId);
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
                            orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongAddress()));
                            orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                            orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanBeginLongAddress());
                            orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanBeginAddress());
                            orderViaInfo.setSortNumber(1);
                        }else{
                            orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongAddress()));
                            orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                            orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanEndLongAddress());
                            orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanEndAddress());
                            orderViaInfo.setSortNumber(journeyNodeInfoCh.getNumber());
                        }
                        orderViaInfos.add(orderViaInfo);

                        //将公务计划时间地点信息插入订单地址表
                        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
                        orderAddressInfo.setOrderId(orderInfo.getOrderId());
                        orderAddressInfo.setJourneyId(jouneyId);
                        orderAddressInfo.setNodeId(nodeId);
                        orderAddressInfo.setPowerId(powerId);
                        orderAddressInfo.setUserId(journeyInfo.getUserId()+"");
                        orderAddressInfo.setCreateBy(userId+"");
                        //起点
                        if(j == journeyNodeInfoList.size()-1){
                            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
                            orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanSetoutTime());
                            orderAddressInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongitude()));
                            orderAddressInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                            orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanBeginAddress());
                            orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanBeginLongAddress());
                            iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                        }
                        //终点
                        if(j==1){
                            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
                            orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanArriveTime());
                            orderAddressInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude()));
                            orderAddressInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                            orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanEndAddress());
                            orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanEndLongAddress());
                            iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                        }
                    }
                    if(orderViaInfos.size()>0){
                        iOrderViaInfoService.insertOrderViaInfoBatch(orderViaInfos);
                    }
                }
            }

            //插入订单轨迹表
            this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId),null);
            if ("A001".equals(applyType)) {
                this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId),null);
            }
        }
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
        RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(orderInfo.getPowerId());
        OrderStateVO orderState = orderInfoMapper.getOrderState(orderId,regimeInfo.getRegimenType());
        orderState.setApplyType(regimeInfo.getRegimenType());
        orderState.setCharterCarType(CharterTypeEnum.format(orderState.getCharterCarType()));
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
            String name = data.getJSONObject("driverInfoDTO").getString("name");
            String phone = data.getJSONObject("driverInfoDTO").getString("phone");
            String licensePlates = data.getJSONObject("driverInfoDTO").getString("licensePlates");//车牌
            String star =  data.getJSONObject("driverInfoDTO").getString("driverRate");
            String carName = data.getJSONObject("driverInfoDTO").getString("modelName");
            String carColor = data.getJSONObject("driverInfoDTO").getString("vehicleColor");
            OrderInfo newOrderInfo = new OrderInfo(orderId,status);
            Map<String,String> queryOrderStateMap = new HashMap<>();
            queryOrderStateMap.put("enterpriseId", enterpriseId);
            queryOrderStateMap.put("licenseContent", licenseContent);
            queryOrderStateMap.put("mac", MacTools.getMacList().get(0));
            queryOrderStateMap.put("driverPhone",phone);
            String resultJson = OkHttpUtil.postJson(apiUrl + "/service/driverLocation", queryOrderStateMap);
            JSONObject resultObject = JSONObject.parseObject(resultJson);
            if (!"0".equals(resultObject.getString("code"))){
                throw new Exception("获取网约车司机位置异常");
            }
            if (!OrderState.ORDEROVERTIME.getState().equals(status)&&!OrderState.ORDERCANCEL.getState().equals(status)){//派车成功后所有状态
                newOrderInfo.setDriverMobile(phone);
                newOrderInfo.setDriverName(name);
                //TODO 将网约车的车辆信息都存在车牌号字段中.格式(车牌号,车辆颜色,车辆名字,司机评分)
                String carStr=licensePlates+","+carColor+","+carName+","+star;
                newOrderInfo.setCarLicense(carStr);
                if (OrderState.READYSERVICE.getState().equals(status)||OrderState.STOPSERVICE.getState().equals(status)){//乘客一上车
                    JSONObject data1 = resultObject.getJSONObject("data");
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
            iOrderStateTraceInfoService.insertOrderStateTraceInfo(new OrderStateTraceInfo(orderId,status,null,null));
            if (OrderState.STOPSERVICE.getState().equals(status)||OrderState.ORDERCLOSE.getState().equals(status)||OrderState.DISSENT.getState().equals(status)) {//服务结束
                //TODO 调财务结算模块
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
                if (CollectionUtils.isEmpty(orderSettlingInfos)) {
                    JSONObject feeInfoBean = data.getJSONObject("feeInfoBean");
                    String amount = feeInfoBean.getString("amount");
                    String distance = feeInfoBean.getString("distance");//里程
                    String duration = feeInfoBean.getString("duration");//时长
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
    public void applyUseCarWithTravel(ApplyUseWithTravelDto applyUseWithTravelDto, Long userId) throws ParseException {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(applyUseWithTravelDto.getOrderId());
        //手动下单，订单状态变为待派单
        orderInfo.setState(OrderState.WAITINGLIST.getState());
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
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfoMapper.updateOrderInfo(orderInfo);
        //订单轨迹
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId),null);
        //订单地址表
        OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(applyUseWithTravelDto.getOrderId());
        String startPoint = applyUseWithTravelDto.getStartPoint();
        String endPoint = applyUseWithTravelDto.getEndPoint();
        String[] start = startPoint.split("\\,");
        String[] end = endPoint.split("\\,");

        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderInfoOld.getOrderId());
        orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
        orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
        orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
        orderAddressInfo.setUserId(orderInfoOld.getUserId()+"");
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
}
