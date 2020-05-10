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
import com.hq.ecmp.ms.api.util.IpAddressUtil;
import com.hq.ecmp.ms.api.util.PayUtil;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@Api(tags = {"-微信支付接口"}, description = "")
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
        Integer orderId = jsonObject.getInteger("orderId");
        String price = jsonObject.getString("price");
        String body = jsonObject.getString("body");
        WxPayAppOrderResult result = null;
            try {
                WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
                //商品描述
                orderRequest.setBody(body);
                //商户订单号
                orderRequest.setOutTradeNo(orderId.toString());
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
//                if(null != result && !"".equals(result.getPrepayId())){
//                    log.info("微信统一下单成功，返回参数为："+result);
//                    map.put("result_code","success");
//                    map.put("result_msg","微信下单成功");
//                    map.put("result",result);
//                }else{
//                    log.info("微信统一下单失败，返回参数为："+result);
//                    map.put("result_code","error");
//                    map.put("result_msg","微信下单失败");
//                }
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
    public String payNotify(HttpServletRequest request){
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult  result = wxPayService.parseOrderNotifyResult(xmlResult);
            log.info("回调接口---返回结果为："+result);
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
            //把订单状态改为关闭状态
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(Long.valueOf(result.getOutTradeNo()));
            orderInfo.setState("S900");
            OrderInfo orderInfo1 = iOrderInfoService.selectOrderInfoById(Long.valueOf(result.getOutTradeNo()));
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
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo(Long.valueOf(result.getOutTradeNo()), "S900");
            int j = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
            if(1 ==j){
                log.info("订单轨迹表修改成功");
            }else{
                log.info("订单轨迹表修改失败");
            }
            //插入订单支付表
            OrderPayInfo orderPayInfo = new OrderPayInfo();
            orderPayInfo.setPayId(Long.valueOf(result.getTransactionId()));
            OrderSettlingInfo  orderSettlingInfo = new OrderSettlingInfo();
            orderSettlingInfo.setOrderId(Long.valueOf(Long.valueOf(result.getOutTradeNo())));
            List<OrderSettlingInfo> orderSettlingInfos = iOrderSettlingInfoService.selectOrderSettlingInfoList(orderSettlingInfo);
            if(orderSettlingInfos.size() != 0){
                orderPayInfo.setBillId(orderSettlingInfos.get(0).getBillId());
            }
            orderPayInfo.setOrderId(Long.valueOf(Long.valueOf(result.getOutTradeNo())));
            orderPayInfo.setState("0000");
            orderPayInfo.setPayMode("M001");
            orderPayInfo.setPayChannel("weixin");
//            orderPayInfo.setChannelRate(0.006);
//            orderPayInfo.setAmount(new BigDecimal(Long.valueOf(result.getTotalFee())));
            orderPayInfo.setAmount(new BigDecimal(result.getTotalFee()));
//            orderPayInfo.setChannelAmount(1L);
//            orderPayInfo.setArriveAmount(9L);
            orderPayInfo.setCreateTime(DateUtils.getNowDate());
            int k = iOrderPayInfoService.insertOrderPayInfo(orderPayInfo);
            if(1 == k){
                log.info("订单支付表----- 信息已更新");
            }
            return WxPayNotifyResponse.success("处理成功!");
        } catch (Exception e) {
            log.info("微信支付失败----------------错误信息为："+e);
            log.info("微信支付失败----------------错误信息为："+e.getMessage());
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}

