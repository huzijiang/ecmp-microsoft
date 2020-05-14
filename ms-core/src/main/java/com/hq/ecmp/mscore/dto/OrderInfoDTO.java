package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "补单入参model")
public class OrderInfoDTO {

        @ApiModelProperty(value = "申请人")
        private Long userId ;

        @ApiModelProperty(value = "乘车人")
        private Integer  passengers;

        @ApiModelProperty(value = "公司id")
        private Long  companyId;

        @ApiModelProperty(value = "服务类型")
        private String  serviceType;

        @ApiModelProperty(value = "同车人数量")
        private Integer  num;

        @ApiModelProperty(value = "用车方式")
        private String  useCarMode;

        @ApiModelProperty(value = "驾驶员")
        private Long  driverId;

        @ApiModelProperty(value = "车辆")
        private Long  carId;

        @ApiModelProperty(value = "订单状态")
        private String  state;

        @ApiModelProperty(value = "上车时间")
        private Date   upCarTime;

        @ApiModelProperty(value = "上车地点")
        private String   upCarPlace;

        @ApiModelProperty(value = "下车时间")
        private Date downCarTime;

        @ApiModelProperty(value = "上车纬度")
        private Double  upCarActualSetoutLatitude;

        @ApiModelProperty(value = "上车经度")
        private Double  upCarActualSetoutLongitude;

        @ApiModelProperty(value = "上车长地址")
        private String  upCarActualSetoutAddressLong;

        @ApiModelProperty(value = "上车短地址")
        private String  upCarActualSetoutAddress;

        @ApiModelProperty(value = "上车城市名称")
        private String  upCarCityName;

        @ApiModelProperty(value = "上车城市Code")
        private String   upCarCityCode;

        @ApiModelProperty(value = "下车纬度")
        private Double  downCarActualSetoutLatitude;

        @ApiModelProperty(value = "下车经度")
        private Double  downCarActualSetoutLongitude;

        @ApiModelProperty(value = "下车长地址")
        private String  downCarActualSetoutAddressLong;

        @ApiModelProperty(value = "下车短地址")
        private String  downCarActualSetoutAddress;

        @ApiModelProperty(value = "下车城市名称")
        private String  downCarCityName;

        @ApiModelProperty(value = "下车城市Code")
        private String  downCarCityCode;

        @ApiModelProperty(value = "下车地点")
        private String  downCarPlace;

        @ApiModelProperty(value = "行驶里程(公里)")
        private BigDecimal  totalMileage;

        @ApiModelProperty(value = "行驶时长(分钟)")
        private Integer  totalTime;

        @ApiModelProperty(value = "等待时长(分钟)")
        private Integer  waitingTime;

        @ApiModelProperty(value = "总费用")
        private BigDecimal amount;

        @ApiModelProperty(value = "费用详情")
        private String amountDetail;

        @ApiModelProperty(value = "调度员所管理车队的服务城市")
        private List<String> cityCodes;
}
