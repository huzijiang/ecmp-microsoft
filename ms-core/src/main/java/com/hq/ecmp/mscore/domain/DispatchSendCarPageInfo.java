package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DispatchSendCarPageInfo {
	Long orderId;
	
	Long journeyId;
	
	String applyUserName;
	
	String applyUserMobile;
    
	String useCarUser;// 乘车人

	Integer peerUserNum;// 同行人人数
	
	List<String> peerUserList;//同行人
	
	Date startDate;// 开始时间
	
	Date endDate;// 结束时间
	
	String setOutAderess;// 上车地址
	
	String arriveAdress;// 下车地址
	
	String cityName;// 用车城市
	
	String itIsReturn;//是否往返
	
	String serviceType;// 服务类型
	
	String useCarMode;//可用车方式
	
	Long waitTime;//预计等待时间
	
	DispatchOptRecord oldDispatchOptRecord;//原有调度信息
	
	DispatchOptRecord  currentDispatchOptRecord;//当前调度信息
}
