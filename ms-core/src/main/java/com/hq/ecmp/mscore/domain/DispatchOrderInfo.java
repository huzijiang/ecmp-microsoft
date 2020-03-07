package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class DispatchOrderInfo {
	
		Long orderId;
       	String state;//状态   待派车-S200    已派车-S299   待改派-S201
       	
       	String applyType;//用车类型   公务用车-A001   差旅用车-A002
       	
       	String serverType;//服务类型  接机  送机  市内用车
       	
       	String useCarCity;//用车城市
       	
       	String applyUserName;//申请人
       	
       	String applyUserTel;//申请人手机号
       	
       	String useCarUserName;//乘车人
       	
       	String startSite;//上车地点
       	
       	String endSite;//下车地点
       	
       	Date useCarDate;//用车时间
       	
       	Date endDate;//预计结束时间
       	
       	Long waitMinute;//等待时间  分钟
       	
       	String useCarMode;//自有车- W100   网约车  -W200    多个以"、"分隔
       	
       	
       	String reassignmentApplyReason;//改派申请理由
       	
       	Date auditSuccessDate;//调度单审批通过时间    调度单
       	
       	Date applyReassignmentDate;//申请改派时间     改派单
       	
       
       	String carType;//车型
       	String carLicense;//车牌号
       	
       	String driverName;//驾驶员名字
    	
    	String driverTel;//驾驶员电话号码
       	
       	
       	DispatchDriverInfo DispatchDriverInfo;//驾驶员车辆信息 派车完成后
       	
       	List<SendCarInfo> sendCarInfoList;//派车信息
       	
}
