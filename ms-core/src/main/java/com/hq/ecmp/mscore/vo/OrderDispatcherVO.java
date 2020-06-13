package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "地址模型")
public class OrderDispatcherVO {

    private Long orderId;

    private String dispatcherFlag;
    @ApiModelProperty(name = "innerDispatcherName",value = "内部调度")
    private String innerDispatcherName;

    @ApiModelProperty(name = "innerDispatcherPhone",value = "内部调度电话")
    private String innerDispatcherPhone;

    @ApiModelProperty(name = "otherDispatcherPhone",value = "外部调度手机")
    private String otherDispatcherPhone;

    @ApiModelProperty(name = "otherDispatcherName",value = "外部调度员")
    private String otherDispatcherName;

}
