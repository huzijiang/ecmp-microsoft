package com.hq.ecmp.mscore.domain;

import java.util.List;

import lombok.Data;

/**
 * 创建制度实体类
 * @author cm
 *
 */
@Data
public class RegimePo {
	/*
	公共
	*/
	String applyType;//公务-A001   差旅-A002
	   
	String name;//制度名称
	
	Long sceneId;//用车场景ID
	
	String canUseCarMode;//用车方式    W001  -自有    W002  -网约车
	
	List<Long> userList;//可用员工
	
	Integer approvalProcess;//  0  不需要审批  不限制    1-需要审批
	
	
	
	
	/*
	公务
	*/
	String serviceType;//服务类型   2002  预约用车   3003  接机    4004  送机    5005  包车
	
    String allowTime;//用车时间段
	
	String allowDate;//可用日期  年月日
	
	String allowCity;//可用城市
	
	String setoutEqualArrive;//同城限制   yes  相等          no  不相等
	
	
	
	
	/**
	 *  自有：W001 豪华型  W002 行程型

	      网约车：
	      T001 舒适性
	       T002 公务型
	 */
	String canUseCarLevel;
	
	
	/*
	差旅
	*/
	
	
}
