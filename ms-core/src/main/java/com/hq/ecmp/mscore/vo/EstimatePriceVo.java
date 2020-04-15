package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName EstimatePriceVo
 * @Description TODO
 * @Author yj
 * @Date 2020/3/29 0:10
 * @Version 1.0
 */
@Data
public class EstimatePriceVo {
    @ApiModelProperty(name = "cityId", value = "城市code",required = true)
    private String cityId;
    @ApiModelProperty(name = "bookingDate", value = "用车时间戳（秒）",required = true)
    private String bookingDate;
    @ApiModelProperty(name = "bookingStartAddr", value = "出发地")
    private String bookingStartAddr;
    @ApiModelProperty(name = "bookingStartPointLo", value = "出发地经度",required = true)
    private String bookingStartPointLo;
    @ApiModelProperty(name = "bookingStartPointLa", value = "出发地纬度",required = true)
    private String bookingStartPointLa;
    @ApiModelProperty(name = "bookingEndAddr", value = "目的地",required = false)
    private String bookingEndAddr;
    @ApiModelProperty(name = "bookingEndPointLo", value = "目的地经度",required = true)
    private String bookingEndPointLo;
    @ApiModelProperty(name = "bookingEndPointLa", value = "目的地纬度",required = true)
    private String bookingEndPointLa;

    /**
     * 服务类型
     * 1000  即时用车
     * 2000  预约用车
     * 3000  接机
     * 4000  送机
     * 5000  包车
     */
    @ApiModelProperty(name = "serviceType", value = "服务类型 即时用车1000,预约2000，接机3000,送机4000",example = "1000",required = true)
    private Integer serviceType;
    @ApiModelProperty(name = "groups",value = "车型级别",example = "P001,P002",required = false)
    private String groups;
}
