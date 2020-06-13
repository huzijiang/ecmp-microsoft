package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.web.domain.server.Sys;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ContactorDto;
import com.hq.ecmp.mscore.dto.IsContinueReDto;
import com.hq.ecmp.mscore.dto.OrderViaInfoDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.util.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName DriverOrderServiceImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/10 14:36
 * @Version 1.0
 */
@Service
@Slf4j
public class DriverOrderServiceImpl implements IDriverOrderService {

    @Resource
    IOrderInfoService iOrderInfoService;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Resource
    OrderStateTraceInfoMapper orderStateTraceInfoMapper;

    @Resource
    IOrderWaitTraceInfoService iOrderWaitTraceInfoService;

    @Resource
    IOrderSettlingInfoService iOrderSettlingInfoService;

    @Resource
    IJourneyPassengerInfoService iJourneyPassengerInfoService;
    @Resource
    JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;

    @Autowired
    private TokenService tokenService;

    @Resource
    ICarInfoService iCarInfoService;

    @Resource
    IDriverInfoService iDriverInfoService;

    @Resource
    IOrderViaInfoService iOrderViaInfoService;

    @Resource
    ICarGroupInfoService iCarGroupInfoService;

    @Resource
    IOrderAddressInfoService iOrderAddressInfoService;

    @Resource
    ThirdService thirdService;
    @Resource
    IEcmpConfigService iEcmpConfigService;
    @Resource
    IsmsBusiness ismsBusiness;
    @Resource
    EcmpUserMapper ecmpUserMapper;
    @Resource
    JourneyInfoMapper journeyInfoMapper;
    @Resource
    IOrderSettlingInfoService iorderSettlingInfoService;


