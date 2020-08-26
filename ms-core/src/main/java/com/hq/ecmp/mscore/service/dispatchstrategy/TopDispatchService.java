package com.hq.ecmp.mscore.service.dispatchstrategy;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/26 10:51
 * @Version 1.0
 */
@Service
@Slf4j
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
    @Resource
    private IEcmpOrgService ecmpOrgService;

    /**
     * 业务执行方法
     * @param dispatchSendCarDto
     */
    public void disBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        validIsSendCar(dispatchSendCarDto);
        judgeIsFinish(dispatchSendCarDto);
        ((TopDispatchService)AopContext.currentProxy()).dispatchCommonBusiness(dispatchSendCarDto);
        ((TopDispatchService)AopContext.currentProxy()).sendSms(dispatchSendCarDto);
    }

    /**
     * 校验是否可以派车
     *  1. 内部调度员选了车队A，车队A操作选人选司机时，内部调度员派给了车队B，此时车队A提交派车，验证，调度表的车队id是否是当前提交申请的车队，如果不是，则不能派车成功。
     *  2. 内部调度员选了车队A，车队A已经完成派车和派司机，内部调度员在派给车队B，此时验证是否完成调度，已经完成，则不能派车成功。
     * @param dispatchSendCarDto
     * @return  1 外部调度已经完成，不可在派给其他外部车队
     *           2 内部调度已经派给其他外部车队，此外部车队不可在提交派车
     *           3 可以正常往下走逻辑
     */
    protected  void validIsSendCar(DispatchSendCarDto dispatchSendCarDto){
        int inOrOut = dispatchSendCarDto.getInOrOut();
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfoMapper.selectDispatcheInfo(dispatchSendCarDto.getOrderId());
        if(inOrOut == 1){
            if(orderDispatcheDetailInfo.getDispatchState().equals(CarConstant.DISPATCH_YES_COMPLETE)){
                Long outerDispatcher = orderDispatcheDetailInfo.getOuterDispatcher();
                CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                carGroupDispatcherInfo.setUserId(outerDispatcher);
                List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
                CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
                String carGroupName = carGroupInfo.getCarGroupName();
                String msg = "您好，此订单"+carGroupName+"车队已完成派车，若您想重新调度，请改派订单。";
                throw new BaseException(msg);
            }
        }else if(inOrOut == 2){
            Long userId = dispatchSendCarDto.getUserId();
            String carGroupIdsStr = carGroupDispatcherInfoMapper.selectCarGroupDispatcherAllId(userId);
            String[] split = carGroupIdsStr.split(",");
            List<String> carGroupIds = Arrays.asList(split);
            if(!carGroupIds.contains(orderDispatcheDetailInfo.getNextCarGroupId().toString())){
                Long innerDispatcher = orderDispatcheDetailInfo.getInnerDispatcher();
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(innerDispatcher);
                String nickName = ecmpUser.getNickName();
                CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(orderDispatcheDetailInfo.getNextCarGroupId());
                String carGroupName = carGroupInfo.getCarGroupName();
                String msg = "您好，此订单已由机关车队调度员"+nickName+"另行指派给"+carGroupName+"车队提供服务，请知悉。";
                throw new BaseException(msg);
            }
        }
    }


    /**
     * 调度公共逻辑
     * @param dispatchSendCarDto
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void dispatchCommonBusiness(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        //查询之前的调度信息
        OrderDispatcheDetailInfo orginOrderDispatcheDetailInfo = orderDispatcheDetailInfoMapper.selectDispatcheInfo(dispatchSendCarDto.getOrderId());

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(dispatchSendCarDto.getOrderId());
        orderInfo.setCarId(dispatchSendCarDto.getCarId());
        orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);//佛山默认自有车
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
        //派车备注
        orderDispatcheDetailInfo.setRemark(dispatchSendCarDto.getRemark());
        if(dispatchSendCarDto.getCarId() != null){
            CarInfo carInfo = carInfoMapper.selectCarInfoById(dispatchSendCarDto.getCarId());
            if(carInfo == null){
                throw new Exception("车辆id"+dispatchSendCarDto.getCarId()+",对应的车队不存在");
            }
            orderDispatcheDetailInfo.setCarCgId(carInfo.getCarGroupId());
        }else{
            orderDispatcheDetailInfo.setCarCgId(null);
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
        }else{
            orderDispatcheDetailInfo.setDriverCgId(null);
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

        //如果是内部调度员把订单转给外部调度员需要发短信
        try {
            if(dispatchSendCarDto.getInOrOut() == 1 ){
                Long dispatchOutCarGroupId=dispatchSendCarDto.getOutCarGroupId();
                log.info("orderId:{} 内部调用员操作 dispatchCommonBusiness==> outCarGroupId:{}",dispatchSendCarDto.getOrderId(),dispatchOutCarGroupId);
                if(null!=dispatchOutCarGroupId) {
                    if (null != orginOrderDispatcheDetailInfo) {
                        Long beforeCarGroupId = orginOrderDispatcheDetailInfo.getNextCarGroupId();
                        log.info("orderId:{} 内部调用员操作 dispatchCommonBusiness==> 改派前,库里carGroupId:{}",dispatchSendCarDto.getOrderId(),beforeCarGroupId);
                        if(null!=beforeCarGroupId){
                            if(!dispatchOutCarGroupId.equals(beforeCarGroupId)){
                                //给之前的外部调度员发短信
                                List<String> groupDispatcherList = carGroupInfoMapper.selectGroupDispatcherList(beforeCarGroupId);
                                if(CollectionUtils.isNotEmpty(groupDispatcherList)){
                                    //发短信
                                    OrderInfo queryOrder = orderInfoMapper.selectOrderInfoById(dispatchSendCarDto.getOrderId());

                                    JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(queryOrder.getJourneyId());
                                    //用车时间
                                    Date startDate = journeyInfo.getStartDate();
                                    String useCarTime = DateUtils.parseDateToStr("yyyy年MM月dd日 HH:mm", startDate);

                                    JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
                                    journeyPassengerInfo.setJourneyId(queryOrder.getJourneyId());
                                    List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
                                    //用车人姓名
                                    String name = journeyPassengerInfos.get(0).getName();
                                    //用车人电话
                                    String mobile = journeyPassengerInfos.get(0).getMobile();

                                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(dispatchSendCarDto.getUserId());
                                    //内部调度员姓名和电话
                                    String userName = ecmpUser.getNickName();
                                    String phoneNumber = ecmpUser.getPhonenumber();

                                    Map<String,String> stringStringMap = new HashMap<>(4);
                                    stringStringMap.put("orderNumber", queryOrder.getOrderNumber());
                                    stringStringMap.put("useTime", useCarTime);
                                    stringStringMap.put("useCarPeople", name+" "+mobile);
                                    stringStringMap.put("dispatcher", userName+" "+phoneNumber);
                                    log.info("orderId:{} 内部调用员操作 dispatchCommonBusiness==> 短信内容:{}",dispatchSendCarDto.getOrderId(),stringStringMap);
                                    for(String dispatcherMobile:groupDispatcherList){
                                        //用车人发短信
                                        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NOTIFY_OUT_GROUP_MSG, stringStringMap, dispatcherMobile);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("orderId:{} 内部调度员多次派车给外部调度员，发短信失败,e:{}",dispatchSendCarDto.getOrderId(),e);
        }
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
    public  void  sendSms(DispatchSendCarDto dispatchSendCarDto) throws Exception {
        log.info("调度短信发送开发------------------------------");
        String carGroupName = "";
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchSendCarDto.getOrderId());
        Long userId = orderInfo.getUserId();
        EcmpUser ecmpUser1 = ecmpUserMapper.selectEcmpUserById(userId);
        //申请人
        String applyNameMobile = ecmpUser1.getPhonenumber();
        //申请人单位
        String applyDeptName = "";
        EcmpOrgDto ecmpOrgDto = ecmpOrgService.getDeptDetails(ecmpUser1.getDeptId());
        if(ecmpOrgDto != null){
            applyDeptName = ecmpOrgDto.getDeptName();
        }
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
        if (notes == null){
            notes = "无";
        }

        if(dispatchSendCarDto.getInOrOut() == 1){
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(dispatchSendCarDto.getUserId());
            //内部调度员姓名和电话
            String userName = ecmpUser.getNickName();
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
                            if (!mobile.equals(applyNameMobile)) {
                                //申请人发短信
                                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_NO_SELF_TO_APPLY_CAR, stringStringMap, applyNameMobile);
                            }
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
                    if (!mobile.equals(applyNameMobile)) {
                        //申请人发短信
                        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_APPLY_CAR, stringStringMap, applyNameMobile);
                    }
                    Map<String,String> stringStringMapDriver = new HashMap<>(10);
                    stringStringMapDriver.put("orderNumber", orderNumber);
                    stringStringMapDriver.put("useCarTime", useCarTime);
                    stringStringMapDriver.put("name", name);
                    stringStringMapDriver.put("mobile", mobile);
                    stringStringMapDriver.put("applyDeptName", applyDeptName);
                    stringStringMapDriver.put("notes", notes);
                    //司机发短信
                    log.info("司机发短信={}", JSONObject.toJSONString(stringStringMapDriver));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_DRIVER,stringStringMapDriver,driverMobile );

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

                //内部调度员把订单指派给外部车队的时候，需要给申请人发短信-S
                //查询外部车队信息
                String dispatcher="";
                CarGroupInfo carGroupInfo1 = carGroupInfoMapper.selectCarGroupInfoById(dispatchSendCarDto.getOutCarGroupId());
                CarGroupDispatcherInfo cg = new CarGroupDispatcherInfo();
                cg.setCarGroupId(dispatchSendCarDto.getOutCarGroupId());
                List<CarGroupDispatcherInfo> cgDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(cg);
                if(CollectionUtils.isNotEmpty(cgDispatcherInfos)){
                    Long outerDispatcher = cgDispatcherInfos.get(0).getUserId();
                    EcmpUser outDispatcher = ecmpUserMapper.selectEcmpUserById(outerDispatcher);
                    dispatcher=outDispatcher.getNickName()+" "+outDispatcher.getPhonenumber();
                }
                Map<String,String> stringStringMap = new HashMap<>(7);
                stringStringMap.put("useTime", useCarTime);
                stringStringMap.put("applyDeptName", applyDeptName);
                stringStringMap.put("useCarPeople", name+" "+mobile);
                stringStringMap.put("carGroupName", carGroupInfo1.getCarGroupName());
                stringStringMap.put("carGroupNm", carGroupInfo1.getCarGroupName());
                stringStringMap.put("carGroupTel", carGroupInfo1.getTelephone());
                stringStringMap.put("dispatcher", StringUtils.isEmpty(dispatcher)?" ":dispatcher);
                log.info("orderId={} 给申请人发短信={}", dispatchSendCarDto.getOrderId(),JSONObject.toJSONString(stringStringMap));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_NOTIFY_APPLY_PEO_MSG, stringStringMap, applyNameMobile);
                //内部调度员把订单指派给外部车队的时候，需要给申请人发短信-E
            }
            if(dispatchSendCarDto.getOutCarGroupId() != null){
                Map<String,String> stringStringMap = new HashMap<>(8);
                stringStringMap.put("carGroupName", carGroupName);
                stringStringMap.put("userName", userName);
                stringStringMap.put("phoneNumber", phoneNumber);
                stringStringMap.put("name", name);
                stringStringMap.put("mobile", mobile);
                stringStringMap.put("useTime", useTime);
                stringStringMap.put("useCarTime", useCarTime);
                stringStringMap.put("notes", notes);

                Long outCarGroupId = dispatchSendCarDto.getOutCarGroupId();
                //给外部调度员发短信
                sendSmsReal(SmsTemplateConstant.SMS_FOSHAN_SEND_CAR_TO_OUT_DISPATCHER, stringStringMap, outCarGroupId);
            }

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
                    String userName = ecmpUser.getNickName();
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
                    if (!mobile.equals(applyNameMobile)) {
                        //申请人发短信
                        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_APPLY_CAR, stringStringMap, applyNameMobile);
                    }
                    Map<String,String> stringStringMapDriver = new HashMap<>(10);
                    stringStringMapDriver.put("orderNumber", orderNumber);
                    stringStringMapDriver.put("useCarTime", useCarTime);
                    stringStringMapDriver.put("name", name);
                    stringStringMapDriver.put("mobile", mobile);
                    stringStringMapDriver.put("applyDeptName", applyDeptName);
                    stringStringMapDriver.put("notes", notes);
                    //司机发短信
                    log.info("司机发短信={}", JSONObject.toJSONString(stringStringMapDriver));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_DRIVER,stringStringMapDriver,driverMobile );
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
                    String userName = ecmpUser.getNickName();
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
                    if (!mobile.equals(applyNameMobile)) {
                        //申请人发短信
                        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_NO_SELF_TO_APPLY_CAR, stringStringMap, applyNameMobile);
                    }
                }
            }else{
                String telephone = "";
                OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
                OrderDispatcheDetailInfo orderDispatcheDetailInfo1 = orderDispatcheDetailInfo;
                orderDispatcheDetailInfo1.setOrderId(dispatchSendCarDto.getOrderId());
                List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = orderDispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(orderDispatcheDetailInfo1);
                OrderDispatcheDetailInfo orderDispatcheDetailInfo2 = orderDispatcheDetailInfos.get(0);
                // Long innerDispatcher = orderDispatcheDetailInfo2.getInnerDispatcher();
                // EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(innerDispatcher);
                Long outerDispatcher = orderDispatcheDetailInfo2.getOuterDispatcher();
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(outerDispatcher);
                //内部调度员姓名和电话
                String userName = ecmpUser.getNickName();
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
                if (!mobile.equals(applyNameMobile)) {
                    //申请人发短信
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_APPLY_CAR, stringStringMap, applyNameMobile);
                }
                Map<String,String> stringStringMapDriver = new HashMap<>(10);
                stringStringMapDriver.put("orderNumber", orderNumber);
                stringStringMapDriver.put("useCarTime", useCarTime);
                stringStringMapDriver.put("name", name);
                stringStringMapDriver.put("mobile", mobile);
                stringStringMapDriver.put("applyDeptName", applyDeptName);
                stringStringMapDriver.put("notes", notes);
                //司机发短信
                log.info("司机发短信={}", JSONObject.toJSONString(stringStringMapDriver));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSAN_SEND_CAR_TO_DRIVER,stringStringMapDriver,driverMobile );
            }
        }
        log.info("调度短信发送结束------------------------------");
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
