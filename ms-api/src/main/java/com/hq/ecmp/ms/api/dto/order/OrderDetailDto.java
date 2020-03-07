package com.hq.ecmp.ms.api.dto.order;

import com.hq.ecmp.mscore.vo.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderDetailDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/5 16:12
 * @Version 1.0
 */
@Data
public class OrderDetailDto {

    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 用车时间
     */
    private String bookingDate;
    /**
     * 上车地址
     */
    private AddressVO startAddr;
    /**
     * 下车地址
     */
    private AddressVO endAddr;

    /**
     * 途经地，集合
     */
    private List<AddressVO> passingAddr;
    /**
     * 申请类型 eg 公务级、行政经
     */
    private String applyType;
    /**
     * 乘车人
     */
    private UserVO passenger;

    /**
     * 同行人
     */
    private UserVO partner;
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 是否返程
     */
    private Integer isGoBack;

    /**
     * 是否展示新订单标志
     */
    private Integer isNew;

    /**
     * 司机信息
     */
    private DriverInfoVO driverInfoVO;

    /**
     * 车辆信息
     */
    private CarInfoVO carInfoVO;

    /**
     * 订单相关的审批流
     */
    private List<ApprovalFlow> flowList;

    /**
     * 改派状态
     */
    private Integer reassignStatus;

    /**
     * 账单结算信息
     */
    private BillVO billVO;
}