    @Resource
    private OrderServiceCostDetailRecordInfoMapper orderServiceCostDetailRecordInfoMapper;


    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;

    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;

    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleDriverOrderStatus(String type, String currentPoint, String orderNo, Long userId, String mileage, String travelTime) throws Exception {

        //ApiResponse<Map> apiResponse = new ApiResponse();
        Double longitude = null;
        Double latitude = null;
        if(currentPoint!=null && !currentPoint.equals("")){
            String[] point = currentPoint.split("\\,| \\，");
            longitude = Double.parseDouble(point[0]);
            latitude =  Double.parseDouble(point[1]);
        }
        long orderId = Long.parseLong(orderNo);

        //更新前订单信息
        OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
        //订单
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateBy(String.valueOf(userId));
        //订单轨迹
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setDriverLongitude(longitude);
        orderStateTraceInfo.setDriverLatitude(latitude);
        orderStateTraceInfo.setCreateBy(String.valueOf(userId));
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());

        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfoOld.getJourneyId());

        if(DriverBehavior.PICKUP_PASSENGER.getType().equals(type)){

            /** xmy1*/
            //1判断当前服务是不是暂停状态
            if(null != orderInfoOld && OrderState.SERVICE_SUSPEND.getState().equals(orderInfoOld.getState())){
                //2改变 服务状态
                orderInfo.setState(OrderState.REASSIGNMENT.getState());
                iOrderInfoService.updateOrderInfo(orderInfo);
                //3轨迹插入-- S639司机继续服务
                orderStateTraceInfo.setState(OrderStateTrace.DRIVER_CONTINUED_SERVICE.getState());
                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
                //4轨迹插入--"S500","前往出发地"
                orderStateTraceInfo.setTraceId(orderStateTraceInfo.getTraceId() +1);
                orderStateTraceInfo.setState(OrderStateTrace.ALREADY_SET_OUT.getState());
                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            /** xmy2*/

            }else {//第一次服务原逻辑
                //订单状态
                orderInfo.setState(OrderState.REASSIGNMENT.getState());
                iOrderInfoService.updateOrderInfo(orderInfo);
                //订单轨迹状态
                orderStateTraceInfo.setState(OrderStateTrace.ALREADY_SET_OUT.getState());
                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            }

            //司机--出发去接乘客--短信-给乘车人和申请人（行程通知）
            ismsBusiness.sendSmsServiceStart(orderId);
            log.info("司机--出发去接乘客--短信-给乘车人和申请人orderId：{}",orderId);

        }else if(DriverBehavior.ARRIVE.getType().equals(type)){
            //订单状态
            orderInfo.setState(OrderState.READYSERVICE.getState());
            iOrderInfoService.updateOrderInfo(orderInfo);
            //订单轨迹状态
            orderStateTraceInfo.setState(OrderStateTrace.PRESERVICE.getState());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            //司机到-达发上车地点-送短信
            ismsBusiness.sendSmsDriverArrivePrivate(orderId);
            log.info("司机到-达发上车地点-送短信orderId：{}",orderId);
        }else if((DriverBehavior.START_SERVICE.getType().equals(type))){
            log.info("司机开始服务订单号orderId:{}",orderId);

            //存储出发点行车经纬度
            OrderServiceCostDetailRecordInfo recordInfo = new OrderServiceCostDetailRecordInfo();
            recordInfo.setStartLongitude(BigDecimal.valueOf(null==longitude?00:longitude));//精度
            recordInfo.setStartLatitude(BigDecimal.valueOf(null==latitude?00:latitude));//维度
            recordInfo.setStartTime(new Date());
            recordInfo.setOrderId(orderId);
            recordInfo.setCreateTime(new Date());
            orderServiceCostDetailRecordInfoMapper.insert(recordInfo);


            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表
            String longAddr = "";
            String shortAddr ="";
            if(!"".equals(currentPoint)){
                Map<String, String> stringStringMap = thirdService.locationByLongitudeAndLatitude(String.valueOf(longitude), String.valueOf(latitude));
                longAddr = stringStringMap.get("longAddr");
                shortAddr = stringStringMap.get("shortAddr");
            }

            //订单地址表
            Long setOutOrderAddressId = null;
            OrderAddressInfo orderAddressInfoOld = new OrderAddressInfo();
            orderAddressInfoOld.setOrderId(orderId);
            orderAddressInfoOld.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfoOld);
            List<OrderStateTraceInfo> orderStateTraceInfos = orderStateTraceInfoMapper.selectOrderStateTraceInfoList(new OrderStateTraceInfo(orderId, OrderState.INSERVICE.getState()));
            if (CollectionUtils.isEmpty(orderStateTraceInfos)){
                /**第一次插服务中状态的记录 更新行程预计价格表的真实开始用车时间*/
                this.updateOrderPlanPrice(userId,orderInfoOld,1);
            }
            if(null != orderAddressInfos && orderAddressInfos.size() > 0){
                OrderAddressInfo orderAddressInfoCh = orderAddressInfos.get(0);
                setOutOrderAddressId = orderAddressInfoCh.getOrderAddressId();
                OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
                orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
                orderAddressInfo.setOrderId(orderId);
                orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
                orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
                orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
                orderAddressInfo.setDriverId(orderInfoOld.getDriverId());
                orderAddressInfo.setCarId(orderInfoOld.getCarId());
                orderAddressInfo.setUserId(orderInfoOld.getUserId()+"");
                orderAddressInfo.setCityPostalCode(null);
                orderAddressInfo.setActionTime(DateUtils.getNowDate());
                orderAddressInfo.setLongitude(longitude);
                orderAddressInfo.setLatitude(latitude);
                orderAddressInfo.setAddress(shortAddr);
                orderAddressInfo.setAddressLong(longAddr);
                orderAddressInfo.setCreateBy(userId+"");
                if(setOutOrderAddressId != null){
                    if (CollectionUtils.isEmpty(orderStateTraceInfos)){
                        /**第一次插服务中状态的记录 更新行程预计价格表的真实开始用车时间*/
                        orderAddressInfo.setOrderAddressId(setOutOrderAddressId);
                        iOrderAddressInfoService.updateOrderAddressInfo(orderAddressInfo);
                    }
                }else{
                    iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                }
                //订单状态
                orderInfo.setState(OrderState.INSERVICE.getState());
                iOrderInfoService.updateOrderInfo(orderInfo);
                //订单轨迹状态
                orderStateTraceInfo.setState(OrderStateTrace.SERVICE.getState());
                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
                //司机开始服务发送短信
                //ismsBusiness.sendSmsDriverBeginService(orderId);
                //司机开始服务发送消息给乘车人和申请人（行程通知）
                ismsBusiness.sendMessageServiceStart(orderId, userId);
            }

        }else if((DriverBehavior.SERVICE_COMPLETION.getType().equals(type))){//----服务完成4---
            log.info("司机服务完成订单号orderId:{}",orderId);
            OrderSettlingInfoVo vo = new OrderSettlingInfoVo();
            vo.setOrderId(orderId);//订单Id
            vo.setTotalMileage(new BigDecimal(mileage==null?"0":mileage));//订单总里程
            vo.setTotalTime(new BigDecimal(travelTime).intValue());//订单总时长
            iorderSettlingInfoService.addExpenseReport(vo, userId,orderInfoOld.getCompanyId());
            //TODO 此处需要根据经纬度去云端的接口获取长地址和短地址存入订单表
            String longAddr = "";
            String shortAddr ="";
            if(!"".equals(currentPoint)){
                Map<String, String> stringStringMap = thirdService.locationByLongitudeAndLatitude(String.valueOf(longitude), String.valueOf(latitude));
                longAddr = stringStringMap.get("longAddr");
                shortAddr = stringStringMap.get("shortAddr");
            }
            //订单地址表
            Long arriveOutOrderAddressId = null;
            OrderAddressInfo orderAddressInfoOld = new OrderAddressInfo();
            orderAddressInfoOld.setOrderId(orderId);
            orderAddressInfoOld.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfoOld);
            if(CollectionUtils.isNotEmpty(orderAddressInfos)){
                 orderAddressInfoOld = orderAddressInfos.get(0);
                arriveOutOrderAddressId = orderAddressInfoOld.getOrderAddressId();
            }
            int i = DateFormatUtils.compareDay(new Date(),orderAddressInfoOld.getActionTime());
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            orderAddressInfo.setOrderId(orderId);
            orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
            orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
            orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
            orderAddressInfo.setDriverId(orderInfoOld.getDriverId());
            orderAddressInfo.setCarId(orderInfoOld.getCarId());
            orderAddressInfo.setUserId(orderInfoOld.getUserId()+"");
            orderAddressInfo.setCityPostalCode(null);
            orderAddressInfo.setActionTime(DateUtils.getNowDate());
            orderAddressInfo.setLongitude(longitude);
            orderAddressInfo.setLatitude(latitude);
            orderAddressInfo.setAddress(shortAddr);
            orderAddressInfo.setAddressLong(longAddr);
            orderAddressInfo.setCreateBy(userId+"");
            if(arriveOutOrderAddressId != null){
                if (orderAddressInfoOld.getActionTime()!=null&&i<=0){
                    orderAddressInfo.setOrderAddressId(arriveOutOrderAddressId);
                    iOrderAddressInfoService.updateOrderAddressInfo(orderAddressInfo);
                    log.info("该最后结束日期订单更新预计价格表结束时间：{}",orderInfoOld);
                    this.updateOrderPlanPrice(userId,orderInfoOld,0);
                }
            }else{
                iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
            }
            int orderConfirmStatus = iEcmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey(),orderInfoOld.getUseCarMode(),orderInfoOld.getCompanyId());
            if (orderAddressInfoOld.getActionTime()!=null&&i<=0){//最后一天时间任一服务类型的订单都只能结束一次
                log.info("该最后结束日期订单以结束服务orderNo：{}",orderNo);
                /**xmy2*/
                this.updateEndOrderServerState(orderConfirmStatus,orderInfo,orderStateTraceInfo,"endServer");
            }else if(orderAddressInfoOld.getActionTime()!=null&&i==1){//小于预计结束时间--包车多日租可以继续服务
                /**xmy1*/
                if (ServiceTypeConstant.CHARTERED.equals(orderInfoOld.getServiceType())){//包车
                    log.info("该多日租包车订单继续服务orderNo：{}",orderNo);
                    this.updateEndOrderServerState(orderConfirmStatus,orderInfo,orderStateTraceInfo,"continueServer");
                }else{
                    log.info("该订单提前结束服务orderNo：{}",orderNo);
                    this.updateEndOrderServerState(orderConfirmStatus,orderInfo,orderStateTraceInfo,"endServer");
                }
            }
            /**xmy1*/
