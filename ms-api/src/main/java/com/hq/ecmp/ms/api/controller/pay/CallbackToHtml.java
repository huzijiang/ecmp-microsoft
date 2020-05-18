package com.hq.ecmp.ms.api.controller.pay;

import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderPayInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderPayInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

/**
 * @author ghb
 * @description 支付成功更改为待确认状态，若回调，则改为已支付状态
 * @date 2020/5/14
 */

@RestController
@RequestMapping("/pay")
public class CallbackToHtml {

    private static final Logger log = LoggerFactory.getLogger(CallbackToHtml.class);

    @Autowired
    @Lazy
    private IOrderPayInfoService iOrderPayInfoService;

    @Autowired
    private IOrderInfoService iOrderInfoService;

    @Autowired
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;

    /**
     * @author ghb
     * @description  支付后。通过订单后。更改订单状态
     */
    @ApiOperation(value = "支付后。通过订单后。更改订单状态", notes = "")
    @PostMapping(value = "/updateState")
    public void pay(String payId) {
        log.info("支付后。通过订单后。更改订单状态----------------,该订单号为："+payId);
        //先查询该订单状态
        OrderPayInfo orderPayInfoByPayId = iOrderPayInfoService.getOrderPayInfoByPayId(payId);
        if(null != orderPayInfoByPayId && OrderPayConstant.UNPAID.equals(orderPayInfoByPayId.getState())){
            OrderPayInfo orderPayInfo = new OrderPayInfo();
            orderPayInfo.setPayId(payId);
            orderPayInfo.setState(OrderPayConstant.CNFIRM_PAID);
            int k = iOrderPayInfoService.updateOrderPayInfo(orderPayInfo);
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
            if(1 == k){
                log.info("支付后。通过订单后。更改订单状态----- 信息已更新");
            }
        }
    }
}
