package com.hq.ecmp.ms.api.dto.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shuxiao
 * @date 2020/6/10
 */
@Data
@ApiModel(description = "地址模型")
public class LocationDTO {
	@ApiModelProperty(name = "shortAddress", value = "短地址")
	private String shortAddress;// 长地址
	@ApiModelProperty(name = "cityName", value = "市名")
	private String cityName;// 市名
	@ApiModelProperty(name = "cityId", value = "城市code")
	private String cityId;// 城市code
}
