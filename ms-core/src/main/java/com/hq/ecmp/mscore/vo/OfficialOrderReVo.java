package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OfficialOrderReVo
 * @Description TODO
 * @Author yj
 * @Date 2020/3/20 11:55
 * @Version 1.0
 */
@Data
@ApiModel("公务用车入参model")
public class OfficialOrderReVo {
    @ApiModelProperty(value = "权限id",required = true)
    private Long powerId;
    @ApiModelProperty(value = "1.走调度（包含自有车）  2 直接约车（只有网约车或城市没有自有车车队）",required = true)
    private Integer isDispatch;
    @ApiModelProperty(value = "车型，<直接约车时必填，即isDispatch为2时必填>多个用逗号分隔。P001-公务级，P002-行政级，P003-六座商务。",required = false)
    private String carLevel;

    public OfficialOrderReVo() {
    }

    public OfficialOrderReVo(Long powerId, Integer isDispatch, String carLevel) {
        this.powerId = powerId;
        this.isDispatch = isDispatch;
        this.carLevel = carLevel;
    }
}