//            OrderInfoDate userAndActionTime = orderInfoMapper.getUserTimeAndActionTime(Long.parseLong(orderNo));
//            String useTime = userAndActionTime.getUseTime();//用车总天数
//            Date useCarTime = userAndActionTime.getUseCarTime();
//            String s = DateUtils.formatDate(useCarTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
//            Date startDate = DateUtils.parseDate(s);//用车开始时间
//            //用车天数3.5转成4天
//            double floor = Math.floor(Double.parseDouble(useTime));
//            int tmp = Integer.parseInt(new DecimalFormat("0").format(floor));
//            String dateNum = String.valueOf(tmp);
//            //Date startDate = DateUtils.parseDate(useCarTime);//用车开始时间
//            //结束用车时间
//            Date futureDate = DateUtils.getFutureDate(startDate, Integer.parseInt(dateNum));
//            String dateStr= DateUtils.parseDateToStr(DateUtils.YYYYMMDD, futureDate);
//            Date endDate = DateUtils.parseDate(dateStr);
//            String day = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());
//            //司机终止服务的时间
//            Date date = DateUtils.parseDate(day);
            //userTime <= 是单日租
            //用车时间 <=1 && 用车类型不是

            //!journeyInfo.getCharterCarType().equals(CharterTypeEnum.MORE_RENT_TYPE.getKey()) && null != journeyInfo
