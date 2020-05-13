package com.hq.ecmp.ms.api.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.ms.api.conf.AlipayConfig;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderPayInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ghb
 * @description 支付宝支付接口
 * @date 2020/5/5
 */
@Controller
@RequestMapping("/pay")
@Api(tags = {"-支付宝支付接口"}, description = "")
public class AliPayController {

    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private IOrderInfoService iOrderInfoService;

    @Autowired
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    @Autowired
    private IOrderSettlingInfoService iOrderSettlingInfoService;

    @Autowired
    private IOrderPayInfoService iOrderPayInfoService;

    /**
     * @author ghb
     * @description  支付宝支付接口，统一下单
     */
    @ApiOperation(value = "支付宝支付接口", notes = "")
    @RequestMapping(value = "/ali", method = RequestMethod.POST)
    @ResponseBody
    public String pay(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        String orderId = jsonObject.getString("payId");
        String price = jsonObject.getString("price");
        String body = jsonObject.getString("body");
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(body); //商品标题
        model.setOutTradeNo(orderId.toString()); //商家订单的唯一编号
        model.setTimeoutExpress(OrderPayConstant.ORDER_PAY_TIMEOUT); //超时关闭该订单时间
        model.setTotalAmount(price);  //订单总金额
        model.setProductCode(OrderPayConstant.PRODUCT_CODE); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);  //回调地址
        String orderString = "";
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            if(response.isSuccess()){
                log.info("支付宝下单成功--------------------------------------------------");
            }
            //orderString 可以直接给客户端请求，无需再做处理。
            orderString = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("支付宝下单失败，错误原因为："+e);
            log.info("支付宝下单失败，错误原因为："+e.getMessage());
        }
        return orderString;
    }

    /**
     * @author ghb
     * @description  支付回调接口
     */
    @RequestMapping(value = "/ali/v1/callback")
    public Boolean payNotify(String params,String out_trade_no, String trade_no, String total_amount) {
        log.info("！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        log.info("！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        log.info("已经进入支付宝支付回调接口");
//        Map<String, String> params = new HashMap<String, String>();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
        log.info("回调接口，重要参数：---" + params);
        Map<String, String> stringStringMap = mapStringToMap(params);
        log.info("String转换为map为================="+stringStringMap);
        //支付宝公钥
        String alipayPublicKey = AlipayConfig.ALIPAY_PUBLIC_KEY;
        //字符编码
        String charset = AlipayConfig.CHARSET;
        boolean flag = false;
        try {
            flag = AlipaySignature.rsaCheckV1(stringStringMap, alipayPublicKey, charset, "RSA2");
            if(flag){
                log.info("支付宝回调签名认证成功");
//                String out_trade_no = request.getParameter("out_trade_no");
//                String trade_no = request.getParameter("trade_no");
//                String total_amount = request.getParameter("total_amount");
                log.info("支付宝回调获取到的订单号为："+out_trade_no);
                log.info("支付宝回调获取到的流水号为："+trade_no);
                log.info("支付宝回调获取到的金额为："+total_amount);
                //判断订单是否已支付
                OrderPayInfo orderPayInfoByPayId = iOrderPayInfoService.getOrderPayInfoByPayId(out_trade_no);
                if(null != orderPayInfoByPayId && OrderPayConstant.UNPAID.equals(orderPayInfoByPayId.getState())){
                    //把订单状态改为关闭状态
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setOrderId(orderPayInfoByPayId.getOrderId());
                    orderInfo.setState(OrderState.ORDERCLOSE.getState());
                    int i = iOrderInfoService.updateOrderInfo(orderInfo);
                    OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo(orderPayInfoByPayId.getOrderId(), OrderState.ORDERCLOSE.getState());
                    int j = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
                    if(1 == i && 1 ==j){
                        log.info("订单信息修改成功");
                    }else{
                        log.info("订单信息修改失败");
                    }
                    //插入订单支付表
                    OrderPayInfo orderPayInfo = new OrderPayInfo();
                    orderPayInfo.setTransactionLog(trade_no);
                    OrderSettlingInfo orderSettlingInfo = new OrderSettlingInfo();
                    orderSettlingInfo.setOrderId(orderPayInfoByPayId.getOrderId());
                    List<OrderSettlingInfo> orderSettlingInfos = iOrderSettlingInfoService.selectOrderSettlingInfoList(orderSettlingInfo);
                    if(orderSettlingInfos.size() != 0){
                        orderPayInfo.setBillId(orderSettlingInfos.get(0).getBillId());
                    }
                    orderPayInfo.setPayId(out_trade_no);
                    orderPayInfo.setState(OrderPayConstant.PAID);
                    orderPayInfo.setPayMode(OrderPayConstant.PAY_AFTER_STATEMENT);
                    orderPayInfo.setPayChannel(OrderPayConstant.PAY_CHANNEL_ALI);
                    orderPayInfo.setChannelRate(new BigDecimal(OrderPayConstant.ALI_CHANNEL_RATE));
                    orderPayInfo.setAmount(new BigDecimal(total_amount));
                    //渠道费
                    BigDecimal channelAmount = new BigDecimal(OrderPayConstant.WX_CHANNEL_RATE).multiply(new BigDecimal(total_amount));
                    orderPayInfo.setChannelAmount(channelAmount);
                    //到账金额
                    BigDecimal arriveAmount = new BigDecimal(total_amount).subtract(channelAmount);
                    orderPayInfo.setArriveAmount(arriveAmount);
                    orderPayInfo.setCreateTime(new Date());
                    int k = iOrderPayInfoService.updateOrderPayInfo(orderPayInfo);
                    if(1 == k){
                        log.info("订单支付表----- 信息已更新");
                    }
                }else{
                    log.info("该订单已支付");
                }
            }else{
                log.info("支付宝回调签名认证失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("支付宝回调失败，错误原因为："+e);
            log.info("支付宝回调失败，错误原因为："+e.getMessage());
        }
        log.info("支付回调标志" + flag);
        return flag;
    }

    private static Map<String,String> mapStringToMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(", ");
        Map<String,String> map = new HashMap();
            for (String string : strs) {
                String key=string.split("=")[0];
                String value=string.split("=")[1];
                map.put(key, value);
            }
        return map;
    }

//    public static void main(String[] args) {
////        String ss = "{gmt_create=2020-05-13 15:37:18, charset=UTF-8, seller_email=finance@hqzhuanche.com, subject=????????????0.01?, sign=XlzwPNW3cnTsn5q2R/D vCzgg6sv0PF37uQv1jgsA1RNjL cVoabbBhFm8wKYWD1Rs2Jv4 3Oem2ZYHBM8Tix1FhOZWVAj2q/CaEce9vbpFUNus3l5l7WNzAZNdvGZjfgHuwiYGXKpwtNKTG4UtBxZ/ sp39YSgMdTy6icBOETs88Z2Ik QgJSBTQwFTXuzu3bfGV0Yn18KzFaclpg4auf3/ 6xJDZRvIPy2LuCP07cmjhis7c53jR2EQdl4/Y6XGYK9C9/FaRTFBx9f 30vtDdNuJhThnF4gG P6carloW4E0bj7l12UVheQK3EiTywPbxr9PHxUTEjhiYTYqiEag==, buyer_id=2088022153869156, invoice_amount=0.01, notify_id=2020051300222153718069151417347880, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"PCREDIT\"}], notify_type=trade_status_sync, trade_status=TRADE_SUCCESS, receipt_amount=0.01, app_id=2021001160623612, buyer_pay_amount=0.01, sign_type=RSA2, seller_id=2088331209193267, gmt_payment=2020-05-13 15:37:18, notify_time=2020-05-13 15:40:47, version=1.0, out_trade_no=1wf98yuirplkjkgf9s8gx84d3813fre5, total_amount=0.01, trade_no=2020051322001469151419497525, auth_app_id=2021001160623612, buyer_logon_id=151****4973, point_amount=0.00}";
////        Map<String, String> stringStringMap = mapStringToMap(ss);
////        System.out.println(stringStringMap);
////    }
}

