package com.hq.ecmp.ms.api.controller.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ghb
 * @description 支付宝支付接口
 * @date 2020/5/5
 */
@RestController
@RequestMapping("/pay")
@Api(tags = {"支付宝支付接口"}, description = "")
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

    @Autowired
    private IOrderRefundInfoService iOrderRefundInfoService;

    @Value("${ali.app_id}")
    private String app_id;

    @Value("${ali.app_private_key}")
    private String app_private_key;

    @Value("${ali.ali_pay_public_key}")
    private String ali_pay_public_key;

    @Value("${ali.notify_url}")
    private String notify_url;

    @Value("${ali.url}")
    private String url;

    @Value("${ali.charset}")
    private String charset;

    @Value("${ali.refund_charset}")
    private String refund_charset;

    @Value("${ali.format}")
    private String format;

    @Value("${ali.sign_type}")
    private String sign_type;

    @Value("${ali.refund_notify_url}")
    private String refund_notify_url;


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
        AlipayClient alipayClient = new DefaultAlipayClient(url, app_id, app_private_key, format, charset, ali_pay_public_key, sign_type);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(body); //商品标题
        model.setOutTradeNo(orderId); //商家订单的唯一编号
        model.setTimeoutExpress(OrderPayConstant.ORDER_PAY_TIMEOUT); //超时关闭该订单时间
        model.setTotalAmount(price);  //订单总金额
        model.setProductCode(OrderPayConstant.PRODUCT_CODE); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        request.setBizModel(model);
        request.setNotifyUrl(notify_url);  //回调地址
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
    @ResponseBody
    public String payNotify(String out_trade_no, String trade_no, String total_amount) {
        log.info("已经进入支付宝支付回调接口");
        try {
                log.info("支付宝回调签名认证成功");
                log.info("支付宝回调获取到的订单号为："+out_trade_no);
                log.info("支付宝回调获取到的流水号为："+trade_no);
                log.info("支付宝回调获取到的金额为："+total_amount);
                //判断订单是否已支付
                OrderPayInfo orderPayInfoByPayId = iOrderPayInfoService.getOrderPayInfoByPayId(out_trade_no);
                if(null != orderPayInfoByPayId && !OrderPayConstant.PAID.equals(orderPayInfoByPayId.getState())){
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
        } catch (Exception e) {
            e.printStackTrace();
            log.info("支付宝回调失败，错误原因为："+e);
            log.info("支付宝回调失败，错误原因为："+e.getMessage());
        }
        return "success";
    }

    @ApiOperation(value = "支付宝退款", notes = "")
    @RequestMapping(value = "/ali/refund", method = RequestMethod.POST)
    @ResponseBody
    public String aliRefun(@RequestBody String param) {
        JSONObject jsonObject = JSONObject.parseObject(param);
        String payId = jsonObject.getString("payId");
        String refundAmount = jsonObject.getString("refundAmount");
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest(); // 统一收单交易退款接口
        AlipayClient alipayClient = new DefaultAlipayClient(url, app_id, app_private_key, format, refund_charset, ali_pay_public_key, sign_type);
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        //退款参数
        model.setOutTradeNo(payId);
        model.setRefundAmount(refundAmount);
//        model.setOutRequestNo(outRequestNo); 多次退款，部分退款必传
        request.setBizModel(model);
        AlipayTradeRefundResponse response;
        try {
            response  = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("退款成功！！！！！！！！！！！！！！！！！！！！");
                //处理业务逻辑
                //插入订单退款表

                //先查询订单支付表
                OrderPayInfo orderPayInfoByPayId = iOrderPayInfoService.getOrderPayInfoByPayId(response.getOutTradeNo());
                OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
                orderRefundInfo.setRefundId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));
                orderRefundInfo.setPayId(response.getOutTradeNo());
                orderRefundInfo.setBillId(orderPayInfoByPayId.getBillId());
                orderRefundInfo.setOrderId(orderPayInfoByPayId.getOrderId());
                orderRefundInfo.setAmount(new BigDecimal(response.getRefundFee()));
                orderRefundInfo.setTransactionLog(response.getTradeNo());
                orderRefundInfo.setFinishPayTime(response.getGmtRefundPay());
                orderRefundInfo.setFinishResult(response.getMsg());
                orderRefundInfo.setCreateTime(new Date());
                int i = iOrderRefundInfoService.insertOrderRefundInfo(orderRefundInfo);
            } else {
                log.info("退款失败！！！！！！！！！！！！！！！！！！！！");
                return OrderPayConstant.ALI_RETURN_CODE_ERROR;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return OrderPayConstant.ALI_RETURN_CODE_OK;
    }
}

