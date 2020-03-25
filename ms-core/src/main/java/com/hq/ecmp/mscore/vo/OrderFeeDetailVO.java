package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: caobj
 * @Date: 2020/3/5 11:10
 */
@Data
@Builder
public class OrderFeeDetailVO {

    private String total;       //订单总金额，计算公式1.2.6
    private String mileage;       //订单总里程
    private String min;       //        订单总时长
    private String actualPayAmount;       //乘客支付金额(优惠前)
    private String customerPayPrice;       //乘客实际支付金额(actualPayAmount-couponSettleAmout)，计算公式1.2.6
    private String basePrice;       //基础价(套餐资费)
    private String includeMileage;       //基础价包含公里
    private String includeMinute;       //基础价包含时长(单位：分钟)
    private String includeMinuteHour;       //忽略
    private String couponAmount;       //优惠券金额，忽略
    private String couponSettleAmout;       //        优惠券抵扣金额
    private int    hasCoupon;    //是否有优惠券:0-无;1-有
    private String decimalsFees;       //        抹零费
    private String isOfflinePay;       //0未知 //1线下 2线上
    private String driverPay;       //        司机代收
    private String mic_price;       //折扣价格
    private String hotDuration;       //        高峰时长
    private String hotDurationFees;       //高峰时长费
    private String hotMileage;       //        高峰里程
    private String hotMileageFees;       //高峰里程费
    private String peakPrice;       //高峰单价(里程)
    private String peakPriceTime;       //高峰单价(时长)
    private String longDistanceNum;       //长途里程
    private String longDistancePrice;       //        长途费
    private String longPrice;       //长途单价(里程)
    private String mileagePrice;       //里程定价
    private String nighitDuration;       //        夜间服务时长
    private String nighitDurationFees;       //夜间服务时长费
    private String nightDistanceNum;       //        夜间服务里程
    private String nightDistancePrice;       //夜间服务里程费
    private String nightPrice;       //夜间服务费（里程）单价
    private String nightPriceTime;       //夜间服务费（时长）单价
    private String overMilageNum;       //        超里程数
    private String overMilagePrice;       //超里程费
    private String overMilageNumTotal;       //里程费小计:平峰里程+高峰里程
    private String overTimeNum;       //        超时长数
    private String overTimePrice;       //超时长费
    private String overTimeNumTotal;       //时长费小计:平峰时长+高峰时长
    private String waitingFee;       //        等待费用
    private String waitingMinutes;       //等待时长
    private int    channelDiscountType;    //0固定金额，1折扣，2阶梯优惠
    private double channelDiscountAmount;       //        实际渠道优惠金额
    private double channelFlodAmount;       //优惠固定金额
    private double channelDiscountPercent;       //        渠道折扣
    private List<OtherFeeDetailVO> otherCost;       //其他费用(包含停车费、高速费、机场服务费、食宿费、语音服务费)
    private String timePrice;       //分钟定价
    private double languageServiceFee;       //语言服务费
    private String designatedDriverFee;       //指定司机费，如无该项功能可忽略
    private String reducePrice;       //        减免金额
    private double reductionPrice;       //减免金额
    private double reductionTotalprice;       //        减免后金额
    private String reductionReason;       //减免原因
    private String reducePriceDesc;       //减免金额描述，可忽略
    private int    reductionPerson;    //        减免人id
    private String reductionDate;       //减免时间，新增字段，可忽略
    private int    estimatedAmount;    //预估金额，忽略
    private String carpool;       //忽略
    private String minHour;       //        忽略
    private String isEndWaitSettle;       //新能源折扣金额，可忽略
    private String energyDiscountAmount;       //        忽略
    private String businessActualPayAmount;       //渠道支付优惠，可忽略
    private String paymentDiscountAmount;       //违约金，可忽略
    private String cancelOrderDamageAmount;       //        忽略
    private String generalActualPayAmount;       //其它支付方式
    private String otherSettleAmount;       //支付方式，可忽略
    private String payType;       //账户支付，可忽略
    private String accountPay;       //支付宝支付，可忽略
    private String aliPay;       //信用卡支付，可忽略
    private String creditPay;       //微信支付，可忽略
    private String wxPay;       //小车图标，可忽略
    private String carImage;       //渠道号，可忽略
    private String channelsNum;       //车型名称，可忽略
    private String groupName;       //服务类型，可忽略
    private String serviceType;       //实际服务司机，可忽略
    private String factDriverId;       //乘客姓名
    private String riderName;       //实际上车时间，可忽略
    private String startDate;       //实际上车地点，返回数据为空，可忽略
    private String startPlace;       //实际下车时间，可忽略
    private String endDate;       //实际下车地点，返回数据为空，可忽略
    private String endPlace;       //是否可以异议：1可以0不可以，可忽略

}
