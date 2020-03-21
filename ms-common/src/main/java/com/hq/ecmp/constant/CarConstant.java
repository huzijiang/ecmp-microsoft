package com.hq.ecmp.constant;

public interface CarConstant {
    String 	USE_CAR_TYPE_OFFICIAL="T001";//公务
	
	String USE_CAR_TYPE_TRAVEL="T002";//差旅
	
	String NOT_USER_USE_CAR="U000";//未使用
	
	String YES_USER_USE_CAR="U999";//已使用
	
	String USE_CAR_AIRPORT_PICKUP="C001";//接机
	
	String USE_CAR_AIRPORT_DROP_OFF="C009";//送机
	
	String CITY_USE_CAR="C222";//市内用车
	
	String OUTWARD_VOYAGE="T001";//去程
	
	String BACK_TRACKING="T009";//返程

	/*********************************用车方式*********************************************************/
	static  final String USR_CARD_MODE_NET = "W200"; //网约车
	static  final  String  USR_CARD_MODE_HAVE = "W100";//自有车
	/**********************************用车方式********************************************************/

	String START_UP_CAR_GROUP = "Y000";   //启用车队
	String DISABLE_CAR_GROUP = "N111";   // 禁用车队

	String START_CAR = "S000";  // 启用中
	String DISABLE_CAR = "S001";  //禁用中
	String MAINTENANCE_CAR = "S002"; //维护中
	String TIME_OUT_CAR = "SOO3";  //已到期
	String BE_BORROWED_CAR = "S101"; // 被借调

	String START_CAR_TYPE = "S000";  //车型 有效
	String DISABLE_CAR_TYPE = "S444";  // 车型 失效

}
