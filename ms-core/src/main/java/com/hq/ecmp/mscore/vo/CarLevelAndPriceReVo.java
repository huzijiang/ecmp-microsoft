package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName CarLevelAndPriceReVo
 * @Description TODO
 * @Author yj
 * @Date 2020/3/29 12:13
 * @Version 1.0
 */
@Data
public class CarLevelAndPriceReVo {

    @ApiModelProperty(name = "onlineCarLevel",value = "车型",example = "P001")
    private String onlineCarLevel;
    @ApiModelProperty(name = "estimatePrice",value = "预估价格")
    private Integer estimatePrice;
    @ApiModelProperty(name = "duration",value = "预估用时")
    private Integer duration;
    @ApiModelProperty(name = "source",value = "平台来源")
    private String source;
    @ApiModelProperty(name = "bookingStartTime",value = "开始时间")
    private Date bookingStartTime;
}
