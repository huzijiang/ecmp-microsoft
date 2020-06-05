package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import com.hq.ecmp.mscore.dto.Page;

import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyDispatchQuery  extends PageResult {
        Long userId; //用户id

        String applyName;//申请人姓名

        String applyMobile;//申请人手机号

        String cityCode;//用车城市编码

        Date optStartDate;//操作时间  开始

        Date optEndDate;//操作时间  结束

        String useCarMode;//用车方式  自有车-W100     网约车 -W200

        String dispatchStatus;//调度状态   T001-待派车/待改派    T002-已过期    T003-已处理/已通过  T004-已驳回

        String driverName;//驾驶员姓名

        String driverMobile;//驾驶员手机号

        @ApiModelProperty(value = "待派车状态")
        private String  homePageWaitingCarState;

        @ApiModelProperty(value = "已派车状态")
        private String  homePageUsingCarState;

        @ApiModelProperty(value = "已过期状态")
        private String  homePageExpireCarState;

        @ApiModelProperty(value = "首页订单动态开始时间")
        private String  homeDynamicBeginTime;

        @ApiModelProperty(value = "首页订单动态结束时间")
        private String  homeDynamicEndTime;

        Long companyId;
        private String detpId;
        String dispatchType;
        List<Long> orderIds;
        String serverDepts;

        private Integer pageNum;
        /**
         * 是否是首页数据 1 是 2 否
         */
        private int isIndex ;
        /**
         * 订单id
         */
        private Long orderId;
        private int inOrOut;
        private String rejectReason;
        private String carGroupIds;
        private int isInnerDispatch;
        private int roleData;

}
