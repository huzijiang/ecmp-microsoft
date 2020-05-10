package com.hq.ecmp.mscore.domain;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;


@Data
public class RegimeVo {

	Long regimeId;// 制度编号

	/**
	 * 用车制度类型 A001-公务用车 A002-差旅用车
	 */
	String regimenType;

	String regimeName;// 制度名称

	String sceneName;// 场景名称

	Long sceneId;//场景编号

	Integer useNum;// 使用人数

	List<Long> userList;// 可用员工 

	Integer approveTemplateId;// 审批模板

	String state;// E000 停用中    Y000   生效中

	String canUseCarMode;// 用车方式 W100 自有车 W200 网约车

	String needApprovalProcess;// N111 不需要审批 不限制 Y000 需要审批
	
	Integer applyUseNum;//该制度用于多少个申请单

	/**
	 * 公务制度
	 */
	String serviceType;// 服务类型 2002 预约用车 3003 接机 4004 送机 5005 包车

	String allowDate;// 可用日期

	String ruleTime;// 用车时段限制类型： T001: 不限 T002: 工作日/节假日 T003: 自定义

	List<RegimeUseCarTimeRuleInfo>  regimeUseCarTimeRuleInfoList;//用车时间段限制

	String setoutEqualArrive;// 同城限制 yes 相等 no 不相等

	String ruleCity;// C001：不限 C002：限制可用城市 C003：限制不可用城市

	List<String> cityLimitIds;//限制固定城市编号
	List<String> notCityLimitIds;//限制不可用城市编号

	String useCarModeOwnerLevel;// 公务 自有车车型配置

	String useCarModeOnlineLevel;// 公务 网约车车型配置

	/**
	 * 差旅制度
	 */

	String travelAllowInTravelCityUseCar;// 出差城市用车 Y000 允许 N111 不允许

	String travelUseCarModeOwnerLevel;// 出差城市用车 自有车车型配置

	String travelUseCarModeOnlineLevel;// 出差城市用车 网约车配置

	Integer travelAllowDateRound;// 出差城市用车 使用期限VVV0 不限制 N 天数

	String travelSetoutEqualArrive;// 出差城市用车 同城限制 Y000 ：相等---不允许跨域 N111
	// ：不相等--允许跨域

	String asAllowAirportShuttle;// 接送服务 是否允许机场接送 Y000 允许 N111 不允许

	String asUseCarModeOwnerLevel;// 接送服务 自有车车型配置

	String asUseCarModeOnlineLevel;// 接送服务 网约车车型配置

	String asAllowDateRound;// 接送服务 使用期限 0 不限制 N 天数

	String asSetoutEqualArrive;// 接送服务 同城限制 Y000 ：相等---不允许跨域 N111 ：不相等--允许跨域

	//----------------------  二期新增字段 ------------------------------
	/**
	 *成本中心（不为空）
	 * C000 公司付费
	 * D000 部门付费
	 * P000 项目付费
	 */
	String costCenter;

	/**
	 * 公务限额类型(此字段仅网约车使用，和字段limit_money一起使用)
	 *
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	String limitType;

	/**
	 * 公务限额金额(:元)
	 */
	BigDecimal limitMoney;

	/**
	 * 差旅市内用车限额类型(此字段仅网约车使用，和字段travel_city_use_car_limit_money一起使用)
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	String travelCityUseCarLimitType;

	/**
	 * 差旅市内用车限额金额(:元)
	 */
	BigDecimal travelCityUseCarLimitMoney;

	/**
	 * 差旅接送机限额类型(此字段仅网约车使用，和字段travel_limit_money一起使用)
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	String travelLimitType;

	/**
	 * 差旅接送机限额金额(:元)
	 */
	BigDecimal travelLimitMoney;
}
