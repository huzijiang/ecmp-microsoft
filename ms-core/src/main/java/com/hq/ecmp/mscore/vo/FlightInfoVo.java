package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caobj
 * @date 2020/3/3
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "航班模型")
public class FlightInfoVo {

    @ApiModelProperty(name = "date",value = "时间")
    private String date;
    @ApiModelProperty(name = "dstTimezone",value = "落地时区")
    private String dstTimezone;
    @ApiModelProperty(name = "fcategory",value = "航班属性（0:国内-国内;1国内-国际;2国内-地区;3:地区-国际;4:国际-国际;5:未知)")
    private String fcategory;
    @ApiModelProperty(name = "flightArr",value = "到达城市名")
    private String flightArr;
    @ApiModelProperty(name = "flightDepcode",value = "出发城市三字码")
    private String flightDepcode;
    @ApiModelProperty(name = "flightArrcode",value = "到达城市三字码")
    private String flightArrcode;
    @ApiModelProperty(name = "flightArrtimeDate",value = "实际落地时间")
    private String flightArrtimeDate;
    @ApiModelProperty(name = "flightArrAirport",value = "到达机场名")
    private String flightArrAirport;
    @ApiModelProperty(name = "flightArr",value = "计划落地时间")
    private String flightArrtimePlanDate;
    @ApiModelProperty(name = "flightArrtimeReadyDate",value = "预计落地时间")
    private String flightArrtimeReadyDate;
    @ApiModelProperty(name = "flightCompany",value = "航空公司名称")
    private String flightCompany;
    @ApiModelProperty(name = "flightDep",value = "出发地")
    private String flightDep;
    @ApiModelProperty(name = "flightArrtimeReadyDate",value = "出发机场")
    private String flightDepAirport;
    @ApiModelProperty(name = "flightDeptimeDate",value = "实际起飞时间")
    private String flightDeptimeDate;
    @ApiModelProperty(name = "flightDeptimePlanDate",value = "计划起飞时间")
    private String flightDeptimePlanDate;
    @ApiModelProperty(name = "flightDeptimeReadyDate",value = "预计起飞时间")
    private String flightDeptimeReadyDate;
    @ApiModelProperty(name = "flightHTerminal",value = "候机楼")
    private String flightHTerminal;
    @ApiModelProperty(name = "flightNo",value = "航班号")
    private String flightNo;
    @ApiModelProperty(name = "flightState",value = "航班状态（计划，起飞，到达，延误，取消，备降，返航）")
    private String flightState;
    @ApiModelProperty(name = "flightTerminal",value = "航机楼")
    private String flightTerminal;
    @ApiModelProperty(name = "from",value = "航班信息来源，1 非常准 2 航旅纵横")
    private Integer from;
    @ApiModelProperty(name = "orgTimezone",value = "起飞时区")
    private String orgTimezone;
    @ApiModelProperty(name = "orgTimezone",value = "起飞时区")
    private String longitude;
    @ApiModelProperty(name = "orgTimezone",value = "起飞时区")
    private String latitude;

    public FlightInfoVo() {
    }

    public FlightInfoVo(String flightArrAirport, String flightArrtimePlanDate, String flightDepAirport, String flightNo) {
        this.flightArrAirport = flightArrAirport;
        this.flightArrtimePlanDate = flightArrtimePlanDate;
        this.flightDepAirport = flightDepAirport;
        this.flightNo = flightNo;
    }
}
