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
}
