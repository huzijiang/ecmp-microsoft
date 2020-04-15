package com.hq.ecmp.mscore.domain;

import java.util.List;

import lombok.Data;

@Data
public class UserAuthorityGroupCity {
	
	String vehicle;//交通工具T001   飞机 T101   火车   T201   汽车  T301   轮渡   T999   其他
	String cityName;//城市名字
	String cityId;//城市编号
	List<UserCarAuthority> userCarAuthorityList;
}
