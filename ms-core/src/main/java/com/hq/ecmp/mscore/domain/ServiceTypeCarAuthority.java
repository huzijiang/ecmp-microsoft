package com.hq.ecmp.mscore.domain;

import lombok.Data;

@Data
public class ServiceTypeCarAuthority {
	   String cityName;//城市名称
	   String type;//C001  接机    C009  送机     C222  市内用车
	   Integer surplusCount;//剩余服务次数
	   String state;//状态   去申请/派车中/约车中/待服务/进行中/待确认/已完成
	   
	  public ServiceTypeCarAuthority(){};
	  public ServiceTypeCarAuthority(String cityName,Integer surplusCount, String state){
		  this.cityName=cityName;
		  this.surplusCount=surplusCount;
		  this.state=state;
	  }
}
