package com.hq.ecmp.constant;

public interface CarConstant {
    String 	USE_CAR_TYPE_OFFICIAL="A001";//公务
	
	String USE_CAR_TYPE_TRAVEL="A002";//差旅
	
	String NOT_USER_USE_CAR="U000";//未使用
	
	String YES_USER_USE_CAR="U999";//已使用
	
	String USE_CAR_AIRPORT_PICKUP="C001";//接机
	
	String USE_CAR_AIRPORT_DROP_OFF="C009";//送机
	
	String CITY_USE_CAR="C222";//市内用车
	
	String OUTWARD_VOYAGE="T001";//去程
	
	String BACK_TRACKING="T009";//返程
	
	String ALLOW_USE="Y000";//允许
	
	String NOT_ALLOW_USE="N111";//不允许

	/*********************************用车方式*********************************************************/
	static  final String USR_CARD_MODE_NET = "W200"; //网约车
	static  final  String  USR_CARD_MODE_HAVE = "W100";//自有车
	/**********************************用车方式********************************************************/

	String START_UP_CAR_GROUP = "Y000";   //启用车队
	String DISABLE_CAR_GROUP = "N111";   // 禁用车队
	String DELETE_CAR_GROUP = "S444";    //删除车队

	String START_CAR = "S000";  // 启用中
	String DISABLE_CAR = "S001";  //禁用中
	String MAINTENANCE_CAR = "S002"; //维护中
	String TIME_OUT_CAR = "S003";  //已到期
	String WAIT_START_CAR = "S004"; //待启用
	String BE_BORROWED_CAR = "S101"; // 被借调
	String DELETE_CAR = "S444";    //被删除

	String LOCKED = "1111";   // 车辆被锁定
	String UN_LOCKED = "0000"; // 车辆未被锁定

	String START_CAR_TYPE = "S000";  //车型 有效
	String DISABLE_CAR_TYPE = "S444";  // 车型 失效

	String START_DRIVER_TYPE = "V000"; //驾驶员 有效
	String DISABLE_DRIVER_TYPE = "NV00"; //驾驶员 失效
	String DELETE_DRIVER_TYPE = "S444"; //驾驶员 被删除

	String OWN_CAR = "S001"; //自有车
	String RENT_CAR = "S002"; //租来的车
	String BORROW_CAR = "S003"; //借来的车

	Integer RETURN_ONE_CODE = 1;
	Integer RETURN_ZERO_CODE = 0;
	Integer RETURN_TWO_CODE = 2;

	//交通工具

	String TRAFFIC_AIRCRAFT ="T001";   //飞机
	String TRAFFIC_TRAIN ="T101";   //火车
	String TRAFFIC_AUTOMOBILE = "T201";   //汽车
	String TRAFFIC_FERRY ="T301";   //轮渡
	String TRAFFIC_OTHER ="T999";   //其他

	//订单调度信息表用于表示调度任务是否完成

	String  DISPATCH_NOT_COMPLETED = "D000";   //调度未完成
	String  DISPATCH_YES_COMPLETE  = "D111";      //调度完成
	/**
	 * 内部车队
	 */
	String IT_IS_USE_INNER_CAR_GROUP_IN = "C000";
	/**
	 * 外部车队
	 */
	String IT_IS_USE_INNER_CAR_GROUP_OUT = "C111";
	/**
	 * 自有车使用模式 车和驾驶员都用
	 */
	String CAR_GROUP_USER_MODE_CAR_DRIVER = "CA00";
	/**
	 * 自有车使用模式 仅用车
	 */
	String CAR_GROUP_USER_MODE_CAR = "CA01";
	/**
	 * 自有车使用模式 仅用驾驶员
	 */
	String CAR_GROUP_USER_MODE_DRIVER = "CA10";
	/**
	 * 自有车使用模式 车和驾驶员都不用
	 */
	String CAR_GROUP_USER_MODE_NO = "CA11";

	String SELFDRIVER_YES = "Y000";
	String SELFDRIVER_NO= "N111";
}
