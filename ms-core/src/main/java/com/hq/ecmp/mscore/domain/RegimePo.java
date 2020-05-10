package com.hq.ecmp.mscore.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 创建制度实体类
 *
 * @author cm
 */
@Data
public class RegimePo {
    /*
     * 公共
     */
    Long regimenId;// 制度ID

    Long templateId;// 模板ID

    Long companyId; //所属公司

    String regimenType;// 公务-A001 差旅-A002

    String name;// 制度名称

    Long sceneId;// 用车场景ID 是否需要？

    String canUseCarMode;// 用车方式 W001 -自有 W002 -网约车

    List<Long> userList;// 可用员工

    String needApprovalProcess;// N111 不需要审批 不限制 Y000 需要审批

    Long approveTemplateId;// 审批模板

    Long optId;// 操作用户编号

    /*
     * 公务
     */
    String serviceType;// 服务类型 2002 预约用车 3003 接机 4004 送机 5005 包车

    String allowDate;// 可用日期 年月日 不限制 0-0 限制2019.12.12-2019.12.31

    String setoutEqualArrive;// 同城限制 yes 相等 no 不相等

    /**
     * C001：不限 C002：限制可用城市 C003：限制不可用城市
     */
    String ruleCity;

    List<String> cityLimitIds;// 限制城市的编号编号 关联 regime_use_car_city_rule_info表

    /**
     * 用车时段限制类型： T001: 不限    T002: 工作日/节假日   T003: 自定义
     */
    String ruleTime;

    List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfoList;

    String useCarModeOwnerLevel;// 差旅 自有车车型配置

    String useCarModeOnlineLevel;// 差旅 网约车车型配置

    /*
     * 差旅
     */
    String travelAllowInTravelCityUseCar;// 出差城市用车 Y000 允许 N111 不允许

    String travelUseCarModeOwnerLevel;// 出差城市用车 自有车车型配置

    String travelUseCarModeOnlineLevel;// 出差城市用车 网约车配置

    String travelAllowDateRound;// 出差城市用车 使用期限VVV0 不限制 N 天数

    String travelSetoutEqualArrive;// 出差城市用车 同城限制 Y000 ：相等---不允许跨域 N111
    // ：不相等--允许跨域

    String asAllowAirportShuttle;// 接送服务 是否允许机场接送 Y000 允许 N111 不允许

    String asUseCarModeOwnerLevel;// 接送服务 自有车车型配置

    String asUseCarModeOnlineLevel;// 接送服务 网约车车型配置

    String asAllowDateRound;// 接送服务 使用期限 0 不限制 N 天数

    String asSetoutEqualArrive;// 接送服务 同城限制 Y000 ：相等---不允许跨域 N111 ：不相等--允许跨域

    Date createTime;

	/**************************二期添加字段*******************************/
	/**
	 * 成本中心
	 * C000 公司付费
	 * D000 部门付费
	 * P000 项目付费
	 */
	private String costCenter;
	/**
	 * 公务限额类型(此字段仅网约车使用，和字段limit_money一起使用)
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	private String limitType;
	/**
	 * 公务限额金额（：元）
	 */
	private BigDecimal limitMoney;
	/**
	 * 差旅市内用车限额类型(此字段仅网约车使用，和字段travel_city_use_car_limit_money一起使用)
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	private String travelCityUseCarLimitType;

	/**
	 * 差旅市内用车限额金额(:元)
	 */
	private BigDecimal travelCityUseCarLimitMoney;

	/**
	 * 差旅接送机限额类型(此字段仅网约车使用，和字段travel_limit_money一起使用)
	 * T000 不限
	 * T001 按天
	 * T002 按次数
	 */
	private String travelLimitType;

	/**
	 * 差旅接送机限额金额(:元)
	 */
	private BigDecimal travelLimitMoney;


	/**************************二期添加字段*******************************/

}
