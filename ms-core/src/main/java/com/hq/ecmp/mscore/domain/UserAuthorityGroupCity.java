package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAuthorityGroupCity {
	
	String vehicle;//交通工具T001   飞机 T101   火车   T201   汽车  T301   轮渡   T999   其他
	String cityName;//城市名字
	String cityId;//城市编号
	List<UserCarAuthority> userCarAuthorityList;
	@ApiModelProperty(value = "行程的实际可用开始时间")
	@JSONField(format = "yyyy-MM-dd")
	Date beginDate;
	@ApiModelProperty(value = "行程的实际可用结束时间")
	@JSONField(format = "yyyy-MM-dd")
	Date endDate;
}
