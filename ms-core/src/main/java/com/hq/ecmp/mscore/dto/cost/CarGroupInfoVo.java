package com.hq.ecmp.mscore.dto.cost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/25 15:55
 * @Version 1.0
 */
@Data
@ApiModel
public class CarGroupInfoVo {
    @ApiModelProperty(value = "车队id")
    private Long carGroupId;
    @ApiModelProperty(value = "车队名字")
    private String carGroupName;
    @ApiModelProperty(value = "价格集合")
    private List<ApplyPriceDetails> priceDetails;
}
