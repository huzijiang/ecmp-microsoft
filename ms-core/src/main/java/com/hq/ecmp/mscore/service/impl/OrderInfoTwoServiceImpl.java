package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.constant.ApplyTypeEnum;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderSettlingInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.CancelOrderCostVO;
import com.hq.ecmp.mscore.vo.OrderStateVO;
import com.hq.ecmp.mscore.vo.RunningOrderVo;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private IOrderSettlingInfoService orderSettlingInfoService;
    @Resource
    private IOrderPayInfoService iOrderPayInfoService;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private ThirdService thirdService;
    @Resource
    private IsmsBusiness ismsBusiness;

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
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason,Long longinUserId) throws Exception{
        CancelOrderCostVO vo=new CancelOrderCostVO();
        BigDecimal cancelFee1=BigDecimal.ZERO;
        int isPayFee=0;
        String ownerAmount=null;
        String personalAmount=null;
        String payId=null;
        String payState=null;
        OrderStateVO orderStateVO = orderInfoService.getOrderState(orderId);
        if (!longinUserId.equals(orderStateVO.getUserId())){
            throw new  Exception("取消订单操作人与申请人不一致,不可取消");
        }
        if (orderStateVO==null){
            throw new  Exception("该订单不存在!");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderStateVO.getJourneyId());
        String state = orderStateVO.getState();
        Date useCarDate = orderStateVO.getUseCarDate();
        if (useCarDate==null){
            throw new  Exception("用车时间不明确!");
        }
        //修改权限标识
        boolean opType=true;
        int intState=Integer.parseInt(state.substring(1));
        //校验是否超过用车时间
        //未派车(无车无司机)
        if (intState < Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {
            log.info("订单:"+orderId+"无车无司机取消订单-------> start,原因{}",cancelReason);
            int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
        } else if (intState >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1)) &&
                intState < Integer.parseInt(OrderState.INSERVICE.getState().substring(1))) {
            log.info("订单:"+orderId+"有车有司机取消订单------- start,原因{}",cancelReason);
            //待服务(有车有司机)
            if (CarConstant.USR_CARD_MODE_HAVE .equals(orderStateVO.getUseCarMode())){
                //自由车带服务取消
                int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
                if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(orderStateVO.getApplyType())){//公务
                    if (DateFormatUtils.compareDayAndTime(useCarDate,DateUtils.getNowDate()) == 1) {
                        //公务自由车带服务取消超时后  权限消失
                        opType=false;
                    }
                }
            }else{//网约车带服务的取消
                JSONObject jsonObject = thirdService.threeCancelServer(orderId, cancelReason);
                cancelFee1 = jsonObject.getDouble("cancelFee")==null?BigDecimal.ZERO:BigDecimal.valueOf(jsonObject.getDouble("cancelFee"));
                if (cancelFee1.compareTo(BigDecimal.ZERO)<=0){
                    //个人不需要支付取消费
                    if (DateFormatUtils.compareDayAndTime(useCarDate,DateUtils.getNowDate()) == 1) {
                        this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
                        //公务网约车待服务取消超时无需支付取消费后  权限消失
                        opType=false;
                    }
                }else{
                    //需要支付取消费
//                    cancelFee=cancelFee1.stripTrailingZeros().toPlainString();
                    String s = iOrderPayInfoService.checkOrderFeeOver(orderId, journeyInfo.getRegimenId(), orderStateVO.getUserId());
                    /*超额个人支付*/
                    if (StringUtils.isNotBlank(s)&&new BigDecimal(s).compareTo(BigDecimal.ZERO)==1){
                        BigDecimal personPay=new BigDecimal(s);
                        BigDecimal ownerPay=cancelFee1.subtract(new BigDecimal(s));
                        ownerAmount=ownerPay.stripTrailingZeros().toPlainString();
                        isPayFee=1;
                        personalAmount=personPay.toPlainString();
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, ownerPay, personPay);
                        payId=orderPayInfo.getPayId();
                        payState=orderPayInfo.getState();
                    }else{
                        /*全部由企业支付*/
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, cancelFee1, BigDecimal.ZERO);
                        iOrderPayInfoService.updateOrderPayInfo(new OrderPayInfo(orderPayInfo.getPayId(), OrderPayConstant.PAID));
                        payId=orderPayInfo.getPayId();
                        payState=OrderPayConstant.PAID;
                    }
                }
            }
        }
        //修改权限为未使用
//        if (opType){
            iJourneyUserCarPowerService.updatePowerSurplus(orderStateVO.getPowerId(),2);
//        }
        vo.setIsPayFee(isPayFee);
        vo.setCancelAmount(cancelFee1.stripTrailingZeros().toPlainString());
        vo.setOwnerAmount(ownerAmount);
        vo.setPersonalAmount(personalAmount);
        vo.setPayState(payState);
        vo.setPayId(payId);
        ismsBusiness.sendMessageCancelOrder(orderId,longinUserId);
        /**发送取消订单短信*/
        if(cancelFee1.compareTo(BigDecimal.ZERO)==1){
            ismsBusiness.sendSmsCancelOrder(orderId);
        }else{
            ismsBusiness.sendSmsCancelOrderHaveFee(orderId,cancelFee1.doubleValue());
        }
        return vo;
    }

    /**
     * 首次登陆进行中的行程订单
     * @param userId
     * @return
     */
    @Override
    public List<RunningOrderVo> runningOrder(Long userId) {
        String states=OrderState.INSERVICE.getState()+","+OrderState.READYSERVICE.getState()
                +","+OrderState.ALREADYSENDING.getState()+","+OrderState.REASSIGNMENT.getState();
        return orderInfoMapper.getRunningOrder(userId,states);
    }

    /**自有车取消订单**/
    private int ownerCarCancel(Long orderId,String cancelReason,Long userId) throws Exception{
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        this.insertOrderStateTraceInfo(orderId,OrderState.ORDERCANCEL.getState(),cancelReason,userId);
        return suc;
    }

    /**网约车取消订单*/
    private OrderPayInfo onlineCarCancel(Long orderId,String cancelReason,Long userId,BigDecimal cancelFee,BigDecimal ownerFee,BigDecimal persionFee) throws Exception{
        int i = this.ownerCarCancel(orderId, cancelReason, userId);
        if (i!=ONE){
            throw new Exception("取消订单失败!");
        }
        OrderPayInfo orderPayInfo=null;
        if (cancelFee!=null&&cancelFee.compareTo(BigDecimal.ZERO)==1){
            String json = orderSettlingInfoService.formatCostFee(new OrderSettlingInfoVo(), persionFee,ownerFee);
            orderPayInfo = iOrderPayInfoService.insertOrderPayAndSetting(orderId, cancelFee, String.valueOf(ZERO), String.valueOf(ZERO), json, userId);
        }
        return orderPayInfo;
    }

    /**插入订单轨迹*/
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
