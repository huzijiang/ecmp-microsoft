package com.hq.ecmp.ms.api.vo.threeparty;

import lombok.Data;

@Data
public class LocationInfoVo {

	private String longAddress;//长地址
	
	private String address;//短地址
	
	private String latitude;//纬度
	
	private String longitude;//经度
	
	public LocationInfoVo (){};
	
	public LocationInfoVo (String longAddress,String address,String latitude,String longitude){
		this.longAddress=longAddress;
		this.address=address;
		this.latitude=latitude;
		this.longitude=longitude;
	}
}
