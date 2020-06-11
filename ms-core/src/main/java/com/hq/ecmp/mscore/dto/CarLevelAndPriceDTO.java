package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shuxiao
 * @date 2020/6/10
 */
@Data
public class CarLevelAndPriceDTO {

	@ApiModelProperty(name = "cityId", value = "城市code",required = true)
	private Integer cityId;
	@ApiModelProperty(name = "bookingDate", value = "用车时间戳（秒）",required = true)
	private String bookingDate;
	@ApiModelProperty(name = "bookingStartAddr", value = "出发地")
	private String bookingStartAddr;
	@ApiModelProperty(name = "bookingStartPointLo", value = "出发地经度",required = true)
	private String bookingStartPointLo;
	@ApiModelProperty(name = "bookingStartPointLa", value = "出发地纬度",required = true)
	private String bookingStartPointLa;
	@ApiModelProperty(name = "bookingEndAddr", value = "目的地",required = false)
	private String bookingEndAddr;
	@ApiModelProperty(name = "bookingEndPointLo", value = "目的地经度",required = true)
	private String bookingEndPointLo;
	@ApiModelProperty(name = "bookingEndPointLa", value = "目的地纬度",required = true)
	private String bookingEndPointLa;

	/**
	 * 服务类型
	 * 1000  即时用车
	 * 2000  预约用车
	 * 3000  接机
	 * 4000  送机
	 * 5000  包车
	 */
	@ApiModelProperty(name = "serviceType", value = "服务类型 即时用车1000,预约2000，接机3000,送机4000",example = "1000",required = true)
	private Integer serviceType;
	@ApiModelProperty(name = "regimenId", value = "用车制度id",required = false)
	private Long regimenId;

	@ApiModelProperty(name = "groups",value = "车型级别",example = "P001,P002",required = false)
	private String groups;

	@ApiModelProperty(name = "itIsReturn",value = "是否往返",required = false)
	private String itIsReturn;

	@ApiModelProperty(name = "type",value = "权限类别 C001  接机\n" +
			"C009  送机\n" +
			"C222  市内用车",required = false)
	private String type;
}
