package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/5 11:13
 */
@Data
public class TripDescription {


    /**
     * 差旅行程开始时间
     */
    @ApiModelProperty(name = "startDate",value = "差旅行程开始时间")
    private Date startDate;
    /**
     * 差旅行程结束时间
     */
    @ApiModelProperty(name = "endDate",value = "差旅行程结束时间")
    private Date endDate;
    /**
     * 市内用车城市 eg:上海、南京
     */
    @ApiModelProperty(name = "tripCity",value = "市内用车城市 eg:上海、南京")
    private String tripCity;

    /**
     * 接送机次数及各个城市的次数
     * eg 共6次
     *    上海 接送服务各一次
     *    南京 接送服务各一次
     */
  //  @ApiModelProperty(name = "tripDesc",value = "接送机次数及各个城市的次数")
   // private String tripDesc;

    /**
     * 接送机次数及各个城市的次数
     * eg 共6次
     *    上海 接送服务各一次
     *    南京 接送服务各一次
     */
    @ApiModelProperty(name = "tripDesc",value = "接送机城市及各个城市的接送次数")
    private List<String> tripDesc;

    /**
     * 行程列表 显示差旅中的每一小段行程
     * eg 行程①：北京-上海
     *    行程②：上海-南京
     *    行程③：南京-北京
     */
    @ApiModelProperty(name = "trips",value = "行程列表 显示差旅中的每一小段行程")
    private List<String> trips;

    /**
     * 接送机总次数
     *
     */
    @ApiModelProperty(name = "pickupTimes",value = "接送机总次数")
    private Integer pickupTimes;  //TODO 新增

}
