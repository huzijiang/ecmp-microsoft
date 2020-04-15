package com.hq.ecmp.mscore.bo;

import java.util.Date;

import lombok.Data;

@Data
public class WeatherAndCity {
	private String weather;//天气
	
	private String cityName;//城市
	
	private String cityCode;//城市编号
	
	private String week;//周几
	
	private String monthAndDay;//日期
	
	
	
	private String weatherDescription;
	
	private String city;
}
