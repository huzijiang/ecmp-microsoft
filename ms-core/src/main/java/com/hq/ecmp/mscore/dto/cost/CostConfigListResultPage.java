package com.hq.ecmp.mscore.dto.cost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/15 18:36
 * @Version 1.0
 */
@Data
@ApiModel
public class CostConfigListResultPage {
    @ApiModelProperty(value = "总条数")
    private int total;
    @ApiModelProperty(value = "数据集合")
    List<CostConfigListResult> results;
}
