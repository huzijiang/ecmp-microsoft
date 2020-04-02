package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "发票账期关联模型")
public class InvoicePeriodDTO {
       @ApiModelProperty(value = "发票ID")
       private Long invoiceId;
       @ApiModelProperty(value = "账期ID")
       private Long periodId;




}
