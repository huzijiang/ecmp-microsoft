package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/9 14:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCarDataVo {

    @ApiModelProperty(value = "用车单位（用户所在部门）")
    private String deptName;
    @ApiModelProperty(value = "累计用车次数")
    private Integer totalTimes;
    @ApiModelProperty(value = "累计用车费用（订单结算表的amount总费用字段）")
    BigDecimal totalFee;
    @ApiModelProperty(value = "累计用车天数")
    private String totalDays;
}
