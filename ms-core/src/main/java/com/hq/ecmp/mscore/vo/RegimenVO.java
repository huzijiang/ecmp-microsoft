package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/2/27 10:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegimenVO {

    /**
     * 用车制度ID
     */
    private Integer regimenId;

    /**
     * 用车制度模板ID
     */
    private Integer templateId;  //TODO 新增

    /**
     * 用车制度名称
     */
    private String name;

    /**
     * 用车制度图标
     */
    @ApiModelProperty(name = "icon",value = "用车制度图标")
    private String icon;    //TODO 数据库表中暂无此字段

    /**
     * 用车方式 eg 自有车、网约车
     */
    @ApiModelProperty(name = "carType",value = "用车方式 eg 自有车、网约车")
    private String canUseCarMode;

    @ApiModelProperty(name = "regimenType",value = "A001-公务用车  A002-差旅用车")
    private String regimenType; //TODO 新增

    //------------------------ 新增返回 --------------
    @ApiModelProperty(value="服务类型 限制，可以多个，逗号分隔",example = "1000,2000,3000")
    private String serviceType;// 服务类型  服务类型 限制，可以多个，逗号分隔网约车 没有包车 1000  即时用车 2000  预约用车 3000  接机 4000  送机 5000  包车



    @ApiModelProperty(value="是否需要审批",example = "N111 不需要审批  Y000 需要审批")
    private String needApprovalProcess;// N111 不需要审批 不限制 Y000 需要审批

    @ApiModelProperty(value="是否允许出差城市用车",example = "Y000 允许 N111 不允许")
    private String travelAllowInTravelCityUseCar;// 出差城市用车 Y000 允许 N111 不允许


    @ApiModelProperty(value="第一个审批节点是否为项目负责人")
    private boolean firstOpproveNodeTypeIsProjectLeader;

    // @ApiModelProperty(value="可用日期",example = " 0-0 不限  2019.12.12-2019.12.31",position = 6)
    // private String allowDate;// 可用日期

    // @ApiModelProperty(value="用车时段限制类型",example = "T001: 不限 T002: 工作日/节假日 T003: 自定义",position = 7)
    // private String ruleTime;// 用车时段限制类型： T001: 不限 T002: 工作日/节假日 T003: 自定义

    // @ApiModelProperty(value="用车城市规则",example = "C001：不限 C002：限制可用城市 C003：限制不可用城市",position = 8)
    // private String ruleCity;// C001：不限 C002：限制可用城市 C003：限制不可用城市

    // @ApiModelProperty(value="同城限制",example = "Y000 不允许跨域  N111 允许跨域",position = 9)
    // private String setoutEqualArrive;// 同城限制 yes 相等 no 不相等 Y000      ：相等---不允许跨域
}
