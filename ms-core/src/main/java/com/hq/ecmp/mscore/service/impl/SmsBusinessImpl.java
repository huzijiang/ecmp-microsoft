package com.hq.ecmp.mscore.service.impl;

import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.constant.SmsTemplateConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
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
     * 取消订单发送短信
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
        Long journeyId = orderInfo.getJourneyId();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(journeyId);
        Long applyUserId = journeyInfo.getUserId();
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(applyUserId);
        applyMobile = ecmpUser.getPhonenumber();
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setItIsPeer("1");
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
        if(journeyPassengerInfos.size()>0){
            riderMobile = journeyPassengerInfos.get(0).getMobile();
            riderName = journeyPassengerInfos.get(0).getName();
        }
        if(!"".equals(riderMobile) && !"".equals(applyMobile)){
            mapMobiles.put("riderMobile", riderMobile);
            mapMobiles.put("applyMobile", applyMobile);
            mapMobiles.put("riderName", riderName);
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
        //乘车人手机号
        String riderMobile = null;
        //乘车人姓名
        String riderName = null;
        //申请人手机号
        String applyMobile = null;
        //车型
        String carType = null;
        //车牌号
        String carLicense = null;
        //订单号
        String orderNum = null;

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        driverName = orderInfo.getDriverName();
        driverMobile = orderInfo.getDriverMobile();
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
        result.put("riderMobile",riderMobile);
        result.put("riderName",riderName);
        result.put("applyMobile",applyMobile);
        result.put("carType",carType);
        result.put("carLicense",carLicense);
        result.put("orderNum",orderNum);
        return result;
    }
}
