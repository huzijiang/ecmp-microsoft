package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ghb
 * @description 订单退款表
 * @date 2020/5/16
 */


@Data
public class OrderRefundInfo {

    /**
     * 支付单退款编号
     */
    @NotNull
    @ApiParam(required = true)
    private String refundId;

    /**
     * 支付单编号    和第三方支付平台建立绑定关系
     */
    @NotNull
    @ApiParam(required = true)
    private String payId;

    /**
     *订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long billId;

    /**
     *订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long orderId;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     *  退款流水
     */
    private String transactionLog;

    /**
     *  交易截止时间
     */
    private Date finishPayTime;

    /**
     *  退款结果
     */
    private String finishResult;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     *  创建时间
     */
    private Date createTime;

    /**
     *  更新者
     */
    private Long updateBy;

    /**
     *  更新时间
     */
    private Date updateTime;

}
