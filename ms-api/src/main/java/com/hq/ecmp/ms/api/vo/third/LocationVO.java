package com.hq.ecmp.ms.api.vo.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4 ecmp-proxy.
 */
@Data
@ApiModel(description = "地址模型")
public class LocationVO {

	@ApiModelProperty(name = "shortName", value = "短地址")
	private String shortName;// 长地址
	@ApiModelProperty(name = "address", value = "长地址")
	private String address;// 短地址
	@ApiModelProperty(name = "province", value = "省份")
	private String province;
	@ApiModelProperty(name = "fullName", value = "完整地址")
	private String fullName;
	@ApiModelProperty(name = "poi", value = "经纬度 【经度,纬度】")
	private String poi;
	@ApiModelProperty(name = "cityName", value = "市名")
	private String cityName;
	@ApiModelProperty(name = "lat", value = "纬度")
	private String lat;// 纬度
	@ApiModelProperty(name = "lng", value = "经度")
	private String lng;// 经度

}
