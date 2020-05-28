package com.hq.ecmp.mscore.service.dispatchstrategy;

import com.hq.common.utils.DateUtils;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.constant.SmsTemplateConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/26 10:51
 * @Version 1.0
 */
@Service
public abstract class TopDispatchService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;
    @Resource
    private  OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Resource
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private ISmsTemplateInfoService iSmsTemplateInfoService;
    @Resource
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Resource
    private DriverInfoMapper driverInfoMapper;

    /**
     * 业务执行方法
     * @param dispatchSendCarDto
     */
    public void disBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        judgeIsFinish(dispatchSendCarDto);
        ((TopDispatchService)AopContext.currentProxy()).dispatchCommonBusiness(dispatchSendCarDto);
        ((TopDispatchService)AopContext.currentProxy()).sendSms(dispatchSendCarDto);
    }


    /**
     * 调度公共逻辑
     * @param dispatchSendCarDto
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void dispatchCommonBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(dispatchSendCarDto.getOrderId());
        orderInfo.setCarId(dispatchSendCarDto.getCarId());
        orderInfo.setDriverId(dispatchSendCarDto.getDriverId());
        orderInfo.setUpdateBy(String.valueOf(dispatchSendCarDto.getUserId()));
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        if(dispatchSendCarDto.getIsFinishDispatch() == 1){
            orderInfo.setState(OrderState.ALREADYSENDING.getState());
        }
        orderInfoMapper.updateOrderInfo(orderInfo);

        if(dispatchSendCarDto.getIsFinishDispatch() == 1) {
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setOrderId(dispatchSendCarDto.getOrderId());
            orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
            orderStateTraceInfo.setCreateBy(String.valueOf(dispatchSendCarDto.getUserId()));
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        }

        OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
        orderDispatcheDetailInfo.setOrderId(dispatchSendCarDto.getOrderId());
        orderDispatcheDetailInfo.setItIsUseInnerCarGroup(dispatchSendCarDto.getUseCarGroupType());
        orderDispatcheDetailInfo.setCarGroupUserMode(dispatchSendCarDto.getCarGroupUseMode());
        orderDispatcheDetailInfo.setItIsSelfDriver(dispatchSendCarDto.getSelfDrive());
        orderDispatcheDetailInfo.setNextCarGroupId(dispatchSendCarDto.getOutCarGroupId());
        orderDispatcheDetailInfo.setCharterCarType(dispatchSendCarDto.getCharterType());
        orderDispatcheDetailInfo.setCarId(dispatchSendCarDto.getCarId());
        if(dispatchSendCarDto.getCarId() != null){
            CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
            if(carInfo == null){
                throw new Exception("车辆id"+dispatchSendCarDto.getCarId()+",对应的车队不存在");
            }
            orderDispatcheDetailInfo.setCarCgId(carInfo.getCarGroupId());
        }
        orderDispatcheDetailInfo.setDriverId(dispatchSendCarDto.getDriverId());
        if(dispatchSendCarDto.getDriverId() !=null){
            CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
            carGroupDriverRelation.setDriverId(dispatchSendCarDto.getDriverId());
            List<CarGroupDriverRelation> carGroupDriverRelations = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
            if(CollectionUtils.isNotEmpty(carGroupDriverRelations)){
                Long carGroupId = carGroupDriverRelations.get(0).getCarGroupId();
                orderDispatcheDetailInfo.setDriverCgId(carGroupId);
            }else{
                throw new Exception("司机id"+dispatchSendCarDto.getDriverId()+",对应的车队不存在");
            }
        }
        if(dispatchSendCarDto.getIsFinishDispatch() == 1) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_YES_COMPLETE);
        }else if(dispatchSendCarDto.getIsFinishDispatch() == 2) {
            orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_NOT_COMPLETED);
        }
        orderDispatcheDetailInfo.setUpdateBy(String.valueOf(dispatchSendCarDto.getUserId()));
        orderDispatcheDetailInfo.setUpdateTime(DateUtils.getNowDate());
        if(dispatchSendCarDto.getInOrOut() == 1 ){
            orderDispatcheDetailInfo.setInnerDispatcher(dispatchSendCarDto.getUserId());
        }else if(dispatchSendCarDto.getInOrOut() == 2 ){
            orderDispatcheDetailInfo.setOuterDispatcher(dispatchSendCarDto.getUserId());
        }
        orderDispatcheDetailInfoMapper.updateOrderDispatcheDetailInfoByOrderId(orderDispatcheDetailInfo);
    }

    /**
     * 是否调度完成（是则状态为已完成调度，否则为未完成）
     * @param dispatchSendCarDto
     */
    public abstract  void judgeIsFinish(DispatchSendCarDto dispatchSendCarDto);

    /**
     * 调度完成以后发短信
     * @param dispatchSendCarDto
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public  void  sendSms(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        String carGroupName = "";
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchSendCarDto.getOrderId());
        //订单编号
        String orderNumber = orderInfo.getOrderNumber();
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyId(orderInfo.getJourneyId());
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
        //用车人姓名
        String name = journeyPassengerInfos.get(0).getName();
        //用车人电话
        String mobile = journeyPassengerInfos.get(0).getMobile();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        //预约用车天数
        String useTime = journeyInfo.getUseTime();
        //用车时间
        Date startDate = journeyInfo.getStartDate();
        String useCarTime = DateUtils.parseDateToStr("yyyy年MM月dd日 HH:mm", startDate);
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(orderInfo.getPowerId());
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyUserCarPower.getApplyId());
        //用车备注
        String notes = applyInfo.getNotes();

        //驾驶员发短信
        if (dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_NO)){
            Map<String,String> stringStringMapDriver = new HashMap<>(10);
            stringStringMapDriver.put("orderNumber", orderNumber);
            stringStringMapDriver.put("useCarTime", useCarTime);
            stringStringMapDriver.put("name", name);
            stringStringMapDriver.put("mobile", mobile);
            stringStringMapDriver.put("notes", notes);
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_DRIVER,stringStringMapDriver,mobile );
        }



        if(dispatchSendCarDto.getInOrOut() == 1){
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(dispatchSendCarDto.getUserId());
            //内部调度员姓名和电话
            String userName = ecmpUser.getUserName();
            String phoneNumber = ecmpUser.getPhonenumber();
            if(dispatchSendCarDto.getUseCarGroupType().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_CAR)){
                    if (dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_YES)){
                            CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
                            //车型
                            String carType = carInfo.getCarType();
                            //车牌号
                            String carLicense = carInfo.getCarLicense();
                            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());
                            //车队名字
                            carGroupName = carGroupInfo.getCarGroupName();

                            //车队电话
                            String telephone = carGroupInfo.getTelephone();
                            Map<String,String> stringStringMap = new HashMap<>(10);
                            stringStringMap.put("useCarTime", useCarTime);
                            stringStringMap.put("orderNumber", orderNumber);
                            stringStringMap.put("carType", carType);
                            stringStringMap.put("carLicense", carLicense);
                            stringStringMap.put("carGroupName", carGroupName);
                            stringStringMap.put("carGroupPhone", telephone);
                            stringStringMap.put("dispatchName", userName);
                            stringStringMap.put("dispatchMobile", phoneNumber);
                            //用车人发短信
                            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_NO_SELF_TO_USER_CAR, stringStringMap, mobile);
                    }
                    CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());
                    //内部调度员车队名字
                    carGroupName = carGroupInfo.getCarGroupName();
                }else if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_DRIVER)){
                    CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
                    carGroupDriverRelation.setDriverId(dispatchSendCarDto.getDriverId());
                    List<CarGroupDriverRelation> carGroupDriverRelations = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
                    if(CollectionUtils.isNotEmpty(carGroupDriverRelations)){
                        Long carGroupId = carGroupDriverRelations.get(0).getCarGroupId();
                        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
                        carGroupName = carGroupInfo.getCarGroupName();
                    }
                }else if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_CAR_DRIVER)){

                    CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
                    DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(dispatchSendCarDto.getDriverId());
                    //车型
                    String carType = carInfo.getCarType();
                    //车牌号
                    String carLicense = carInfo.getCarLicense();
                    //司机姓名
                    String driverName = driverInfo.getDriverName();
                    //司机电话
                    String driverMobile = driverInfo.getMobile();

                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());
                    //车队名字
                    carGroupName = carGroupInfo.getCarGroupName();
                    //车队电话
                    String telephone = carGroupInfo.getTelephone();
                    Map<String,String> stringStringMap = new HashMap<>(10);
                    stringStringMap.put("useCarTime", useCarTime);
                    stringStringMap.put("orderNumber", orderNumber);
                    stringStringMap.put("carType", carType);
                    stringStringMap.put("carLicense", carLicense);
                    stringStringMap.put("driverName", driverName);
                    stringStringMap.put("driverMobile", driverMobile);
                    stringStringMap.put("carGroupName", carGroupName);
                    stringStringMap.put("carGroupPhone", telephone);
                    stringStringMap.put("dispatchName", userName);
                    stringStringMap.put("dispatchMobile", phoneNumber);
                    //用车人发短信
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_USER_CAR, stringStringMap, mobile);
                    return;

                }
            }else if (dispatchSendCarDto.getUseCarGroupType().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_OUT)){
                CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                carGroupDispatcherInfo.setUserId(dispatchSendCarDto.getUserId());
                List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                if(CollectionUtils.isNotEmpty(carGroupDispatcherInfos)){
                    Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
                    carGroupName = carGroupInfo.getCarGroupName();
                }
            }
            Map<String,String> stringStringMap = new HashMap<>(8);
            stringStringMap.put("carGroup", carGroupName);
            stringStringMap.put("innerDisName", userName);
            stringStringMap.put("innerDisMobile", phoneNumber);
            stringStringMap.put("useCarName", name);
            stringStringMap.put("useCarMobile", mobile);
            stringStringMap.put("useCarCount", useTime);
            stringStringMap.put("useCarTime", useCarTime);
            stringStringMap.put("notes", notes);

            Long outCarGroupId = dispatchSendCarDto.getOutCarGroupId();
            //给外部调度员发短信
            sendSmsReal(SmsTemplateConstant.SMS_FOSHAN_SEND_CAR_TO_OUT_DISPATCHER, stringStringMap, outCarGroupId);

        }else if(dispatchSendCarDto.getInOrOut() == 2){

            if(dispatchSendCarDto.getCarGroupUseMode() == null){
                if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_NO)){
                    CarGroupInfo carGroupInfo1 = carGroupInfoMapper.selectCarGroupInfoById(dispatchSendCarDto.getOutCarGroupId());
                    CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                    carGroupDispatcherInfo.setCarGroupId(carGroupInfo1.getCarGroupId());
                    List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                    Long outerDispatcher = carGroupDispatcherInfos.get(0).getUserId();

                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(outerDispatcher);
                    //外部调度员姓名和电话
                    String userName = ecmpUser.getUserName();
                    String phoneNumber = ecmpUser.getPhonenumber();
                    CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
                    DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(dispatchSendCarDto.getDriverId());
                    //车型
                    String carType = carInfo.getCarType();
                    //车牌号
                    String carLicense = carInfo.getCarLicense();
                    //司机姓名
                    String driverName = driverInfo.getDriverName();
                    //司机电话
                    String driverMobile = driverInfo.getMobile();

                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(dispatchSendCarDto.getOutCarGroupId());
                    //车队名字
                    carGroupName = carGroupInfo.getCarGroupName();
                    //车队电话
                    String telephone = carGroupInfo.getTelephone();
                    Map<String,String> stringStringMap = new HashMap<>(10);
                    stringStringMap.put("useCarTime", useCarTime);
                    stringStringMap.put("orderNumber", orderNumber);
                    stringStringMap.put("carType", carType);
                    stringStringMap.put("carLicense", carLicense);
                    stringStringMap.put("driverName", driverName);
                    stringStringMap.put("driverMobile", driverMobile);
                    stringStringMap.put("carGroupName", carGroupName);
                    stringStringMap.put("carGroupPhone", telephone);
                    stringStringMap.put("dispatchName", userName);
                    stringStringMap.put("dispatchMobile", phoneNumber);
                    //用车人发短信
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_USER_CAR, stringStringMap, mobile);
                }else if(dispatchSendCarDto.getSelfDrive().equals(CarConstant.SELFDRIVER_YES)){

                    CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
                    //车型
                    String carType = carInfo.getCarType();
                    //车牌号
                    String carLicense = carInfo.getCarLicense();
                    OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
                    OrderDispatcheDetailInfo orderDispatcheDetailInfo1 = orderDispatcheDetailInfo;
                    orderDispatcheDetailInfo1.setOrderId(dispatchSendCarDto.getOrderId());
                    List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = orderDispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo1);
                    OrderDispatcheDetailInfo orderDispatcheDetailInfo2 = orderDispatcheDetailInfos.get(0);
                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(orderDispatcheDetailInfo2.getCarCgId());
                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(dispatchSendCarDto.getUserId());
                    //外部调度员姓名和电话
                    String userName = ecmpUser.getUserName();
                    String phoneNumber = ecmpUser.getPhonenumber();
                    //车队名字
                    carGroupName = carGroupInfo.getCarGroupName();
                    //车队电话
                    String telephone = carGroupInfo.getTelephone();
                    Map<String,String> stringStringMap = new HashMap<>(10);
                    stringStringMap.put("useCarTime", useCarTime);
                    stringStringMap.put("orderNumber", orderNumber);
                    stringStringMap.put("carType", carType);
                    stringStringMap.put("carLicense", carLicense);
                    stringStringMap.put("carGroupName", carGroupName);
                    stringStringMap.put("carGroupPhone", telephone);
                    stringStringMap.put("dispatchName", userName);
                    stringStringMap.put("dispatchMobile", phoneNumber);
                    //用车人发短信
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_NO_SELF_TO_USER_CAR, stringStringMap, mobile);
                }
            }else{
                String telephone = "";
                OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
                OrderDispatcheDetailInfo orderDispatcheDetailInfo1 = orderDispatcheDetailInfo;
                orderDispatcheDetailInfo1.setOrderId(dispatchSendCarDto.getOrderId());
                List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = orderDispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo1);
                OrderDispatcheDetailInfo orderDispatcheDetailInfo2 = orderDispatcheDetailInfos.get(0);
                Long innerDispatcher = orderDispatcheDetailInfo2.getInnerDispatcher();
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(innerDispatcher);
                //内部调度员姓名和电话
                String userName = ecmpUser.getUserName();
                String phoneNumber = ecmpUser.getPhonenumber();

                CarInfo carInfo = carInfoMapper.selectCarInfoById(orderDispatcheDetailInfo2.getCarId());
                DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(orderDispatcheDetailInfo2.getDriverId());
                //车型
                String carType = carInfo.getCarType();
                //车牌号
                String carLicense = carInfo.getCarLicense();
                //司机姓名
                String driverName = driverInfo.getDriverName();
                //司机电话
                String driverMobile = driverInfo.getMobile();


                //车队名字
                if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_CAR)){
                    CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(orderDispatcheDetailInfo2.getCarCgId());
                    carGroupName = carGroupInfo.getCarGroupName();
                    //车队电话
                     telephone = carGroupInfo.getTelephone();
                }
                if(dispatchSendCarDto.getCarGroupUseMode().equals(CarConstant.CAR_GROUP_USER_MODE_DRIVER)){
                    CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
                    carGroupDriverRelation.setDriverId(orderDispatcheDetailInfo2.getDriverId());
                    List<CarGroupDriverRelation> carGroupDriverRelations = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
                    if(CollectionUtils.isNotEmpty(carGroupDriverRelations)){
                        Long carGroupId = carGroupDriverRelations.get(0).getCarGroupId();
                        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
                        carGroupName = carGroupInfo.getCarGroupName();
                        //车队电话
                         telephone = carGroupInfo.getTelephone();
                    }
                }
                Map<String,String> stringStringMap = new HashMap<>(10);
                stringStringMap.put("useCarTime", useCarTime);
                stringStringMap.put("orderNumber", orderNumber);
                stringStringMap.put("carType", carType);
                stringStringMap.put("carLicense", carLicense);
                stringStringMap.put("driverName", driverName);
                stringStringMap.put("driverMobile", driverMobile);
                stringStringMap.put("carGroupName", carGroupName);
                stringStringMap.put("carGroupPhone", telephone);
                stringStringMap.put("dispatchName", userName);
                stringStringMap.put("dispatchMobile", phoneNumber);
                //用车人发短信
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_USER_CAR, stringStringMap, mobile);
            }
        }
    }

    /**
     * 给调度员发短信
     * @param smsTemplateCode
     * @param paramsMap
     * @param carGroupId
     */
    private void sendSmsReal(String smsTemplateCode,Map<String,String> paramsMap,Long carGroupId) throws Exception {
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setCarGroupId(carGroupId);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        if(CollectionUtils.isNotEmpty(carGroupDispatcherInfos)){
            for (CarGroupDispatcherInfo carGroupDispatcherInfo1:
                    carGroupDispatcherInfos ) {
                EcmpUser ecmpUser1 = ecmpUserMapper.selectEcmpUserById(carGroupDispatcherInfo1.getUserId());
                iSmsTemplateInfoService.sendSms(smsTemplateCode,paramsMap,ecmpUser1.getPhonenumber() );
            }

        }
    }
}
