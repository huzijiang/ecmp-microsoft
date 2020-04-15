package com.hq.ecmp.ms.api.vo.threeparty;

import lombok.Data;

@Data
public class LocationInfoVo {

	private String longAddress;//长地址
	
	private String address;//短地址
	
	private String lat;//纬度
	
	private String lng;//经度
	
	public LocationInfoVo (){};

	public LocationInfoVo(String lat, String lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public LocationInfoVo (String longAddress, String address, String lat, String lng){
		this.longAddress=longAddress;
		this.address=address;
		this.lat=lat;
		this.lng=lng;
	}
}
