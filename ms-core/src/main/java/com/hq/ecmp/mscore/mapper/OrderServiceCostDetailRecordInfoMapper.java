package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.dto.ordercost.OrderServiceCostDetailRecordInfo;

import java.util.List;

/***
 * 订单费用明细
 */
public interface OrderServiceCostDetailRecordInfoMapper {


    /***
     *
     * @param data
     * @return
     */
    List<OrderServiceCostDetailRecordInfo> getList(OrderServiceCostDetailRecordInfo data);



}
