package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/7 22:15
 * @Version 1.0
 */
@ApiModel("按月司机排班变更入参")
@Data
public class DriverWorkInfoDetailVo {

   @ApiModelProperty(value = "排班变更数据集合")
   List<DriverWorkInfoMonthVo> driverWorkInfoMonthVos;
}