//            if( floor <= 1 || date.getTime() >= endDate.getTime()){ //行程结束时间是用车结束当天
//                log.info("该订单以结束服务orderNo：{}",orderNo);
//                /**xmy2*/  //非多日租走老方法
//                if(orderConfirmStatus == 1){
//                    orderInfo.setState(OrderState.STOPSERVICE.getState());
//                    orderStateTraceInfo.setState(OrderStateTrace.SERVICEOVER.getState());
//                }else{//确认行程不展示
//                    orderInfo.setState(OrderState.ORDERCLOSE.getState());
//                    orderStateTraceInfo.setState(OrderStateTrace.ORDERCLOSE.getState());
//                }
//                iOrderInfoService.updateOrderInfo(orderInfo);
//                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
//            }else {//false的话说明是多日服务 还没有到租车结束当天
//
//                log.info("该订继续服务orderNo：{}",orderNo);
//                /**xmy1*/
//                orderInfo.setState(OrderState.SERVICE_SUSPEND.getState());//订单状态
//                iOrderInfoService.updateOrderInfo(orderInfo);//更新订单状态
//                orderStateTraceInfo.setState(OrderStateTrace.DRIVER_SERVICE_SUSPEND.getState());//轨迹状态
//                //插入轨迹
//                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
//                orderStateTraceInfo.setTraceId(orderStateTraceInfo.getTraceId()+1);
//                orderStateTraceInfo.setState(OrderStateTrace.SERVICE_SUSPEND.getState());//轨迹状态
//                //插入轨迹
//                orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
//            }
            //存储行车结束经纬度
            OrderServiceCostDetailRecordInfo recordInfo = new OrderServiceCostDetailRecordInfo();

            long recorId = orderServiceCostDetailRecordInfoMapper.selsctRecordIdByOrderId(orderId);

            recordInfo.setEndLongitude(BigDecimal.valueOf(null==longitude?00:longitude));//精度
            recordInfo.setEndLatitude(BigDecimal.valueOf(null==latitude?00:latitude));//维度
            recordInfo.setOrderId(orderId);
            recordInfo.setRecordId(recorId);
            recordInfo.setEndTime(new Date());
            recordInfo.setUpdateTime(new Date());
            orderServiceCostDetailRecordInfoMapper.update(recordInfo);

            /**xmy2*/
            //添加里程数和总时长
            OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
            orderSettlingInfo.setOrderId(orderId);
            if(StringUtils.isNotBlank(mileage)){
                orderSettlingInfo.setTotalMileage(new BigDecimal(mileage).divide(new BigDecimal("1000"),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros());
            }else {
                orderSettlingInfo.setTotalMileage(new BigDecimal("0"));
            }
            if(StringUtils.isNotBlank(travelTime)){
                orderSettlingInfo.setTotalTime(new BigDecimal(travelTime).stripTrailingZeros());
            }else{
                orderSettlingInfo.setTotalTime(new BigDecimal("0"));
            }
            orderSettlingInfo.setCreateBy(String.valueOf(userId));


            iOrderSettlingInfoService.insertOrderSettlingInfo(orderSettlingInfo);

            //司机服务-结束发送短信
            //ismsBusiness.sendSmsDriverServiceComplete(orderId);
            ismsBusiness.sendSmsDriverServiceEnd(orderId);
            log.info("司机服务-结束发送短信已发送orderId：{}",orderId);

        }else{
            throw new Exception("操作类型有误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IsContinueReDto isContinue(String orderNo,String userId) {
        long orderId = Long.parseLong(orderNo);

        //判断是还车还是继续用车
        OrderInfo orderInfoOri = iOrderInfoService.selectOrderInfoById(orderId);
        //车辆
        Long carId = orderInfoOri.getCarId();
        //驾驶员
        Long driverId = orderInfoOri.getDriverId();
        //查询此驾驶员的订单
        OrderDriverListInfo nextTaskWithDriver = iOrderInfoService.getNextTaskWithDriver(driverId);
        OrderDriverListInfo nextTaskWithCar = iOrderInfoService.getNextTaskWithCar(carId);
        IsContinueReDto isContinueReDto = new IsContinueReDto();
        //还车
        if(nextTaskWithDriver == null || nextTaskWithDriver.getCarId()!=carId || nextTaskWithCar.getDriverId()!=driverId){
            isContinueReDto.setIsCallBack(1);
            if(nextTaskWithCar != null && nextTaskWithCar.getDriverId()!=driverId){
                isContinueReDto.setIsCarUse(1);
                isContinueReDto.setUseDateTime(nextTaskWithCar.getUseCarTime());
            }else {
                isContinueReDto.setIsCarUse(2);
            }
        }
        //继续用车
        if(nextTaskWithDriver!=null && nextTaskWithCar!=null && carId.equals(nextTaskWithDriver.getCarId())
        && driverId.equals(nextTaskWithCar.getDriverId())){
            JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(nextTaskWithDriver.getJourneyId());
            if (journeyInfo!=null){
                nextTaskWithDriver.setCharteredDays(journeyInfo.getUseTime());
                nextTaskWithDriver.setEndDate(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,journeyInfo.getEndDate()));
                nextTaskWithDriver.setStartDate(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_1,journeyInfo.getStartDate()));
            }
            isContinueReDto.setNextTaskDetailWithDriver(nextTaskWithDriver);
        }
        return isContinueReDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long waitingOrder(String orderNo, String isFinish, String currentPoint,String userId,String waitingId) throws Exception {
        Long orderId = Long.parseLong(orderNo);
        Double longitude = null;
        Double latitude = null;
        if(!"".equals(currentPoint) && currentPoint.contains("\\,|\\，")){
            String[] point = currentPoint.split("\\,| \\，");
            longitude = Double.parseDouble(point[0]);
            latitude = Double.parseDouble(point[1]);
        }
        if(CommonConstant.START.equals(isFinish)){
            OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(orderId);
            OrderWaitTraceInfo orderWaitTraceInfo = new OrderWaitTraceInfo();
            orderWaitTraceInfo.setOrderId(orderId);
            orderWaitTraceInfo.setJourneyId(orderInfo.getJourneyId());
            orderWaitTraceInfo.setDirverId(orderInfo.getDriverId());
            orderWaitTraceInfo.setCarLicense(orderInfo.getCarLicense());
            orderWaitTraceInfo.setStartTime(DateUtils.getNowDate());
            orderWaitTraceInfo.setLongitude(longitude);
            orderWaitTraceInfo.setLatitude(latitude);
            orderWaitTraceInfo.setCreateBy(userId);
            iOrderWaitTraceInfoService.insertOrderWaitTraceInfo(orderWaitTraceInfo);
            return orderWaitTraceInfo.getTraceId();
        }else if(CommonConstant.FINISH.equals(isFinish)){
            OrderWaitTraceInfo orderWaitTraceInfoQuery = new OrderWaitTraceInfo();
            orderWaitTraceInfoQuery.setOrderId(orderId);
            orderWaitTraceInfoQuery.setTraceId(Long.parseLong(waitingId));
            iOrderWaitTraceInfoService.updateOrderWaitTraceInfo(orderWaitTraceInfoQuery);
            OrderWaitTraceInfo orderWaitTraceInfo = iOrderWaitTraceInfoService.selectOrderWaitTraceInfoById(Long.parseLong(waitingId));
            Date startTime = orderWaitTraceInfo.getStartTime();
            Long duration = (DateUtils.getNowDate().getTime()-startTime.getTime())/1000;
            OrderWaitTraceInfo orderWaitTraceInfoParam = new OrderWaitTraceInfo();
            orderWaitTraceInfoParam.setEndTime(DateUtils.getNowDate());
            orderWaitTraceInfoParam.setDuration(duration);
            orderWaitTraceInfoParam.setUpdateBy(userId);
            orderWaitTraceInfoParam.setTraceId(Long.parseLong(waitingId));
            iOrderWaitTraceInfoService.updateOrderWaitTraceInfo(orderWaitTraceInfoParam);
            return null;
        }else{
            throw new Exception("操作异常");
        }
    }

    @Override
    public List<JourneyPassengerInfo> getInfoWithPassenger(String orderId) {
        OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(Long.parseLong(orderId));
        Long journeyId = orderInfo.getJourneyId();
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyId(journeyId);
        journeyPassengerInfo.setItIsPeer("01");
        List<JourneyPassengerInfo> journeyPassengerInfos = iJourneyPassengerInfoService.selectJourneyPassengerInfoList(journeyPassengerInfo);
        return journeyPassengerInfos;
    }

    @Override
    public List<ContactorDto> getInfoWithCarGroup(String orderId) {
        List<ContactorDto> result = new ArrayList();
        OrderInfo orderInfo = iOrderInfoService.selectOrderInfoById(Long.parseLong(orderId));
        Long carId = orderInfo.getCarId();
        CarInfo carInfo = iCarInfoService.selectCarInfoById(carId);
        if(null == carInfo){
            log.info("接口：contactCarGroup 获取车信息失败carId：{}",carId);
            return null;
        }
        Long carGroupId = carInfo.getCarGroupId();
        CarGroupInfo carGroupInfo = iCarGroupInfoService.selectCarGroupInfoById(carGroupId);
        String telephone = carGroupInfo.getTelephone();
        //車隊座機
        ContactorDto contactorDtoCarGr = new ContactorDto();
        contactorDtoCarGr.setRoleName(CommonConstant.CARGROUP_PHONE_ROLE);
        contactorDtoCarGr.setPhone(telephone);
        contactorDtoCarGr.setName(CommonConstant.CARGROUP_PHONE_ROLE);
        result.add(contactorDtoCarGr);
        //调度员
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
        List<OrderStateTraceInfo> orderStateTraceInfos = orderStateTraceInfoMapper.selectOrderStateTraceInfoList(orderStateTraceInfo);
        if (CollectionUtils.isNotEmpty(orderStateTraceInfos)){
            OrderStateTraceInfo orderStateTraceInfoSc = orderStateTraceInfos.get(0);
            String createBy = orderStateTraceInfoSc.getCreateBy();
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.parseLong(createBy));
            if (null == ecmpUser) {
                log.info("方法名：getInfoWithCarGroup 参数createBy：{}", createBy);
                return result;
            }
            String mobile = ecmpUser.getPhonenumber();
            String driverName = ecmpUser.getNickName();
            ContactorDto contactorDto = new ContactorDto();
            contactorDto.setRoleName(CommonConstant.DISPATCHER_ROLE);
            contactorDto.setPhone(mobile);
            contactorDto.setName(driverName);
            result.add(contactorDto);
        }
        return result;
    }

    @Override
    public List<OrderViaInfoDto> getOrderViaInfos(String orderId) {
        OrderViaInfo orderViaInfo = new OrderViaInfo();
        orderViaInfo.setOrderId(Long.parseLong(orderId));
        List<OrderViaInfoDto> orderViaInfoDtos = new ArrayList<>();
        List<OrderViaInfo> orderViaInfos = iOrderViaInfoService.selectOrderViaInfoList(orderViaInfo);
        for (int i = 0; i < orderViaInfos.size(); i++) {
            OrderViaInfo orderViaInfoCh = orderViaInfos.get(i);
            OrderViaInfoDto orderViaInfoDto = new OrderViaInfoDto();
            orderViaInfoDto.setViaId(orderViaInfoCh.getViaId());
            orderViaInfoDto.setFullAddress(orderViaInfoCh.getFullAddress());
            orderViaInfoDto.setLatitude(orderViaInfoCh.getLatitude());
            orderViaInfoDto.setLongitude(orderViaInfoCh.getLongitude());
            orderViaInfoDto.setShortAddress(orderViaInfoCh.getShortAddress());
            orderViaInfoDto.setSortNumber(orderViaInfoCh.getSortNumber());
            orderViaInfoDto.setOrderId(Long.parseLong(orderId));
            orderViaInfoDtos.add(orderViaInfoDto);
        }
        return orderViaInfoDtos;
    }

    private void updateOrderPlanPrice(Long userId,OrderInfo orderInfoOld,int flag){
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos = journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(new JourneyPlanPriceInfo(orderInfoOld.getJourneyId(),orderInfoOld.getOrderId()));
        if (CollectionUtils.isNotEmpty(journeyPlanPriceInfos)){
            JourneyPlanPriceInfo journeyPlanPriceInfo = journeyPlanPriceInfos.get(0);
            if (flag==0){
                journeyPlanPriceInfo.setPlannedArrivalTime(DateUtils.getNowDate());
            }else{
                journeyPlanPriceInfo.setPlannedDepartureTime(DateUtils.getNowDate());
            }
            journeyPlanPriceInfo.setUpdateBy(String.valueOf(userId));
            journeyPlanPriceInfo.setUpdateTime(DateUtils.getNowDate());
            journeyPlanPriceInfoMapper.updateJourneyPlanPriceInfo(journeyPlanPriceInfo);
        }
    }

    private void updateEndOrderServerState(int orderConfirmStatus,OrderInfo orderInfo,OrderStateTraceInfo orderStateTraceInfo,String flag){

        if ("continueServer".equals(flag)){
            orderInfo.setState(OrderState.SERVICE_SUSPEND.getState());//订单状态
            iOrderInfoService.updateOrderInfo(orderInfo);//更新订单状态
            orderStateTraceInfo.setState(OrderStateTrace.DRIVER_SERVICE_SUSPEND.getState());//轨迹状态
            //插入轨迹
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            orderStateTraceInfo.setTraceId(orderStateTraceInfo.getTraceId()+1);
            orderStateTraceInfo.setState(OrderStateTrace.SERVICE_SUSPEND.getState());//轨迹状态
            //插入轨迹
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        }else if ("endServer".equals(flag)){
            if(orderConfirmStatus == 1){
                orderInfo.setState(OrderState.STOPSERVICE.getState());
                orderStateTraceInfo.setState(OrderStateTrace.SERVICEOVER.getState());
            }else{//确认行程不展示
                orderInfo.setState(OrderState.ORDERCLOSE.getState());
                orderStateTraceInfo.setState(OrderStateTrace.ORDERCLOSE.getState());
            }
            iOrderInfoService.updateOrderInfo(orderInfo);
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        }
    }
}
