package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/23 22:55
 */
@Data
public class RegimenDetailVO {


    /**
     * 用车制度类型 A001-公务用车 A002-差旅用车
     */
    @ApiModelProperty(value="用车制度类型",example = "A001-公务用车 A002-差旅用车",position = 1)
    private String regimenType;

    private String regimeName;// 制度名称

    @ApiModelProperty(value="可用车方式，可多选",example = " W100 自有车 W200 网约车",position = 2)
    private String canUseCarMode;// 用车方式 W100 自有车 W200 网约车

    @ApiModelProperty(value="自有车 可用车辆级别 可多选",example = " P001,P002,P003",position = 3)
    private String useCarModeOwnerLevel;// 自有车 可用车辆级别 可多选

    @ApiModelProperty(value="网约车 可用车辆级别 可多选",example = " P001,P002,P003",position = 4)
    private String useCarModeOnlineLevel;// 网约车 可用车辆级别 可多选

    @ApiModelProperty(value="服务类型 限制，可以多个，逗号分隔",example = "1000,2000,3000",position = 5)
    private String serviceType;// 服务类型 2002 预约用车 3003 接机 4004 送机 5005 包车

    @ApiModelProperty(value="可用日期",example = " 0-0 不限  2019.12.12-2019.12.31",position = 6)
    private String allowDate;// 可用日期

    @ApiModelProperty(value="用车时段限制类型",example = "T001: 不限 T002: 工作日/节假日 T003: 自定义",position = 7)
    private String ruleTime;// 用车时段限制类型： T001: 不限 T002: 工作日/节假日 T003: 自定义

    @ApiModelProperty(value="用车城市规则",example = "C001：不限 C002：限制可用城市 C003：限制不可用城市",position = 8)
    private String ruleCity;// C001：不限 C002：限制可用城市 C003：限制不可用城市

    @ApiModelProperty(value="同城限制",example = "Y000 不允许跨域  N111 允许跨域",position = 9)
    private String setoutEqualArrive;// 同城限制 yes 相等 no 不相等 Y000      ：相等---不允许跨域

    @ApiModelProperty(value="是否需要审批",example = "N111 不需要审批  Y000 需要审批",position = 10)
    private String needApprovalProcess;// N111 不需要审批 不限制 Y000 需要审批


   // -----------------------------  差旅制度  ----------------------------------------------

    @ApiModelProperty(value="出差城市用车",example = "Y000 允许 N111 不允许",position = 10)
    private String travelAllowInTravelCityUseCar;// 出差城市用车 Y000 允许 N111 不允许

    @ApiModelProperty(value="是否允许机场接送",example = "Y000 允许 N111 不允许",position = 10)
    private String asAllowAirportShuttle;// 接送服务 是否允许机场接送 Y000 允许 N111 不允许

    @ApiModelProperty(value="出差城市用车 自有车车型级别",example = "P001 公务,P002 行政,P003 商务六座",position = 10)
    private String travelUseCarModeOwnerLevel;// 出差城市用车 自有车车型配置

    @ApiModelProperty(value="出差城市用车 网约车车型级别",example = "P001 公务,P002 行政,P003 商务六座",position = 10)
    private String travelUseCarModeOnlineLevel;// 出差城市用车 网约车配置

    @ApiModelProperty(value="出差城市用车 使用期限",example = "0 不限制 N 天数",position = 10)
    private Integer travelAllowDateRound;// 出差城市用车 使用期限VVV0 不限制 N 天数

    @ApiModelProperty(value="出差城市用车 同城限制",example = " Y000---不允许跨域  N111--允许跨域",position = 10)
    private String travelSetoutEqualArrive;// 出差城市用车 同城限制 Y000 ：相等---不允许跨域 N111

    @ApiModelProperty(value="接送服务 自有车车型级别",example = "P001,P002,P003",position = 10)
    private String asUseCarModeOwnerLevel;// 接送服务 自有车车型配置

    @ApiModelProperty(value="接送服务 网约车车型级别",example = "P001,P002,P003",position = 10)
    private String asUseCarModeOnlineLevel;// 接送服务 网约车车型配置

    @ApiModelProperty(value="接送服务 使用期限",example = "0 不限制 N 天数",position = 10)
    private String asAllowDateRound;// 接送服务 使用期限 0 不限制 N 天数

    @ApiModelProperty(value="接送服务 使用期限",example = "Y000---不允许跨域 N111--允许跨域",position = 10)
    private String asSetoutEqualArrive;// 接送服务 同城限制 Y000 ：相等---不允许跨域 N111 ：不相等--允许跨域


    private Long regimeId;// 制度编号

    private String sceneName;// 场景名称

    private Integer useNum;// 使用人数

    private Integer approveTemplateId;// 审批模板

    private Integer status;// 0-启用 1-禁用

}
