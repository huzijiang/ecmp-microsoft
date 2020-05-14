package com.hq.ecmp.ms.api.controller.pay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.ms.api.util.IpAddressUtil;
import com.hq.ecmp.ms.api.util.PayUtil;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/pay")
@Api(tags = {"微信支付接口"}, description = "")
public class WxPayController {

    private static final Logger log = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private IOrderInfoService iOrderInfoService;

    @Autowired
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Autowired
    private IOrderSettlingInfoService iOrderSettlingInfoService;

    @Autowired
    private IOrderPayInfoService iOrderPayInfoService;

    protected WxPayConfig config;

    /**
     * @author ghb
     * @description  微信app支付接口，统一下单
     */
    @ApiOperation(value = "微信支付接口", notes = "")
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    @ResponseBody
    public WxPayAppOrderResult pay(@RequestBody String param, HttpServletRequest request) {
        log.info("微信支付，传来的参数为："+param);
        String ipAddr = IpAddressUtil.getIpAddr(request);
        JSONObject jsonObject = JSONObject.parseObject(param);
        String orderId = jsonObject.getString("payId");
        String price = jsonObject.getString("price");
        String body = jsonObject.getString("body");
        WxPayAppOrderResult result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            //商品描述
            orderRequest.setBody(body);
            //商户订单号
            orderRequest.setOutTradeNo(orderId);
            //金额
            orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(price));//元转成分
            //ip
            orderRequest.setSpbillCreateIp(ipAddr);
            //签名
            orderRequest.setSign(wxPayService.getSandboxSignKey());
            //随机字符串
            String s = PayUtil.makeUUID(32);
            orderRequest.setNonceStr(s);
            //统一下单
            result = wxPayService.createOrder(orderRequest);
            log.info("微信下单后返回结果为："+result);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("下单失败，错误信息为："+e);
            log.info("下单失败，错误信息为："+e.getMessage());
        }
        return result;
    }


    /**
     * @author ghb
     * @description  微信app支付回调接口
     */
    @RequestMapping(value = "/wechat/v1/callback", method = RequestMethod.POST)
    @ResponseBody
    public String payNotify(String xmlResult){
        log.info("已经进入微信支付回调接口");
        try {
            log.info("回调接口---重要参数为："+xmlResult);
            WxPayOrderNotifyResult  result = wxPayService.parseOrderNotifyResult(xmlResult);
            log.info("回调接口---返回结果--result为："+result);
            if (!"SUCCESS".equals(result.getReturnCode())) {
                log.info("微信支付-通知失败");
                log.error(xmlResult);
                throw new WxPayException("微信支付-通知失败！");
            }
            if (!"SUCCESS".equals(result.getResultCode())) {
                log.info("微信支付-通知失败");
                log.error(xmlResult);
                throw new WxPayException("微信支付-通知失败！");
            }
            //判断订单是否已支付
            OrderPayInfo orderPayInfoByPayId = iOrderPayInfoService.getOrderPayInfoByPayId(result.getOutTradeNo());
            if(null != orderPayInfoByPayId && !OrderPayConstant.PAID.equals(orderPayInfoByPayId.getState())){
                //把订单状态改为关闭状态
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(orderPayInfoByPayId.getOrderId());
                orderInfo.setState(OrderState.ORDERCLOSE.getState());
                OrderInfo orderInfo1 = iOrderInfoService.selectOrderInfoById(orderPayInfoByPayId.getOrderId());
                if(null != orderInfo1){
                    int i = iOrderInfoService.updateOrderInfo(orderInfo);
                    if(1 == i){
                        log.info("订单信息表修改成功");
                    }else{
                        log.info("订单信息表修改失败");
                    }
                }else{
                    log.info("该订单不存在");
                }
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo(orderPayInfoByPayId.getOrderId(), OrderState.ORDERCLOSE.getState());
                int j = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
                if(1 ==j){
                    log.info("订单轨迹表修改成功");
                }else{
                    log.info("订单轨迹表修改失败");
                }
                //插入订单支付表
                OrderPayInfo orderPayInfo = new OrderPayInfo();
                orderPayInfo.setPayId(result.getOutTradeNo());
                orderPayInfo.setTransactionLog(result.getTransactionId());
                OrderSettlingInfo  orderSettlingInfo = new OrderSettlingInfo();
                List<OrderSettlingInfo> orderSettlingInfos = iOrderSettlingInfoService.selectOrderSettlingInfoList(orderSettlingInfo);
                if(orderSettlingInfos.size() != 0){
                    orderPayInfo.setBillId(orderSettlingInfos.get(0).getBillId());
                }
                orderPayInfo.setState(OrderPayConstant.PAID);
                orderPayInfo.setPayMode(OrderPayConstant.PAY_AFTER_STATEMENT);
                orderPayInfo.setPayChannel(OrderPayConstant.PAY_CHANNEL_WX);
                orderPayInfo.setChannelRate(new BigDecimal(OrderPayConstant.WX_CHANNEL_RATE));
                //分转换为元
                BigDecimal totalFee = BigDecimal.valueOf(Long.parseLong(result.getTotalFee().toString())).divide(new BigDecimal(100));
                orderPayInfo.setAmount(totalFee);
                //渠道费
                BigDecimal channelAmount = new BigDecimal(OrderPayConstant.WX_CHANNEL_RATE).multiply(totalFee);
                orderPayInfo.setChannelAmount(channelAmount);
                //到账金额
                BigDecimal arriveAmount = totalFee.subtract(channelAmount);
                orderPayInfo.setArriveAmount(arriveAmount);
                orderPayInfo.setCreateTime(DateUtils.getNowDate());
                int k = iOrderPayInfoService.updateOrderPayInfo(orderPayInfo);
                if(1 == k){
                    log.info("订单支付表----- 信息已更新");
                }
            }else{
                log.info("该订单已支付");
            }

            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("微信支付失败----------------错误信息为："+e);
            log.info("微信支付失败----------------错误信息为："+e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}

