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
@ApiModel(description = "网约车车型模型")
public class ThridCarTypeVo {

    @ApiModelProperty(name = "description",value = "")
    private String description;
    @ApiModelProperty(name = "name",value = "")
    private String name;
    @ApiModelProperty(name = "remark",value = "车型照片")
    private String remark;
    @ApiModelProperty(name = "value",value = "车型等级")
    private String value;
}
