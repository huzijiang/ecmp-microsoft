package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysUser;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;
import com.hq.ecmp.mscore.dto.DismissedOutDispatchDTO;
import com.hq.ecmp.mscore.dto.EcmpMessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IsmsBusiness;
import com.hq.ecmp.mscore.vo.DriverSmsInfo;
import com.hq.ecmp.mscore.vo.UserVO;
import com.hq.ecmp.util.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hq.ecmp.constant.CommonConstant.ONE;

/**
 * @ClassName SmsBusinessImpl
 * @Description TODO
 * @Author yj
 * @Date 2020/3/26 21:19
 * @Version 1.0
 */
@Service
@Slf4j
public class SmsBusinessImpl implements IsmsBusiness {

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
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private EcmpMessageMapper ecmpMessageMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Resource
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper dispatcheDetailInfoMapper;


    /**
     * 网约车约车失败短信发送
     *
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsCallTaxiNetFail(Long orderId) {
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
            Map<String, String> paramsMapRider = new HashMap<>();
            paramsMapRider.put("useCarTime", useCarTime);
            paramsMapRider.put("planBeginAddress", planBeginAddress);
            paramsMapRider.put("planEndAddress", planEndAddress);
            if (riderAndApplyMatch(applyAndRiderMobile)) {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_RIDER, paramsMapRider, riderMobile);
            } else {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_RIDER, paramsMapRider, riderMobile);
                Map<String, String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_FAIL_APPLICANT, paramsMapApplicant, applyMobile);
            }

        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},约车超时", orderId);
    }


    /**
     * 网约车，约车成功短信通知
     */
    @Async
    @Override
    public void sendSmsCallTaxiNet(Long orderId) {
        log.info("短信开始-订单{},约车成功", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            //司机姓名
            String driverName = orderCommonInfo.get("driverName");
            //司机手机号
            String driverMobile = orderCommonInfo.get("driverMobile");
            //乘车人
            String riderMobile = orderCommonInfo.get("riderMobile");
            //乘车人姓名
            String riderName = orderCommonInfo.get("riderName");
            //申请人
            String applyMobile = orderCommonInfo.get("applyMobile");
            //用车时间
            String useCarTime = orderCommonInfo.get("useCarTime");
            //车型
            String carType = orderCommonInfo.get("carType");
            //车牌号
            String carLicense = orderCommonInfo.get("carLicense");
            //上车地址
            String planBeginAddress = orderCommonInfo.get("planBeginAddress");
            //下车地址
            String planEndAddress = orderCommonInfo.get("planEndAddress");
            //订单号
            String orderNum = orderCommonInfo.get("orderNum");
            String driverId = orderCommonInfo.get("driverId");
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("useCarTime", useCarTime);
            paramsMap.put("driverName", driverName);
            paramsMap.put("carType", carType);
            paramsMap.put("carLicense", carLicense);

            //乘车人和申请人不是一个人
            if (!riderAndApplyMatch(orderCommonInfo)) {
                //申请人短信
                Map<String, String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_APPLICANT, paramsMapApplicant, applyMobile);
                //乘车人短信
                if (isEnterpriseWorker(riderMobile)) {//企业员工
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_ENTER, paramsMap, riderMobile);
                } else {//非企业员
                    Map<String, String> paramsMapRiderNoE = new HashMap<>();
                    paramsMapRiderNoE.put("applyMobile", applyMobile);
                    paramsMapRiderNoE.put("useCarTime", useCarTime);
                    paramsMapRiderNoE.put("planBeginAddress", planBeginAddress);
                    paramsMapRiderNoE.put("planEndAddress", planEndAddress);
                    paramsMapRiderNoE.put("driverName", driverName);
                    paramsMapRiderNoE.put("carType", carType);
                    paramsMapRiderNoE.put("carLicense", carLicense);
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_NO_ENTER, paramsMapRiderNoE, riderMobile);
                }
            } else {//乘车人和申请人是一个人
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_RIDER_ENTER, paramsMap, riderMobile);
            }

            //司机手机号不为空  则给司机发送短信
            if (StringUtil.isNotEmpty(driverMobile) && StringUtil.isNotEmpty(driverId)) {
                Map<String, String> driverMap = new HashMap<>();
                driverMap.put("orderNum", orderNum);
                driverMap.put("useCarTime", useCarTime);
                driverMap.put("riderName", riderName);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.NETCAR_SUCC_DRIVER, driverMap, driverMobile);
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},约车成功", orderId);
    }


    /**
     * 取消订单无取消费发送短信
     *
     * @param orderId
     * @throws Exception
     */
    @Async
    @Override
    public void sendSmsCancelOrder(Long orderId) {
        log.info("短信开始-订单{},取消", orderId);
        try {
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            String planBeginAddress = orderCommonInfo.get("planBeginAddress");
            String planEndAddress = orderCommonInfo.get("planEndAddress");
            String useCarTime = orderCommonInfo.get("useCarTime");
            //给司机发送短信
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            String driverMobile = orderInfo.getDriverMobile();
            if (driverMobile != null) {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("planBeginAddress", planBeginAddress);
                paramsMap.put("planEndAddress", planEndAddress);
                paramsMap.put("useCarTime", useCarTime);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_DRIVER, paramsMap, driverMobile);
            }
            //给申请人发送短信
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                String applyMobile = applyAndRiderMobile.get("applyMobile");
                Map<String, String> paramsMapApplicant = new HashMap<>();
                paramsMapApplicant.put("useCarTime", useCarTime);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_APPLICANT, paramsMapApplicant, applyMobile);
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},取消", orderId);
    }

    /**
     * 取消订单有取消费发送短信
     *
     * @param orderId
     */
    @Async
    @Override
    public void sendSmsCancelOrderHaveFee(Long orderId, Double comAmount) {
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
            if (isEnterpriseWorker(riderMobile)) {//企业员工
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("useCarTime", useCarTime);
                paramsMap.put("comAmount", String.valueOf(comAmount));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_HAVEFEE_RIDER_ENTER, paramsMap, riderMobile);
            }
            //申请人发
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                paramsMap.put("useCarTime", useCarTime);
                paramsMap.put("comAmount", String.valueOf(comAmount));
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.CANCEL_ORDER_HAVEFEE_APPLICANT, paramsMap, applyMobile);
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},取消-收费", orderId);

    }

    /**
     * 自有车司机到达发送短信
     * @param orderId
     *//*
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
            log.error("业务处理异常", e)();
        }
        log.info("短信结束-订单{},自有车司机到达", orderId);
    }*/


    /**
     * 自有车司机到达发送短信
     * @param orderId
     *//*
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
            log.error("业务处理异常", e)();
        }
        log.info("短信结束-订单{},自有车司机到达", orderId);
    }*/

    /**
     * 自有车司机到达发送短信
     *
     * @param orderId
     */
    @Override
    @Async
    public void sendSmsDriverArrivePrivate(Long orderId) {
        log.info("短信开始-订单{},司机开始服务", orderId);
        try {
            DriverSmsInfo orderCommonInfo = getOrderinfo(orderId);
            //用车人
            String applyMobile = orderCommonInfo.getApplyMobile();
            Map<String, String> orderCommonInfoMap = objToMap(orderCommonInfo);
            //乘车人
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_READY_APPLICANT, orderCommonInfoMap, applyMobile);

        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机开始服务", orderId);
    }

    /**
     * 司机开始服务发送短信
     *
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
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_BEGINSERVICE_APPLICANT, paramsMap, applyMobile);
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机开始服务", orderId);
    }


    /**
     * 司机服务结束发送短信
     *
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
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETESERVICE_APPLICANT, paramsMap, applyMobile);
            }
            ecmpMessageMapper.updateByCategoryId(orderId, MsgStatusConstant.MESSAGE_STATUS_T001.getType(), null);
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机服务结束", orderId);
    }

    /**
     * 申请人和乘车人是否是同一人的验证
     *
     * @param mapMobiles
     * @return
     */
    public boolean riderAndApplyMatch(Map<String, String> mapMobiles) throws Exception {
        if (mapMobiles == null || "".equals(mapMobiles.get("riderMobile")) || "".equals(mapMobiles.get("applyMobile")))
            throw new Exception("乘车人或申请人信息异常");
        return mapMobiles.get("riderMobile").equals(mapMobiles.get("applyMobile"));

    }

    /**
     * 获取申请人和乘车人电话
     *
     * @return
     */
    public Map<String, String> getApplyAndRiderMobile(OrderInfo orderInfo) {
        Map<String, String> mapMobiles = new HashMap<>();
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
        if (journeyPassengerInfos.size() > 0) {
            riderMobile = journeyPassengerInfos.get(0).getMobile();
            riderName = journeyPassengerInfos.get(0).getName();
            EcmpUser ecmpUser1 = new EcmpUser();
            ecmpUser1.setPhonenumber(riderMobile);
            List<EcmpUser> ecmpUsers = ecmpUserMapper.selectEcmpUserList(ecmpUser1);
            if (ecmpUsers.size() > 0) {
                EcmpUser ecmpUserCh = ecmpUsers.get(0);
                riderId = ecmpUserCh.getUserId() + "";
            }

        }
        if (!"".equals(riderMobile) && !"".equals(applyMobile)) {
            mapMobiles.put("riderMobile", riderMobile);
            mapMobiles.put("applyMobile", applyMobile);
            mapMobiles.put("riderName", riderName);
            mapMobiles.put("applyUserId", String.valueOf(applyUserId));
            mapMobiles.put("riderId", riderId);
            return mapMobiles;
        }
        return null;
    }

    /**
     * 通过手机号判断是否是企业员
     *
     * @return
     */
    public boolean isEnterpriseWorker(String riderMobile) {
        EcmpUser ecmpUser = new EcmpUser();
        ecmpUser.setPhonenumber(riderMobile);
        List<EcmpUser> ecmpUsers = ecmpUserMapper.selectEcmpUserList(ecmpUser);
        if (ecmpUser != null && ecmpUsers.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取订单公共信息
     */
    public Map<String, String> getOrderCommonInfo(Long orderId) {
        Map<String, String> result = new HashMap<>();
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
        String driverId = null;
        //乘车人手机号
        String riderMobile = null;
        //乘车人姓名
        String riderName = null;
        //申请人手机号
        String applyMobile = null;
        //申请人编号
        String applyUserId = null;
        //车型
        String carType = null;
        //车牌号
        String carLicense = null;
        //订单号
        String orderNum = null;
        //调度员id
        String dispatchId = null;
        //调度员name
        String dispatchName = null;
        //调度员电话
        String dispatchMobile = null;

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        driverName = orderInfo.getDriverName();
        driverMobile = orderInfo.getDriverMobile();
        driverId = (null == orderInfo.getDriverId() ? null : orderInfo.getDriverId().toString());
        carLicense = orderInfo.getCarLicense();
        orderNum = orderInfo.getOrderNumber();
        if (orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)) {
            Long carId = orderInfo.getCarId();
            if (carId != null) {
                CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
                carType = carInfo.getCarType();
                carLicense = carInfo.getCarLicense();
            }
        } else {
            carType = orderInfo.getCarModel();
        }
        Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
        riderMobile = applyAndRiderMobile.get("riderMobile");
        applyMobile = applyAndRiderMobile.get("applyMobile");
        applyUserId = applyAndRiderMobile.get("applyUserId");
        riderName = applyAndRiderMobile.get("riderName");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        if (orderAddressInfos != null && orderAddressInfos.size() > 0) {
            for (OrderAddressInfo orderAddressInfo1 :
                    orderAddressInfos) {
                if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)) {
                    planBeginAddress = orderAddressInfo1.getAddress();
                    useCarTime = simpleDateFormat.format(orderAddressInfo1.getActionTime());
                } else if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)) {
                    planEndAddress = orderAddressInfo1.getAddress();
                } else {

                }
            }
        }
        //已派车状态,调度员信息
        UserVO sysUser = orderStateTraceInfoMapper.getOrderDispatcher(orderId, OrderState.ALREADYSENDING.getState());
        if (sysUser != null) {

            dispatchId = sysUser.getUserId().toString();
            dispatchMobile = sysUser.getUserPhone();
            dispatchName = sysUser.getUserName();
        }
        result.put("useCarTime", useCarTime);
        result.put("planBeginAddress", planBeginAddress);
        result.put("planEndAddress", planEndAddress);
        result.put("driverName", driverName);
        result.put("driverMobile", driverMobile);
        result.put("driverId", driverId);
        result.put("riderMobile", riderMobile);
        result.put("riderName", riderName);
        result.put("applyMobile", applyMobile);
        result.put("applyUserId", applyUserId);
        result.put("carType", carType);
        result.put("carLicense", carLicense);
        result.put("orderNum", orderNum);
        result.put("dispatchId", dispatchId);
        result.put("dispatchMobile", dispatchMobile);
        result.put("dispatchName", dispatchName);
        return result;
    }

    /**
     * 消息通知-订单取消-给司机发通知（自有车）
     *
     * @param orderId
     * @param createId
     */
    @Async
    @Override
    public void sendMessageCancelOrder(Long orderId, Long createId) {
        updateOldStateMessage(null, orderId, null, null);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        Long applyId = getApplyIdByOrderId(orderId, orderInfo.getJourneyId());
        sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(), orderInfo.getDriverId(),
                MsgConstant.MESSAGE_T005.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                orderId, createId, MsgConstant.MESSAGE_T005.getDesc(), applyId);
    }

    /**
     * 服务开始消息通知
     *
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
            Long applyId = getApplyIdByOrderId(orderId, orderInfo.getJourneyId());
            //申请人
            sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(), Long.parseLong(applyUserId),
                    MsgConstant.MESSAGE_T006.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                    orderId, createId, MsgConstant.MESSAGE_T006.getDesc(), applyId);
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                //乘车人
                if (!"".equals(riderId)) {
                    sendMessage(MsgUserConstant.MESSAGE_USER_USER.getType(), Long.parseLong(riderId),
                            MsgConstant.MESSAGE_T006.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                            orderId, createId, MsgConstant.MESSAGE_T006.getDesc(), applyId);
                }
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
    }

    /**
     * 服务结束未确认行程
     *
     * @param orderId
     */
    @Override
    @Async
    public void endServiceNotConfirm(Long orderId) {
        log.info("短信开始-订单{},司机服务结束乘客未确认行程", orderId);
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            //乘车人
            String riderMobile = applyAndRiderMobile.get("riderMobile");
            String applyMobile = applyAndRiderMobile.get("applyMobile");
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("riderMobile", riderMobile);
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETE_NOT_CONFIRM, paramsMap, applyMobile);
            } else {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.DRIVER_COMPLETE_NOT_CONFIRM_RIDER, null, applyMobile);
            }
            ecmpMessageMapper.updateByCategoryId(orderId, MsgStatusConstant.MESSAGE_STATUS_T001.getType(), null);
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机服务结束", orderId);
    }

    /**
     * 司机已到达短信(网约车)
     *
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
            Map<String, String> paramsMap = Maps.newHashMap();
            paramsMap.put("driverName", orderCommonInfo.get("driverName"));
            paramsMap.put("time", orderCommonInfo.get("useCarTime").substring(11));
            paramsMap.put("carLicense", orderCommonInfo.get("carLicense"));
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_ENTER, paramsMap, applyAndRiderMobile.get("riderMobile"));
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                //乘车人非企业员
                if (StringUtils.isEmpty(riderId)) {
                    paramsMap.put("applyMobile", applyAndRiderMobile.get("applyMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER, paramsMap, applyAndRiderMobile.get("riderMobile"));
                } else {//申请人
                    paramsMap.put("riderMobile", applyAndRiderMobile.get("riderMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_APPLY, paramsMap, applyAndRiderMobile.get("applyMobile"));
                }
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
    }

    /**
     * 开始服务发送短信网约车
     *
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void startService(Long orderId, Long createId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            String riderId = applyAndRiderMobile.get("riderId");
            //乘车人企业员工

            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                //乘车人非企业员
                Map<String, String> paramsMap = Maps.newHashMap();
                paramsMap.put("driverName", orderCommonInfo.get("driverName"));
                paramsMap.put("time", orderCommonInfo.get("useCarTime").substring(11));
                paramsMap.put("carLicense", orderCommonInfo.get("carLicense"));
                if (StringUtils.isEmpty(riderId)) {
                    paramsMap.put("applyMobile", applyAndRiderMobile.get("applyMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER, paramsMap, applyAndRiderMobile.get("riderMobile"));
                } else {//申请人
                    paramsMap.put("riderMobile", applyAndRiderMobile.get("riderMobile"));
                    iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TAXI_DRIVER_ARR_RIDER_NO_ENTER, paramsMap, applyAndRiderMobile.get("applyMobile"));
                }
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
    }

    /**
     * 改派成功消息通知
     *
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void sendMessageReassignSucc(Long orderId, Long createId) {
        try {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            Long applyId = getApplyIdByOrderId(orderId, orderInfo.getJourneyId());
            Map<String, String> applyAndRiderMobile = getApplyAndRiderMobile(orderInfo);
            Long driverId = orderInfo.getDriverId();
            String applyUserId = applyAndRiderMobile.get("applyUserId");
            String riderId = applyAndRiderMobile.get("riderId");
            //司机
            if (driverId != null) {
                sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(), driverId,
                        MsgConstant.MESSAGE_T007.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                        orderId, createId, MsgConstant.MESSAGE_T007.getDesc(), applyId);
            }
            //申请人
            sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(), Long.parseLong(applyUserId),
                    MsgConstant.MESSAGE_T015.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                    orderId, createId, MsgConstant.MESSAGE_T015.getDesc(), applyId);
            if (!riderAndApplyMatch(applyAndRiderMobile)) {
                //乘车人
                if (!"".equals(riderId)) {
                    sendMessage(MsgUserConstant.MESSAGE_USER_USER.getType(), Long.parseLong(riderId),
                            MsgConstant.MESSAGE_T015.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                            orderId, createId, MsgConstant.MESSAGE_T015.getDesc(), applyId);
                }
            }
            //查询该订单的发起改派申请的司机
            Long applyReassignmentDriverId = orderStateTraceInfoMapper.queryApplyReassignmentDriver(orderId, OrderStateTrace.APPLYREASSIGNMENT.getState());
            if (null != applyReassignmentDriverId) {
                //给发起改派申请的司机发送消息通知  改派成功了
                sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(), applyReassignmentDriverId,
                        MsgConstant.MESSAGE_T011.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                        orderId, createId, MsgConstant.MESSAGE_T011.getDesc(), applyId);
            }
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
    }

    /**
     * 差旅下单成功，发送消息通知-调度员
     *
     * @param orderId
     * @param createId
     */
    @Override
    @Async
    public void sendMessagePriTravelOrderSucc(Long orderId, Long createId) {
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        Long applyId = getApplyIdByOrderId(orderId, orderInfo.getJourneyId());
        if (orderAddressInfos.size() > 0) {
            for (OrderAddressInfo orderAddressInfoCh1 :
                    orderAddressInfos) {
                if (orderAddressInfoCh1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)) {
                    String cityPostalCode = orderAddressInfoCh1.getCityPostalCode();
                    CarGroupInfo carGroupInfo = new CarGroupInfo();
                    carGroupInfo.setCity(cityPostalCode);
                    List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
                    CarGroupInfo carGroupInfoCh = carGroupInfos.get(0);
                    CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                    carGroupDispatcherInfo.setCarGroupId(carGroupInfoCh.getCarGroupId());
                    List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                    for (CarGroupDispatcherInfo carGroupDispatcherInfo1 :
                            carGroupDispatcherInfos) {
                        sendMessage(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType(), carGroupDispatcherInfo1.getUserId(),
                                MsgConstant.MESSAGE_T003.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                                orderId, createId, MsgConstant.MESSAGE_T003.getDesc(), applyId);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 基础方法
     *
     * @param role
     * @param personId
     * @param type
     * @param businessType
     * @param businessId
     * @param createId
     * @param msg
     */
    private void sendMessage(int role, Long personId, String type, String businessType, Long businessId, Long createId, String msg, Long applyId) {
        EcmpMessage ecmpMessage = new EcmpMessage();
        ecmpMessage.setConfigType(role);
        ecmpMessage.setEcmpId(personId);
        ecmpMessage.setType(businessType);
        ecmpMessage.setStatus(MsgStatusConstant.MESSAGE_STATUS_T002.getType());
        ecmpMessage.setContent(msg);
        ecmpMessage.setCategory(type);
        ecmpMessage.setCategoryId(businessId);
        ecmpMessage.setApplyId(applyId);
        ecmpMessage.setUrl("");
        ecmpMessage.setCreateBy(createId);
        ecmpMessage.setCreateTime(DateUtils.getNowDate());
        ecmpMessage.setUpdateBy(null);
        ecmpMessage.setUpdateTime(null);
        ecmpMessageMapper.insert(ecmpMessage);
    }


    /**
     * 自有车派车通知
     *
     * @param orderId
     * @param userId
     */
    @Override
    @Async
    public void sendMessageDispatchCarComplete(Long orderId, Long userId) {
        Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
        String driverId = orderCommonInfo.get("driverId");
        Long applyId = getApplyIdByOrderId(orderId, null);
        if (null != driverId) {
            // 给司机发送消息
            sendMessage(MsgUserConstant.MESSAGE_USER_DRIVER.getType(), Long.valueOf(driverId),
                    MsgConstant.MESSAGE_T007.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
                    MsgConstant.MESSAGE_T007.getDesc(), applyId);
        }

        String applyUserId = orderCommonInfo.get("applyUserId");
        if (null != applyUserId) {
            // 给申请人发消息
            sendMessage(MsgUserConstant.MESSAGE_USER_APPLICANT.getType(), Long.valueOf(applyUserId),
                    MsgConstant.MESSAGE_T013.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
                    MsgConstant.MESSAGE_T013.getDesc(), applyId);
        }

    }

    /**
     * 更换车辆通知
     */
    @Override
    @Async
    public void sendMessageReplaceCarComplete(Long orderId, Long userId) {
        Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
        String applyUserId = orderCommonInfo.get("applyUserId");
        Long applyId = getApplyIdByOrderId(orderId, null);
        if (null != applyUserId) {
            // 给申请人发消息
            sendMessage(MsgUserConstant.MESSAGE_USER_USER.getType(), Long.valueOf(applyUserId),
                    MsgConstant.MESSAGE_T014.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
                    MsgConstant.MESSAGE_T014.getDesc(), applyId);
        }

        String dispatchId = orderCommonInfo.get("dispatchId");
        String driverName = orderCommonInfo.get("driverName");
        /*//司机姓名
        String driverName = null;
        //司机手机号
        String driverMobile = null;
        //司机编号
        String driverId=null;*/
        if (null != dispatchId) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            // 给调度员发消息
            sendMessage(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType(), Long.valueOf(dispatchId),
                    MsgConstant.MESSAGE_T014.getType(), MsgTypeConstant.MESSAGE_TYPE_T001.getType(), orderId, userId,
                    MsgConstant.MESSAGE_T014.getType(), applyId);
        }
    }

    @Override
    @Transactional
    public void sendSmsReplaceCar(Long orderId) throws Exception {
        log.info("短信开始-订单{},换车成功", orderId);
        Map<String, String> orderCommonInfo = getOrderCommonInfo(orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        CarInfo carInfo = carInfoMapper.selectCarInfoById(orderInfo.getCarId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String driverMobile = orderCommonInfo.get("driverMobile");
        String applyMobile = orderCommonInfo.get("applyMobile");
        String riderMobile = orderCommonInfo.get("riderMobile");
        if (isEnterpriseWorker(riderMobile)) {//企业员工
            Map mapEnterprice = new HashMap();
            mapEnterprice.put("date", df.format(orderInfo.getCreateTime()));
            mapEnterprice.put("driverName", orderCommonInfo.get("driverName"));
            mapEnterprice.put("carType", carInfo.getCarType());
            mapEnterprice.put("carLicense", carInfo.getCarLicense());
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.REPLACE_CAR_RIDER_ENTERPRICE_NOTICE, mapEnterprice, riderMobile);
        } else {
            Map mapNoEnterprice = new HashMap();
            mapNoEnterprice.put("date", df.format(orderInfo.getCreateTime()));
            mapNoEnterprice.put("driverName", orderCommonInfo.get("driverName"));
            mapNoEnterprice.put("applyName", orderCommonInfo.get("applyMobile"));
            mapNoEnterprice.put("carType", carInfo.getCarType());
            mapNoEnterprice.put("carLicense", carInfo.getCarLicense());
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.REPLACE_CAR_RIDER_NO_ENTERPRICE_NOTICE, mapNoEnterprice, riderMobile);
        }
        Map mapDispatch = new HashMap();
        mapDispatch.put("date", df.format(orderInfo.getCreateTime()));
        mapDispatch.put("driverName", orderCommonInfo.get("driverName"));
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.REPLACE_CAR_DISPATCH_NOTICE, mapDispatch, driverMobile);
        Map mapApply = new HashMap();
        mapApply.put("date", df.format(orderInfo.getCreateTime()));
        mapApply.put("driverName", orderCommonInfo.get("driverName"));
        mapApply.put("riderName", orderCommonInfo.get("riderName"));
        mapApply.put("carType", carInfo.getCarType());
        mapApply.put("carLicense", carInfo.getCarLicense());
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.REPLACE_CAR_APPLY_NOTICE, mapApply, applyMobile);
        log.info("短信结束-订单{},换车成功", orderId);
    }

    /**
     * 外部调度员驳回给内部调度元发短信
     *
     * @param orderInfo
     * @param rejectReason
     * @throws Exception
     */
    @Override
    @Async
    public void sendSmsDispatchReject(OrderInfo orderInfo, String rejectReason) throws Exception {
        log.info("短信开始-订单{},外部调度员驳回成功", orderInfo.getOrderId());
        String useCarPeople = "";
        String carGroup = "";
        String day = "";
        String startDate = "";
        String content = "";
        String dispatcher = "";
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(orderInfo.getOrderId()));
        if (CollectionUtils.isEmpty(orderDispatcheDetailInfos)) {
            return;
        }
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(orderDispatcheDetailInfo.getNextCarGroupId());
        if (carGroupInfo != null) {
            carGroup = carGroupInfo.getCarGroupName();
        }
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(new JourneyPassengerInfo(orderInfo.getJourneyId()));
        if (CollectionUtils.isEmpty(journeyPassengerInfos)) {
            return;
        }
        JourneyPassengerInfo journeyPassengerInfo = journeyPassengerInfos.get(0);
        useCarPeople = journeyPassengerInfo.getName() + " " + journeyPassengerInfo.getMobile();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        if (journeyInfo == null) {
            return;
        }
        EcmpUser outDispather = ecmpUserMapper.selectEcmpUserById(orderDispatcheDetailInfo.getOuterDispatcher());
        EcmpUser innerDispather = ecmpUserMapper.selectEcmpUserById(orderDispatcheDetailInfo.getInnerDispatcher());
        if (outDispather == null || innerDispather == null) {
            return;
        }
        dispatcher = outDispather.getNickName() + " " + outDispather.getPhonenumber();
        List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(orderInfo.getJourneyId()));
        if (CollectionUtils.isEmpty(applyInfos)) {
            return;
        }
        ApplyInfo applyInfo = applyInfos.get(0);
        content = applyInfo.getNotes();
        day = journeyInfo.getUseTime();
        startDate = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, journeyInfo.getStartDate());
        Map<String, String> map = Maps.newHashMap();
        map.put("carGroup", carGroup);
        map.put("useCarPeople", useCarPeople);
        map.put("day", day);
        map.put("startDate", startDate);
        map.put("content", content);
        map.put("rejectReason", rejectReason);
        map.put("dispatcher", dispatcher);
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.OUT_DISPATCH_REJECT_USECARPEOPLE, map, innerDispather.getPhonenumber());
        log.info("短信结束-订单{},外部调度员驳回成功", orderInfo.getOrderId(), JSON.toJSON(map));
    }

    /***
     * 内部调度员驳回给用车人/业务员发短信
     * @param orderInfo
     * @param rejectReason
     * @throws Exception
     */
    @Override
    public void sendSmsInnerDispatcherReject(OrderInfo orderInfo, String rejectReason, LoginUser loginUser) throws Exception {
        log.info("短信开始-订单{},外部调度员驳回成功", orderInfo.getOrderId());
        String useCarPeople = "";
        String day = "";
        String content = "";
        String dispatcher = "";//调度员
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(orderInfo.getOrderId()));
        if (CollectionUtils.isEmpty(orderDispatcheDetailInfos)) {
            return;
        }
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(new JourneyPassengerInfo(orderInfo.getJourneyId()));
        if (CollectionUtils.isEmpty(journeyPassengerInfos)) {
            return;
        }
        JourneyPassengerInfo journeyPassengerInfo = journeyPassengerInfos.get(0);
        useCarPeople = journeyPassengerInfo.getName() + " " + journeyPassengerInfo.getMobile();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        if (journeyInfo == null) {
            return;
        }
        day = journeyInfo.getUseTime();
        dispatcher = loginUser.getUser().getNickName() + " " + loginUser.getUser().getPhonenumber();
        List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(orderInfo.getJourneyId()));
        if (CollectionUtils.isEmpty(applyInfos)) {
            return;
        }
        ApplyInfo applyInfo = applyInfos.get(0);
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
        if (ecmpUser == null) {
            return;
        }
        /**获取车队电话**/
        String carGroupPhone = carGroupDispatcherInfoMapper.selectCarGroupPhoneByUserId(loginUser.getUser().getUserId());
        Map<String, String> map = Maps.newHashMap();
        map.put("orderNumber", orderInfo.getOrderNumber());
        map.put("useCarPeaple", useCarPeople);
        map.put("day", day);
        map.put("startDate", DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT, journeyInfo.getStartDate()));
        map.put("content", content);
        map.put("rejectReason", rejectReason);
        map.put("carGroupPhone", carGroupPhone);
        map.put("dispatcher", dispatcher);
        /**给业务员发短信*/
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.INNER_DISPATCH_REJECT_SALESMAN, map, ecmpUser.getPhonenumber());
        /**用车人、申请人电话相同只发一次**/
        if (!ecmpUser.getPhonenumber().equals(journeyPassengerInfo.getMobile())) {
            /**给用车人发短信*/
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.INNER_DISPATCH_REJECT_SALESMAN, map, journeyPassengerInfo.getMobile());
        }
        log.info("驳回申请发送短信内容={}", JSON.toJSONString(map));
        log.info("驳回短信结束-订单{},内部调度员驳回成功", orderInfo.getOrderId(), JSON.toJSON(map));
    }

    /**
     * 业务员提交申请单 -----用车人
     *
     * @param journeyId
     * @param applyId
     * @param officialCommitApply
     */
    @Override
    @Async
    public void sendVehicleUserApply(Long journeyId, Long applyId, ApplyOfficialRequest officialCommitApply) throws Exception {
        log.info("短信开始-业务员提交申请单{},成功", applyId);
        UndoSMSTemplate undoSMSTemplate = applyInfoMapper.queryApplyUndoList(applyId);
        String drivingTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, undoSMSTemplate.getStartDate());
        String vehicleUser = undoSMSTemplate.getNickName() + " " + undoSMSTemplate.getPhonenumber();
        Map<String, String> map = Maps.newHashMap();
        map.put("drivingTime", drivingTime); // 用车时间
        map.put("vehicleUser", vehicleUser);//业务员信息
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_VEHICLE_APPLICANT, map, undoSMSTemplate.getVehicleUserMobile());
        /**预约用车给申请人也发短信**/
        if (!undoSMSTemplate.getPhonenumber().equals(undoSMSTemplate.getVehicleUserMobile())) {
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_VEHICLE_APPLICANT, map, undoSMSTemplate.getPhonenumber());
            log.info("预约用车发送短信申请人手机号={},用车人手机号={}", undoSMSTemplate.getPhonenumber(), undoSMSTemplate.getVehicleUserMobile());
        }
        log.info("业务员提交申请单短信结束", JSON.toJSON(map));
    }

    /**
     * 业务员提交申请单 -----调度员
     *
     * @param journeyId
     * @param applyId
     * @param officialCommitApply
     */
    @Override
    @Async
    public void sendDispatcherApply(Long journeyId, Long applyId, ApplyOfficialRequest officialCommitApply) throws Exception {
        log.info("短信开始-业务员提交申请单给调度员发短信{},开始", applyId);
        String subscribeTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, officialCommitApply.getApplyDate());
        String vehicleUser = officialCommitApply.getPassenger().getUserName() + " " + officialCommitApply.getPassenger().getUserPhone();
        String salesman = officialCommitApply.getApplyUser().getUserName() + " " + officialCommitApply.getApplyUser().getUserPhone();
        String applyDays = officialCommitApply.getApplyDays();
        UndoSMSTemplate undoSMSTemplate = applyInfoMapper.queryApplyUndoList(applyId);
        String deptName = undoSMSTemplate.getDeptName();
        String orderNumber = undoSMSTemplate.getOrderNumber();
        String carTypeName = undoSMSTemplate.getCarTypeName();
        Map<String, String> map = Maps.newHashMap();
        map.put("deptName", deptName);//申请人单位名称
        map.put("orderNumber", orderNumber);//订单编号
        map.put("carTypeName", carTypeName);//服务车型
        map.put("subscribeTime", subscribeTime);//用车时间
        map.put("vehicleUser", vehicleUser);//用车人
        map.put("salesman", salesman); //业务员
        map.put("applyDays", applyDays); //预约天数
        if (StringUtils.isNotBlank(officialCommitApply.getReason())) {
            String reason = officialCommitApply.getReason();
            map.put("reason", "预约备注：" + reason); // 预约备注
        } else {
            map.put("reason", "预约备注：无");
        }
        //发给内部车队所有调度员
        List<EcmpUser> ecmpUser = carGroupDispatcherInfoMapper.getCarGroupDispatcherList(officialCommitApply);
        if (!ecmpUser.isEmpty()) {
            for (EcmpUser user : ecmpUser) {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_INTERNAL_DISPATCHER, map, user.getPhonenumber());
            }
        }
        log.info("业务员提交申请单短信结束 -调度员", JSON.toJSON(map));
    }

    /**
     * 撤销未派单短信
     *
     * @param undoSMSTemplate
     */
    @Override
    public void sendRevokeUndelivered(UndoSMSTemplate undoSMSTemplate) throws Exception {
        log.info("短信开始-撤销未派单短信");
        String subscribeTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, undoSMSTemplate.getStartDate());
        String salesman = undoSMSTemplate.getNickName() + " " + undoSMSTemplate.getPhonenumber(); //申请人
        String vehicleUser = undoSMSTemplate.getVehicleUser() + " " + undoSMSTemplate.getVehicleUserMobile(); //用车人
        String applyDays = undoSMSTemplate.getUseTime();
        String deptName = undoSMSTemplate.getDeptName();
        String orderNumber = undoSMSTemplate.getOrderNumber();
        String carTypeName = undoSMSTemplate.getCarTypeName();
        Map<String, String> map = Maps.newHashMap();
        map.put("subscribeTime", subscribeTime);//用车时间
        map.put("salesman", salesman); //业务员
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_VEHICLE_APPLICANT_NOT, map, undoSMSTemplate.getVehicleUserMobile());
        Map<String, String> mapTwo = Maps.newHashMap();
        mapTwo.put("deptName", deptName);//部门名称
        mapTwo.put("orderNumber", orderNumber);//订单编号
        mapTwo.put("carTypeName", carTypeName);//服务车型
        mapTwo.put("subscribeTime", subscribeTime);//用车时间
        mapTwo.put("salesman", salesman); //业务员
        mapTwo.put("vehicleUser", vehicleUser);//用车人
        mapTwo.put("applyDays", applyDays); //预约天数
        if (StringUtils.isNotBlank(undoSMSTemplate.getNotes())) {
            String reason = undoSMSTemplate.getNotes();
            mapTwo.put("reason", "预约备注：" + reason); // 预约备注
        } else {
            mapTwo.put("reason", "预约备注：无");
        }
        //发给内部车队所有调度员
        ApplyOfficialRequest applyOfficialRequest = new ApplyOfficialRequest();
        applyOfficialRequest.setCompanyId(undoSMSTemplate.getCompanyId());
        List<EcmpUser> ecmpUser = carGroupDispatcherInfoMapper.getCarGroupDispatcherList(applyOfficialRequest);
        if (!ecmpUser.isEmpty()) {
            for (EcmpUser user : ecmpUser) {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_INTERNAL_DISPATCHER_NOT, mapTwo, user.getPhonenumber());
            }
        }
        log.info("短信结束-撤销未派单短信", JSON.toJSON(map));
    }

    /**
     * 撤销已派单短信
     *
     * @param undoSMSTemplate
     */
    @Override
    public void sendRevokealSentList(UndoSMSTemplate undoSMSTemplate) throws Exception {
        log.info("短信开始-撤销已派单短信");
        String subscribeTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, undoSMSTemplate.getStartDate());
        String salesman = undoSMSTemplate.getNickName() + " " + undoSMSTemplate.getPhonenumber();
        String vehicleUser = undoSMSTemplate.getVehicleUser() + " " + undoSMSTemplate.getVehicleUserMobile();
        String applyDays = undoSMSTemplate.getUseTime();
        String orderNumber = undoSMSTemplate.getOrderNumber();
        String carTypeName = undoSMSTemplate.getCarTypeName();
        Map<String, String> map = Maps.newHashMap();
        map.put("subscribeTime", subscribeTime);//用车时间
        map.put("salesman", salesman); //业务员
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_EXTERNAL_VEHICLE_USER_YES, map, undoSMSTemplate.getVehicleUserMobile());
        Map<String, String> mapTwo = Maps.newHashMap();
        mapTwo.put("orderNumber", orderNumber);//订单编号
        mapTwo.put("carTypeName", carTypeName);//服务车型
        mapTwo.put("subscribeTime", subscribeTime);//用车时间
        mapTwo.put("salesman", salesman); //业务员
        mapTwo.put("vehicleUser", vehicleUser);//用车人
        mapTwo.put("applyDays", applyDays); //预约天数
        if (StringUtils.isNotBlank(undoSMSTemplate.getNotes())) {
            String reason = undoSMSTemplate.getNotes();
            mapTwo.put("reason", "预约备注：" + reason); // 预约备注
        } else {
            mapTwo.put("reason", "预约备注：无");
        }
        //发给内部车队所有调度员
        ApplyOfficialRequest applyOfficialRequest = new ApplyOfficialRequest();
        applyOfficialRequest.setCompanyId(undoSMSTemplate.getCompanyId());
        List<EcmpUser> ecmpUser = carGroupDispatcherInfoMapper.getCarGroupDispatcherExternalList(applyOfficialRequest);
        if (!ecmpUser.isEmpty()) {
            for (EcmpUser user : ecmpUser) {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_EXTERNAL_DISPATCHER_YES, mapTwo, user.getPhonenumber());
            }
        }
        log.info("短信结束-撤销已派单短信", JSON.toJSON(map));
    }

    /**
     * 撤销待服务短信
     *
     * @param undoSMSTemplate
     */
    @Override
    public void sendRevokeToBeServed(UndoSMSTemplate undoSMSTemplate) throws Exception {
        log.info("短信开始-撤销待服务短信");
        log.info("短信开始-撤销待服务短信:驾驶员短信开始");
        String subscribeTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, undoSMSTemplate.getStartDate());
        String orderNumber = undoSMSTemplate.getOrderNumber();
        String driverMobile = undoSMSTemplate.getDriverMobile();
        String innerPhonenumber = undoSMSTemplate.getInnerPhonenumber();
        String outerPhonenumber = undoSMSTemplate.getOuterPhonenumber();
        String salesman = undoSMSTemplate.getNickName() + " " + undoSMSTemplate.getPhonenumber();
        String vehicleUser = undoSMSTemplate.getVehicleUser() + " " + undoSMSTemplate.getVehicleUserMobile();
        String applyDays = undoSMSTemplate.getUseTime();
        String deptName = undoSMSTemplate.getDeptName();
        Map<String, String> map = Maps.newHashMap();
        map.put("subscribeTime", subscribeTime);//用车时间
        map.put("vehicleUser", vehicleUser); //业务员
        map.put("orderNumber", orderNumber); //订单号
        if (StringUtils.isNotBlank(undoSMSTemplate.getNotes())) {
            String reason = undoSMSTemplate.getNotes();
            map.put("reason", "预约备注：" + reason); //用车备注
        } else {
            map.put("reason", "预约备注：无");
        }
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_REVOKE_DRIVER, map, driverMobile);
        log.info("短信开始-撤销待服务短信:驾驶员短信结束", JSON.toJSON(map));
        log.info("短信开始-撤销待服务短信:用车人短信开始");
        Map<String, String> mapTwo = Maps.newHashMap();

        mapTwo.put("subscribeTime", subscribeTime);//用车时间
        mapTwo.put("salesman", salesman); //业务员
        mapTwo.put("orderNumber", orderNumber);//订单编号
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_REVOKE_VEHICLE_USER, mapTwo, undoSMSTemplate.getVehicleUserMobile());
        log.info("短信开始-撤销待服务短信:用车人短信结束", JSON.toJSON(mapTwo));
        log.info("短信开始-撤销待服务短信:调度员短信开始");
        Map<String, String> mapThree = Maps.newHashMap();
        mapThree.put("subscribeTime", subscribeTime);//用车时间
        mapThree.put("salesman", salesman); //业务员
        mapThree.put("vehicleUser", vehicleUser);//用车人
        mapThree.put("applyDays", applyDays); //预约天数
        mapThree.put("orderNumber", orderNumber); //订单号
        mapThree.put("deptName", deptName); //用车单位
        if (StringUtils.isNotBlank(undoSMSTemplate.getNotes())) {
            String reason = undoSMSTemplate.getNotes();
            mapThree.put("reason", "预约备注：" + reason); //预约备注
        } else {
            mapThree.put("reason", "预约备注：无");
        }
        if (StringUtils.isNotBlank(innerPhonenumber)) {
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_REVOKE_DISPATCHER, mapThree, innerPhonenumber);
        }
        if (StringUtils.isNotBlank(outerPhonenumber)) {
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_REVOKE_DISPATCHER, mapThree, outerPhonenumber);
        }
        log.info("短信开始-撤销待服务短信:调度员短信结束");
        log.info("短信结束-撤销待服务短信");
    }

    /**
     * 修改申请单发送短信  用车人撤销  用车人申请  调度员
     *
     * @param undoSMSTemplate
     */
    @Override
    @Async
    public void sendUpdateApplyInfoSms(UndoSMSTemplate undoSMSTemplate) throws Exception {
        log.info("短信开始-撤销用车人短信:撤销用车人短信开始");
        String subscribeTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, undoSMSTemplate.getStartDate());
        String orderNumber = undoSMSTemplate.getOrderNumber();
        String salesman = undoSMSTemplate.getNickName() + " " + undoSMSTemplate.getPhonenumber();//申请人
        String vehicleUser = undoSMSTemplate.getVehicleUser() + " " + undoSMSTemplate.getVehicleUserMobile();//用车人
        String applyDays = undoSMSTemplate.getUseTime();
        String carTypeName = undoSMSTemplate.getCarTypeName();
        String deptName = undoSMSTemplate.getDeptName();
        String telephone = undoSMSTemplate.getTelephone();
        Map<String, String> map = Maps.newHashMap();
        map.put("subscribeTime", subscribeTime);//用车时间
        map.put("salesman", salesman); //申请人
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_VEHICLE_APPLICANT_NOT, map, undoSMSTemplate.getVehicleUserMobile());
        log.info("短信结束-撤销用车人短信:撤销用车人短信结束");
        //--------------------------------------------------------
        log.info("短信开始-用车人申请短信:用车人申请短信开始");
        Map<String, String> mapTwo = Maps.newHashMap();
        mapTwo.put("drivingTime", subscribeTime);//用车时间
        mapTwo.put("vehicleUser", salesman); //业务员
        mapTwo.put("telephone", telephone); //内部车队座机
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_VEHICLE_APPLICANT, mapTwo, undoSMSTemplate.getVehicleUserMobile());
        log.info("短信结束-用车人申请短信:用车人申请短信结束", JSON.toJSON(mapTwo));
        //------------------------------------------------------------
        log.info("短信开始-调度员短信:调度员短信开始");
        Map<String, String> mapThree = Maps.newHashMap();
        //①申请人单位名称；②业务员信息；③订单编号；④用车人信息；⑤预约天数；⑥用车时间；⑦服务车型；⑧预约备注
        mapThree.put("deptName", deptName);//申请人单位名称
        mapThree.put("carTypeName", carTypeName);//服务车型
        mapThree.put("orderNumber", orderNumber);//订单编号
        mapThree.put("subscribeTime", subscribeTime);//用车时间
        mapThree.put("salesman", salesman); //业务员
        mapThree.put("vehicleUser", vehicleUser);//用车人
        mapThree.put("applyDays", applyDays); //预约天数
        if (StringUtils.isNotBlank(undoSMSTemplate.getNotes())) {
            String reason = undoSMSTemplate.getNotes();
            mapThree.put("reason", "预约备注：" + reason); //预约备注
        } else {
            mapThree.put("reason", "预约备注：无");
        }
        //发给内部车队所有调度员
        ApplyOfficialRequest applyOfficialRequest = new ApplyOfficialRequest();
        applyOfficialRequest.setCompanyId(undoSMSTemplate.getCompanyId());
        List<EcmpUser> ecmpUser = carGroupDispatcherInfoMapper.getCarGroupDispatcherList(applyOfficialRequest);
        if (!ecmpUser.isEmpty()) {
            for (EcmpUser user : ecmpUser) {
                iSmsTemplateInfoService.sendSms(SmsTemplateConstant.SMS_FOSHAN_INTERNAL_DISPATCHER, mapThree, user.getPhonenumber());
            }
        }
        log.info("短信开始-调度员短信:调度员短信结束");
    }

    private Long getApplyIdByOrderId(Long orderId, Long journeryId) {
        if (journeryId == null) {
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            journeryId = orderInfo.getJourneyId();
        }
        Long applyId = null;
        List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(journeryId));
        if (CollectionUtils.isNotEmpty(applyInfos)) {
            applyId = applyInfos.get(0).getApplyId();
        }
        return applyId;
    }

    private void updateOldStateMessage(String configType, Long applyId, Long categoryId, String categorys) {
        List<EcmpMessage> ecmpMessages = ecmpMessageMapper.queryList(new EcmpMessageDto(configType, categoryId, applyId, MsgStatusConstant.MESSAGE_STATUS_T002.getType(), categorys));
        if (CollectionUtils.isNotEmpty(ecmpMessages)) {
            for (EcmpMessage message : ecmpMessages) {
                message.setStatus(MsgStatusConstant.MESSAGE_STATUS_T001.getType());
                message.setUpdateBy(Long.valueOf(ONE));
            }
            ecmpMessageMapper.updateList(ecmpMessages);
        }
    }


    @Async
    @Override
    public void sendSmsServiceStart(long orderId) {
        log.info("短信开始-订单{},司机开始服务", orderId);
        try {
            DriverSmsInfo orderCommonInfo = getOrderinfo(orderId);
            //用车人
            String applyMobile = orderCommonInfo.getApplyMobile();

            Map<String, String> orderCommonInfoMap = objToMap(orderCommonInfo);
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_START_SERVICE, orderCommonInfoMap, applyMobile);

        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机开始服务", orderId);
    }

    private DriverSmsInfo getOrderinfo(Long orderId) {
        DriverSmsInfo smsInfo = orderInfoMapper.getOrderInfo(orderId);
        log.info(smsInfo.toString());
        String startDate = smsInfo.getStartDate();
        String endDate = smsInfo.getEndDate();
        String sdate;
        String edate;
        if (null != startDate && !StringUtils.isEmpty(startDate)) {
            sdate = DateUtils.getYearMonthDayHourMinuteSecond(DateUtils.parseDate(startDate).getTime());
        } else {
            sdate = "";
        }
        if (null != endDate && !StringUtils.isEmpty(endDate)) {
            edate = DateUtils.getYearMonthDayHourMinuteSecond(DateUtils.parseDate(endDate).getTime());
        } else {
            edate = "";
        }

        smsInfo.setStartDate(sdate);
        smsInfo.setEndDate(edate);
        //获取开始结束时间 加金额
        ///order_service_cost_detail_record_info表--start_time--end_time---total_fee
        return smsInfo;
    }


    @Async
    @Override//司机结束服务
    public void sendSmsDriverServiceEnd(long orderId) {
        log.info("短信开始-订单{},司机结束服务", orderId);
        try {
            DriverSmsInfo orderCommonInfo = getOrderinfo(orderId);
            if (null != orderCommonInfo.getTotalFee()) {
                BigDecimal totalFee = orderCommonInfo.getTotalFee();
                BigDecimal totalFee2 = totalFee.setScale(2, RoundingMode.HALF_UP);//保留两位小数
                orderCommonInfo.setTotalFee(totalFee2);
            } else {
                orderCommonInfo.setTotalFee(new BigDecimal(0.00));
            }
            Map<String, String> orderCommonInfoMap = objToMap(orderCommonInfo);
            //用车人
            String applyMobile = orderCommonInfo.getApplyMobile();
            log.info("短信已发送用车人电话：{}", applyMobile);
            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.PRICAR_DRIVER_SERVICE_END, orderCommonInfoMap, applyMobile);

        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
        log.info("短信结束-订单{},司机结束服务", orderId);
    }

    private Map<String, String> objToMap(DriverSmsInfo orderCommonInfo) throws IllegalAccessException {
        Map<String, String> orderCommonInfoMap = new HashMap();
        Class<?> clazz = orderCommonInfo.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = String.valueOf(field.get(orderCommonInfo));
            if (value == null) {
                value = "";
            }
            orderCommonInfoMap.put(fieldName, value);
        }
        return orderCommonInfoMap;
    }


    /**
     * 外部调度员驳回给申请人、乘车人发短信
     *
     * @param orderInfo
     * @param rejectReason
     * @param user
     * @throws Exception
     */
    @Override
    public void sendSmsOutDispatcherReject(OrderInfo orderInfo, String rejectReason, SysUser user) throws Exception {
        String logHeader = "#orderId=" + orderInfo.getOrderId() + " 外部调度员驳回发短信==>";
        String orderNumber = orderInfo.getOrderNumber();
        String useCarPeople = "";
        String useDays = "";
        String useTime = "";
        String carType = "";
        String remark = "";
        String groupTelephone = "";
        String dispatcher = user.getNickName() + " " + user.getPhonenumber();

        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(new JourneyPassengerInfo(orderInfo.getJourneyId()));
        if (CollectionUtils.isEmpty(journeyPassengerInfos)) {
            return;
        }
        JourneyPassengerInfo journeyPassengerInfo = journeyPassengerInfos.get(0);
        log.info("{} journeyPassengerInfo:{}", logHeader, JSON.toJSONString(journeyPassengerInfo));
        //乘车人
        useCarPeople = journeyPassengerInfo.getName() + " " + journeyPassengerInfo.getMobile();
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        log.info("{} journeyInfo:{}", logHeader, JSON.toJSONString(journeyInfo));
        if (journeyInfo == null) {
            return;
        }

        //用车天数
        useDays = journeyInfo.getUseTime();
        //用车时间
        if (null != journeyInfo.getUseCarTime()) {
            useTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, journeyInfo.getUseCarTime());
        }

        //查询约定备注、车队信息、车型
        DismissedOutDispatchDTO dismissedOutDispatchDTO = applyInfoMapper.selectApplyInfoForDismissedMsg(orderInfo.getJourneyId());
        log.info("{} dismissedOutDispatchDTO:{}", logHeader, JSON.toJSONString(dismissedOutDispatchDTO));

        if (dismissedOutDispatchDTO == null) {
            return;
        }

        carType = dismissedOutDispatchDTO.getCarTypeName();
        remark = dismissedOutDispatchDTO.getNotes();
        groupTelephone = dismissedOutDispatchDTO.getTelephone();

        EcmpUser applyPeo = ecmpUserMapper.selectEcmpUserById(orderInfo.getUserId());
        log.info("{} applyPeo:{}", logHeader, JSON.toJSONString(applyPeo));
        if (applyPeo == null) {
            return;
        }
        String applyPeoPhone = applyPeo.getPhonenumber();

        /**给业务员发短信*/
        Map<String, String> map = Maps.newHashMap();
        map.put("orderNumber", orderNumber);
        map.put("useCarPeople", useCarPeople);
        map.put("useDays", useDays);
        map.put("useTime", useTime);
        map.put("carType", carType);
        map.put("remark", remark);
        map.put("rejectReason", rejectReason);
        map.put("groupTelephone", groupTelephone);
        map.put("dispatcher", dispatcher);

        //给乘车人发短信
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.OUT_DISPATCH_DISMISS_MESSAGE, map, journeyPassengerInfo.getMobile());

        //给申请人发短信
        iSmsTemplateInfoService.sendSms(SmsTemplateConstant.OUT_DISPATCH_DISMISS_MESSAGE, map, applyPeoPhone);
        log.info("{} 外部车队驳回发送短信内容={},发送的乘车人={},发送的申请人:{}", logHeader, JSON.toJSONString(map), journeyPassengerInfo.getMobile(), applyPeoPhone);
    }


}
