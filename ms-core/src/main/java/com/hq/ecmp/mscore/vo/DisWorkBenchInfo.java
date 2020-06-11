package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>调度工作台出参</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 15:13
 */
@Data
@ApiModel(value = "调度工作台信息model")
public class DisWorkBenchInfo {

    @ApiModelProperty(value = "调度工作台订单列表")
    List<DisWorkBenchOrder> disWorkbenchOrders;

    @ApiModelProperty(value = "调度工作台车辆列表")
    List<DisWorkBenchCar> disWorkBenchCars;

    @ApiModelProperty(value = "列表总数")
    Long total;

    @ApiModelProperty(value = "各状态数据数量")
    List<DisOrderStateCount> stateCount;


}
