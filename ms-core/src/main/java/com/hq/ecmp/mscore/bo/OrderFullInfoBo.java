package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.*;
import lombok.Data;

import java.util.List;

/**
 * @auther: zj.hu
 * @date: 2020-07-21
 */
@Data
public class OrderFullInfoBo {

    private OrderInfo orderInfo;

    private JourneyInfo journeyInfo;

    private List<JourneyPlanPriceInfo> journeyPlanPriceInfos;

    private List<JourneyPassengerInfo> journeyPassengerInfos;

    private ApplyInfo applyInfo;

    private OrderDispatcheDetailInfo orderDispatcheDetailInfo;

    private OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo;
}
