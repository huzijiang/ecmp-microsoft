package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.dto.ordercost.OrderServiceImagesInfo;

import java.util.List;

public interface OrderServiceImagesInfoMapper {

    /***
     *
     * @param orderServiceImagesInfo
     * @return
     */
    List<OrderServiceImagesInfo> getList(OrderServiceImagesInfo orderServiceImagesInfo);


}
