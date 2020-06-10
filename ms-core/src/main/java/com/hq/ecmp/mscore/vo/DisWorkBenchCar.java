package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>调度工作台数据</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 14:53
 */
@Data
@ApiModel("调度工作台车辆相关model")
public class DisWorkBenchCar {

    @ApiModelProperty(value = "车型")
    private String carTypeName;

    @ApiModelProperty(value = "座位数")
    private String seatNum;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "车型图片路径")
    private String carTypeImageUrl;
}
