package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ghb
 * @description 订单支付表
 * @date 2020/5/7
 */


@Data
public class OrderPayInfo {

    /**
     * 支付单编号    和第三方支付平台建立绑定关系
     */
    @NotNull
    @ApiParam(required = true)
    private String payId;

    /**
     * 结算单  编号
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
     *   支付状态  0000 已支付  N111未支付
     */
    private String state;

    /**
     *  支付方式  M001-结单后付费  M002-开单预付费   M003-充值卡扣款   M999-其他
     */
    private String payMode;

    /**
     * 支付渠道  weixin    zhifubao
     */
    private String payChannel;

    /**
     * 渠道费率
     */
    private BigDecimal channelRate;

    /**
     *  支付流水
     */
    private String transactionLog;


    /**
     * 用户订单支付总金额
     */
    private BigDecimal amount;

    /**
     * 渠道扣费总额
     */
    private BigDecimal channelAmount;

    /**
     *  到企业账户余额
     */
    private BigDecimal arriveAmount;

    /**
     *  支付完成状态
     */
    private String finishPayTime;

    /**
     *  完成结果，各个支付渠道返回的返回结果
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

    public OrderPayInfo() {
    }

    public OrderPayInfo(@NotNull String payId, @NotNull String state) {
        this.payId = payId;
        this.state = state;
    }
}
