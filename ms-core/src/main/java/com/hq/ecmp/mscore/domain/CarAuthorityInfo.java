package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用车权限
 * @author cm
 *
 */
@Data
public class CarAuthorityInfo {
		
	  Long journeyId;//行程编号
       String type;//差旅/公务
       List<String> cityName;
       Date startDate;
       Date endDate;
       Integer joinCount;//接机次数
       Integer giveCount;//送机次数
       Integer cityCount;//出差城市用车次数
       
       String applyName;//公务出差理由
       String useDate;//公务用车时间
       String carType;//用车方式  自有车or网约车
       String status;//状态 去申请/派车中/约车中/待服务/进行中/待确认/已完成
       
}
