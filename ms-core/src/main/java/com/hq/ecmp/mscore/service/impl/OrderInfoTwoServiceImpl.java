package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.constant.ApplyTypeEnum;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderSettlingInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.CancelOrderCostVO;
import com.hq.ecmp.mscore.vo.OrderStateVO;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hq.ecmp.constant.CommonConstant.*;
import static com.hq.ecmp.constant.OrderState.WAITINGLIST;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class OrderInfoTwoServiceImpl implements OrderInfoTwoService
{

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Resource
    private IOrderPayInfoService iOrderPayInfoService;

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;

    /**
     * 公务取消订单
     * @param orderId
     * @param cancelReason
     */
    @Override
    public void cancelBusinessOrder(Long orderId, String cancelReason) throws Exception{
        CancelOrderCostVO vo=new CancelOrderCostVO();
        String cancelFee=null;
        int isPayFee=0;

        OrderStateVO orderStateVO = orderInfoService.getOrderState(orderId);
        if (orderStateVO==null){
            throw new  Exception("该订单不存在!");
        }
        String state = orderStateVO.getState();
        Date useCarDate = orderStateVO.getUseCarDate();
        if (useCarDate==null){
            return;
        }
        boolean opType=true;
        int intState=Integer.parseInt(state.substring(1));
        //校验是否超过用车时间
        //超时
        //未派车(无车无司机)
        if (intState < Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {

        } else if (intState >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1)) &&
                intState < Integer.parseInt(OrderState.INSERVICE.getState().substring(1))) {
            //待服务(有车有司机)
            if (CarConstant.USR_CARD_MODE_HAVE .equals(orderStateVO.getUseCarMode())){//自由车带服务取消
                if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(orderStateVO.getApplyType())){//公务
                    if (DateFormatUtils.compareDayAndTime(useCarDate,DateUtils.getNowDate()) == 1) {
                        //公务自由车带服务取消超时后  权限消失
                        opType=false;
                    }
                }
            }else{//网约车带服务的取消
                JSONObject jsonObject = this.threeCancelServer(orderId, cancelReason);
                BigDecimal cancelFee1 = jsonObject.getDouble("cancelFee")==null?BigDecimal.ZERO:BigDecimal.valueOf(jsonObject.getDouble("cancelFee"));
                if (cancelFee1.compareTo(BigDecimal.ZERO)<=0){
                    //不需要支付取消费
                    if (DateFormatUtils.compareDayAndTime(useCarDate,DateUtils.getNowDate()) == 1) {
                        //公务网约车待服务取消超时无需支付取消费后  权限消失
                        opType=false;
                    }
                }else{
                    //需要支付取消费


                }
            }
        }
        if (opType){
            iJourneyUserCarPowerService.updatePowerSurplus(orderStateVO.getPowerId(),2);
        }
        vo.setIsPayFee(isPayFee);
        vo.setCancelAmount(cancelFee);
    }

    private int ownerCarCancel(Long orderId,String useCarMode,String cancelReason,Long userId) throws Exception{
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        this.insertOrderStateTraceInfo(orderId,OrderState.ORDERCANCEL.getState(),cancelReason,userId);
        return suc;
    }

    private OrderPayInfo onlineCarCancel(Long orderId,String useCarMode,String cancelReason,Long userId,String cancelFee,String ownerFee,String persionFee) throws Exception{
        int i = this.ownerCarCancel(orderId, useCarMode, cancelReason, userId);
        if (i!=ONE){
            throw new Exception("取消订单失败!");
        }
//        if (StringUtils.isNotBlank(useCarMode)&& useCarMode.equals(CarConstant.USR_CARD_MODE_NET )) {
//            //TODO 调用网约车的取消订单接口
//            JSONObject data = threeCancelServer(orderId, cancelReason);
//            cancelFee = data.getString("cancelFee");
//        }
        OrderPayInfo orderPayInfo=null;
        if (cancelFee!=null){
            OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
            OrderSettling OrderSettling =new OrderSettling();
            OrderSettling.setEnterpriseCancellationFee(new BigDecimal(ownerFee).stripTrailingZeros());
            OrderSettling.setPersonalCancellationFee(new BigDecimal(persionFee).stripTrailingZeros());
            String json= JSON.toJSONString(OrderSettling);
            orderPayInfo = iOrderPayInfoService.insertOrderPayAndSetting(orderId, cancelFee, String.valueOf(ZERO), String.valueOf(ZERO), json, String.valueOf(ZERO));
        }
        return orderPayInfo;
    }

    private JSONObject threeCancelServer(Long orderId,String cancelReason)throws Exception{
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("enterpriseId", enterpriseId);
        paramMap.put("enterpriseOrderId", String.valueOf(orderId));
        paramMap.put("licenseContent", licenseContent);
        paramMap.put("mac",  MacTools.getMacList().get(0));
        paramMap.put("reason", cancelReason);
        log.info("网约车订单{}取消参数{}",orderId,paramMap);
        String result = OkHttpUtil.postForm(apiUrl + "/service/cancelOrder", paramMap);
        log.info("网约车订单{}取消返回结果{}",orderId,result);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (ApiResponse.SUCCESS_CODE != jsonObject.getInteger("code")) {
            throw new Exception("调用三方取消订单服务-》取消失败");
        }
        return jsonObject.getJSONObject("data");
    }

    private void insertOrderStateTraceInfo(Long orderId,String state,String cancelReason,Long userId){
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setState(state);
        orderStateTraceInfo.setContent(cancelReason);
        orderStateTraceInfo.setCreateBy(userId+"");
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }
}
