package com.hq.ecmp.ms.api.controller.order;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.DictDto;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.EcmpDictData;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:16
 */
@RestController
@RequestMapping("/settle")
public class SettleController {


    /**
     * 通过订单查询订单的详细费用结算信息
     * @param  orderDto  数据字典信息
     * @return
     */
    @ApiOperation(value = "getSettleDetailInfoByOrder",notes = "通过订单编号查询订单的详细结算信息 ",httpMethod ="POST")
    @PostMapping("/getSettleDetailInfoByOrder")
    public ApiResponse<OrderSettlingInfo> getDictDataByType(OrderDto orderDto){

        return null;
    }

}
