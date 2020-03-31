package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.EcmpMessageService;
import com.hq.ecmp.mscore.service.IsmsBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SmsBusinessImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/26 21:19
 * @Version 1.0
 */
@Service
@Slf4j
public class SmsBusinessImpl implements IsmsBusiness{

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private ISmsTemplateInfoService iSmsTemplateInfoService;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private EcmpMessageMapper ecmpMessageMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;



    /**
     * 网约车约车失败短信发送
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsCallTaxiNetFail(Long orderId){
        log.info("短信开始-订单{},约车超时", orderId);
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            String useCarTime = orderCommonInfo.get("useCarTime");
            String planBeginAddress = orderCommonInfo.get("planBeginAddress");
            String planEndAddress = orderCommonInfo.get("planEndAddress");
            Map<String,String> paramsMapRider = new HashMap<>();
            paramsMapRider.put("useCarTime",useCarTime);
            paramsMapRider.put("planBeginAddress",planBeginAddress);
            paramsMapRider.put("planEndAddress",planEndAddress);
            if(riderAndApplyMatch(applyAndRiderMobile)){
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_RIDER,paramsMapRider ,riderMobile);
            }else{
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_RIDER,paramsMapRider ,riderMobile);
                Map<String,String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("riderMobile",riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_APPLICANT,paramsMapApplicant,applyMobile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},约车超时", orderId);
    }


    /**
     * 网约车，约车成功短信通知
     */
    @Async
    @Override
    public void sendSmsCallTaxiNet(Long orderId)  {
        log.info("短信开始-订单{},约车成功", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            //司机姓名
            String driverName =  orderCommonInfo.get("driverName");
            //乘车人
            String riderMobile = orderCommonInfo.get("riderMobile");
            //乘车人姓名
            String riderName = orderCommonInfo.get("riderName");
            //申请人
            String applyMobile = orderCommonInfo.get("applyMobile");
            //用车时间
            String useCarTime =  orderCommonInfo.get("useCarTime");
            //车型
            String carType =  orderCommonInfo.get("carType");
            //车牌号
            String carLicense =  orderCommonInfo.get("carLicense");
            //上车地址
            String planBeginAddress =  orderCommonInfo.get("planBeginAddress");
            //下车地址
            String planEndAddress =  orderCommonInfo.get("planEndAddress");
            //订单号
            String orderNum = orderCommonInfo.get("orderNum");
            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("useCarTime", useCarTime);
            paramsMap.put("driverName", driverName);
            paramsMap.put("carType", carType);
            paramsMap.put("carLicense", carLicense);

            //乘车人和申请人不是一个人
            if(!riderAndApplyMatch(orderCommonInfo)){
                //申请人短信
                Map<String,String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_APPLICANT,paramsMapApplicant,applyMobile);
                //乘车人短信
                if(isEnterpriseWorker(riderMobile)){//企业员工
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_ENTER,paramsMap,riderMobile );
                }else{//非企业员
                    Map<String,String> paramsMapRiderNoE = new HashMap<>();
                    paramsMapRiderNoE.put("applyMobile", applyMobile);
                    paramsMapRiderNoE.put("useCarTime",useCarTime);
                    paramsMapRiderNoE.put("planBeginAddress", planBeginAddress);
                    paramsMapRiderNoE.put("planEndAddress",planEndAddress);
                    paramsMapRiderNoE.put("driverName",driverName);
                    paramsMapRiderNoE.put("carType",carType);
                    paramsMapRiderNoE.put("carLicense",carLicense);
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_NO_ENTER,paramsMapRiderNoE,riderMobile);
                }
            }else{//乘车人和申请人是一个人
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_ENTER,paramsMap,riderMobile );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},约车成功", orderId);
    }


    /**
     * 取消订单无取消费发送短信
     * @param orderId
     * @throws Exception
     */
    @Async
    @Override
    public void sendSmsCancelOrder(Long orderId)  {
        log.info("短信开始-订单{},取消", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            String planBeginAddress = orderCommonInfo.get("planBeginAddress");
            String planEndAddress = orderCommonInfo.get("planEndAddress");
            String useCarTime = orderCommonInfo.get("useCarTime");
            //给司机发送短信
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            String driverMobile = orderInfo.getDriverMobile();
            if(driverMobile != null){
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("planBeginAddress",planBeginAddress);
                paramsMap.put("planEndAddress",planEndAddress);
                paramsMap.put("useCarTime",useCarTime);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_DRIVER,paramsMap,driverMobile);
            }
            //给申请人发送短信
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                String applyMobile = applyAndRiderMobile.get("applyMobile");
                Map<String,String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("useCarTime",useCarTime);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_APPLICANT,paramsMapApplicant,applyMobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},取消", orderId);
    }

    /**
     * 取消订单有取消费发送短信
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsCancelOrderHaveFee(Long orderId,Double comAmount) {
        log.info("短信开始-订单{},取消-收费", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            String useCarTime = orderCommonInfo.get("useCarTime");
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            //给乘车人发送短信

            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            //乘车人发
            if(isEnterpriseWorker(riderMobile)){//企业员工
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("useCarTime",useCarTime);
                paramsMap.put("comAmount",String.valueOf(comAmount));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_HAVEFEE_RIDER_ENTER,paramsMap,riderMobile);
            }
            //申请人发
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile",riderMobile);
                paramsMap.put("useCarTime",useCarTime);
                paramsMap.put("comAmount",String.valueOf(comAmount));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_HAVEFEE_APPLICANT,paramsMap,applyMobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},取消-收费", orderId);

    }

    /**
     * 自有车司机到达发送短信
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsDriverArrivePrivate(Long orderId) {
        log.info("短信开始-订单{},自有车司机到达", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            //司机姓名
            String driverName =  orderCommonInfo.get("driverName");
            //乘车人
            String riderMobile = orderCommonInfo.get("riderMobile");
            //申请人
            String applyMobile = orderCommonInfo.get("applyMobile");
            //车牌号
            String carLicense =  orderCommonInfo.get("carLicense");

            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("driverName", driverName);
            paramsMap.put("carLicense", carLicense);
            //乘车人和申请人不是一个人
            if(!riderAndApplyMatch(orderCommonInfo)){
                //申请人短信
                Map<String,String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("riderMobile", riderMobile);
                paramsMapApplicant.put("driverName", riderMobile);
                paramsMapApplicant.put("carLicense", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_READY_APPLICANT,paramsMapApplicant,applyMobile);
                //乘车人短信
                if(isEnterpriseWorker(riderMobile)){//企业员工
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_ARR_RIDER_ENTER,paramsMap,riderMobile );
                }else{//非企业员
                    Map<String,String> paramsMapRiderNoE = new HashMap<>();
                    paramsMapRiderNoE.put("applyMobile", applyMobile);
                    paramsMapRiderNoE.put("driverName",driverName);
                    paramsMapRiderNoE.put("carLicense",carLicense);
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_ARR_RIDER_NO_ENTER,paramsMapRiderNoE,riderMobile);
                }
            }else{//乘车人和申请人是一个人
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_ENTER,paramsMap,riderMobile );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},自有车司机到达", orderId);
    }

    /**
     * 司机开始服务发送短信
     * @param orderId
     */
    @Override
    @Async
    public void sendSmsDriverBeginService(Long orderId) {
        log.info("短信开始-订单{},司机开始服务", orderId);
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            //乘车人
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_BEGINSERVICE_APPLICANT,paramsMap,applyMobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},司机开始服务", orderId);
    }


    /**
     * 司机服务结束发送短信
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsDriverServiceComplete(Long orderId) {
        log.info("短信开始-订单{},司机服务结束", orderId);
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            //乘车人
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETESERVICE_APPLICANT,paramsMap,applyMobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},司机服务结束", orderId);
    }

    /**
     * 申请人和乘车人是否是同一人的验证
     * @param mapMobiles
     * @return
     */
    public boolean riderAndApplyMatch(Map<String,String> mapMobiles) throws Exception {
        if(mapMobiles == null || "".equals(mapMobiles.get("riderMobile"))||"".equals(mapMobiles.get("applyMobile")))
            throw new Exception("乘车人或申请人信息异常");
        return  mapMobiles.get("riderMobile").equals(mapMobiles.get("applyMobile"));

    }

    /**
     * 获取申请人和乘车人电话
     * @return
     */
    public Map<String,String> getApplyAndRiderMobile(OrderInfo orderInfo){
        Map<String,String> mapMobiles = new HashMap<>();
        String riderMobile = "";
        String applyMobile = "";
        String riderName = "";
        String riderId = "";
        Long journeyId = orderInfo.getJourneyId();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(journeyId);
        Long applyUserId = journeyInfo.getUserId();
        mapMobiles.put("applyUserId", applyUserId.toString());
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(applyUserId);
        applyMobile = ecmpUser.getPhonenumber();
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setItIsPeer("01");
        journeyPassengerInfo.setJourneyId(orderInfo.getJourneyId());
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
        if(journeyPassengerInfos.size()>0){
            riderMobile = journeyPassengerInfos.get(0).getMobile();
            riderName = journeyPassengerInfos.get(0).getName();
            EcmpUser ecmpUser1 = new EcmpUser();
            ecmpUser1.setPhonenumber(riderMobile);
            List<EcmpUser> ecmpUsers = ecmpUserMapper.selectEcmpUserList(ecmpUser1);
            if(ecmpUsers.size()>0){
                EcmpUser ecmpUserCh = ecmpUsers.get(0);
                riderId = ecmpUserCh.getUserId()+"";
            }

        }
        if(!"".equals(riderMobile) && !"".equals(applyMobile)){
            mapMobiles.put("riderMobile", riderMobile);
            mapMobiles.put("applyMobile", applyMobile);
            mapMobiles.put("riderName", riderName);
            mapMobiles.put("applyUserId", String.valueOf(applyUserId));
            mapMobiles.put("riderId",riderId);
            return mapMobiles;
        }
        return null;
    }

    /**
     * 通过手机号判断是否是企业员
     * @return
     */
    public boolean isEnterpriseWorker(String riderMobile){
        EcmpUser ecmpUser = new EcmpUser();
        ecmpUser.setPhonenumber(riderMobile);
        List<EcmpUser> ecmpUsers = ecmpUserMapper.selectEcmpUserList(ecmpUser);
        if(ecmpUser != null && ecmpUsers.size()>0){
            return true;
        }
        return false;
    }
    /**
     * 获取订单公共信息
     */
    public Map<String,String> getOrderCommonInfo(Long orderId){
        Map<String,String> result = new HashMap<>();
        //用车时间
        String useCarTime = null;
        //出发地
        String planBeginAddress = null;
        //目的地
        String planEndAddress = null;
        //司机姓名
        String driverName = null;
        //司机手机号
        String driverMobile = null;
        //司机编号
        String driverId=null;
        //乘车人手机号
        String riderMobile = null;
        //乘车人姓名
        String riderName = null;
        //申请人手机号
        String applyMobile = null;
        //申请人编号
        String applyUserId=null;
        //车型
        String carType = null;
        //车牌号
        String carLicense = null;
        //订单号
        String orderNum = null;

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        driverName = orderInfo.getDriverName();
        driverMobile = orderInfo.getDriverMobile();
        driverId=(null==orderInfo.getDriverId()?null:orderInfo.getDriverId().toString());
        carLicense = orderInfo.getCarLicense();
        orderNum = orderInfo.getOrderNumber();
        if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
            Long carId = orderInfo.getCarId();
            if(carId != null){
                CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
                carType = carInfo.getCarType();
                carLicense=carInfo.getCarLicense();
            }
        }else{
            carType = orderInfo.getCarModel();
        }
        Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
        riderMobile = applyAndRiderMobile.get("riderMobile");
        applyMobile = applyAndRiderMobile.get("applyMobile");
        applyUserId=applyAndRiderMobile.get("applyUserId");
        riderName = applyAndRiderMobile.get("riderName");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        if(orderAddressInfos != null && orderAddressInfos.size()>0 ){
            for (OrderAddressInfo orderAddressInfo1:
                    orderAddressInfos) {
                if(orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)){
                    planBeginAddress = orderAddressInfo1.getAddress();
                    useCarTime = simpleDateFormat.format(orderAddressInfo1.getActionTime());
                }else if(orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)){
                    planEndAddress = orderAddressInfo1.getAddress();
                }else{

                }
            }
        }
        result.put("useCarTime", useCarTime);
        result.put("planBeginAddress", planBeginAddress);
        result.put("planEndAddress",planEndAddress);
        result.put("driverName",driverName );
        result.put("driverMobile",driverMobile);
        result.put("driverId",driverId);
        result.put("riderMobile",riderMobile);
        result.put("riderName",riderName);
        result.put("applyMobile",applyMobile);
        result.put("applyUserId",applyUserId);
        result.put("carType",carType);
        result.put("carLicense",carLicense);
        result.put("orderNum",orderNum);
        return result;
    }

    /**
     * 消息通知-订单取消-给司机发通知（自有车）
     * @param orderId
     * @param createId
     */
    @Async
    @Override
    public void sendMessageCancelOrder(Long orderId,Long createId){
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(),orderInfo.getDriverId(),
                MsgConstant.MESSAGE_T005.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                orderId,createId,"您有一条任务被取消，请及时查看！");
    }

    /**
     * 服务开始消息通知
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void sendMessageServiceStart(Long orderId, Long createId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String applyUserId = applyAndRiderMobile.get("applyUserId");
            String riderId = applyAndRiderMobile.get("riderId");
            //申请人
            sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(),Long.parseLong(applyUserId),
                    MsgConstant.MESSAGE_T006.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                    orderId,createId,"您有一个行程正在进行中，请及时查看！");
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                //乘车人
                if(!"".equals(riderId)){
                    sendMessage(MsgUserConstant.MESSAGE_USER_USER.getType(),Long.parseLong(riderId),
                            MsgConstant.MESSAGE_T006.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                            orderId,createId,"您有一个行程正在进行中，请及时查看！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务结束未确认行程
     * @param orderId
     */
    @Override
    @Async
    public void endServiceNotConfirm(Long orderId){
        log.info("短信开始-订单{},司机服务结束乘客未确认行程", orderId);
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            //乘车人
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                Map<String,String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETE_NOT_CONFIRM,paramsMap,applyMobile);
            }else{
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETE_NOT_CONFIRM_RIDER,null,applyMobile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("短信结束-订单{},司机服务结束", orderId);
    }

    /**
     * 司机已到达短信(网约车)
     * @param orderId
     */
    @Override
    @Async
    public void driverArriveMessage(Long orderId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String riderId = applyAndRiderMobile.get("riderId");
            //乘车人企业员工
            Map<String,String> paramsMap= Maps.newHashMap();
            paramsMap.put("driverName",orderCommonInfo.get("driverName"));
            paramsMap.put("time",orderCommonInfo.get("useCarTime").substring(11));
            paramsMap.put("carLicense",orderCommonInfo.get("carLicense"));
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_ENTER,paramsMap,applyAndRiderMobile.get("riderMobile"));
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                //乘车人非企业员
                if(StringUtils.isEmpty(riderId)){
                    paramsMap.put("applyMobile",applyAndRiderMobile.get("applyMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER,paramsMap,applyAndRiderMobile.get("riderMobile"));
                }else{//申请人
                    paramsMap.put("riderMobile",applyAndRiderMobile.get("riderMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_APPLY,paramsMap,applyAndRiderMobile.get("applyMobile"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始服务发送短信网约车
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void startService(Long orderId,Long createId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String riderId = applyAndRiderMobile.get("riderId");
            //乘车人企业员工

            if(!riderAndApplyMatch(applyAndRiderMobile)){
                //乘车人非企业员
                Map<String,String> paramsMap= Maps.newHashMap();
                paramsMap.put("driverName",orderCommonInfo.get("driverName"));
                paramsMap.put("time",orderCommonInfo.get("useCarTime").substring(11));
                paramsMap.put("carLicense",orderCommonInfo.get("carLicense"));
                if(StringUtils.isEmpty(riderId)){
                    paramsMap.put("applyMobile",applyAndRiderMobile.get("applyMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER,paramsMap,applyAndRiderMobile.get("riderMobile"));
                }else{//申请人
                    paramsMap.put("riderMobile",applyAndRiderMobile.get("riderMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER,paramsMap,applyAndRiderMobile.get("applyMobile"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改派成功消息通知
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void sendMessageReassignSucc(Long orderId, Long createId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            Long driverId = orderInfo.getDriverId();
            String applyUserId = applyAndRiderMobile.get("applyUserId");
            String riderId = applyAndRiderMobile.get("riderId");
            //司机
            if(driverId != null){
                sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(),driverId,
                        MsgConstant.MESSAGE_T004.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                        orderId,createId,"您有一条改派通知，请及时查看！");
            }
            //申请人
            sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(),Long.parseLong(applyUserId),
                    MsgConstant.MESSAGE_T004.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                    orderId,createId,"您有一个行程发生改派变更，请及时查看！");
            if(!riderAndApplyMatch(applyAndRiderMobile)){
                //乘车人
                if(!"".equals(riderId)){
                    sendMessage(MsgUserConstant.MESSAGE_USER_USER.getType(),Long.parseLong(riderId),
                            MsgConstant.MESSAGE_T004.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                            orderId,createId,"您有一个行程发生改派变更，请及时查看！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 差旅下单成功，发送消息通知-调度员
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void sendMessagePriTravelOrderSucc(Long orderId, Long createId) {
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        if(orderAddressInfos.size()>0){
            for (OrderAddressInfo orderAddressInfoCh1:
            orderAddressInfos) {
                if(orderAddressInfoCh1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)){
                    String cityPostalCode = orderAddressInfoCh1.getCityPostalCode();
                    CarGroupInfo carGroupInfo = new CarGroupInfo();
                    carGroupInfo.setCity(cityPostalCode);
                    List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
                    CarGroupInfo carGroupInfoCh = carGroupInfos.get(0);
                    CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                    carGroupDispatcherInfo.setCarGroupId(carGroupInfoCh.getCarGroupId());
                    List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                    for (CarGroupDispatcherInfo carGroupDispatcherInfo1:
                    carGroupDispatcherInfos) {
                        sendMessage(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType(),carGroupDispatcherInfo1.getUserId(),
                                MsgConstant.MESSAGE_T009.getType(),MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                                orderId,createId,"您有一条调度任务待处理,请注意查看！");
                    }
                    break;
                 }
            }
        }
    }

    /**
     * 基础方法
     * @param role
     * @param personId
     * @param type
     * @param businessType
     * @param businessId
     * @param createId
     * @param msg
     */
    private void sendMessage(int role,Long personId ,String type,String businessType,Long businessId,Long createId,String msg){
        EcmpMessage ecmpMessage = new EcmpMessage();
        ecmpMessage.setConfigType(role);
        ecmpMessage.setEcmpId(personId);
        ecmpMessage.setType(businessType);
        ecmpMessage.setStatus(MsgStatusConstant.MESSAGE_STATUS_T002.getType());
        ecmpMessage.setContent(msg);
        ecmpMessage.setCategory(type);
        ecmpMessage.setCategoryId(businessId);
        ecmpMessage.setUrl("");
        ecmpMessage.setCreateBy(createId);
        ecmpMessage.setCreateTime(DateUtils.getNowDate());
        ecmpMessage.setUpdateBy(null);
        ecmpMessage.setUpdateTime(null);
        ecmpMessageMapper.insert(ecmpMessage);
    }


	@Override
	@Async
	public void sendMessageDispatchCarComplete(Long orderId, Long userId) {
		Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
		String driverId = orderCommonInfo.get("driverId");
		if (null != driverId) {
			// 给司机发送消息
			sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(), Long.valueOf(driverId),
					MsgConstant.MESSAGE_T007.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
					String.format(MessageTemplateConstant.DISPATCH_CAR_COMPLETE_DRIVER, orderCommonInfo.get("orderNum"),
							orderCommonInfo.get("useCarTime"), orderCommonInfo.get("riderName"),
							orderCommonInfo.get("riderMobile")));
		}

		String applyUserId = orderCommonInfo.get("applyUserId");
		if (null != applyUserId) {
			// 给申请人发消息
			sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(), Long.valueOf(applyUserId),
					MsgConstant.MESSAGE_T003.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
					String.format(MessageTemplateConstant.DISPATCH_CAR_COMPLETE_APPLY,
							orderCommonInfo.get("useCarTime"), orderCommonInfo.get("driverName"),
							orderCommonInfo.get("driverMobile"), orderCommonInfo.get("carType"),
							orderCommonInfo.get("carLicense")));
		}

	}
}
