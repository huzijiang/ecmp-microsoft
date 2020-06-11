package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shuxiao
 * @date 2020/6/10
 */
@Data
@ApiModel(description = "航班服务模型")
public class FlightInfoDTO {
	@ApiModelProperty(name = "flightCode",value = "航班号")
	private String flightCode;
	@ApiModelProperty(name = "flightDate",value = "航班时间")
	private String flightDate;
}
