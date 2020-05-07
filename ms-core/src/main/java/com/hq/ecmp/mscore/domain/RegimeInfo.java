package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.pagehelper.util.StringUtil;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import com.hq.ecmp.constant.CarConstant;

import java.math.BigDecimal;

/**
 * 【请填写功能名称】对象 regime_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegimeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long regimenId;

    private Long companyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long templateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long approveTemplateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String regimenType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String canUseCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowCity;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ruleTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ruleCity;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String setoutEqualArrive;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarModeOwnerLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String needApprovalProcess;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String useCarModeOnlineLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelAllowInTravelCityUseCar;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelUseCarModeOwnerLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelUseCarModeOnlineLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long travelAllowDateRound;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelSetoutEqualArrive;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String asAllowAirportShuttle;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String asUseCarModeOwnerLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String asUseCarModeOnlineLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long asAllowDateRound;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String asSetoutEqualArrive;

    /** Y000-生效中         N111-已失效 */
    @Excel(name = "Y000-生效中         N111-已失效")
    private String state;

    /**************************二期添加字段*******************************/
    /**
     * 成本中心
     * C000 公司付费
     * D000 部门付费
     * P000 项目付费
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String costCenter;
    /**
     * 公务限额类型(此字段仅网约车使用，和字段limit_money一起使用)
     * T000 不限
     * T001 按天
     * T002 按次数
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String limitType;
    /**
     * 公务限额金额（：元）
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal limitMoney;
    /**
     * 差旅市内用车限额类型(此字段仅网约车使用，和字段travel_city_use_car_limit_money一起使用)
     * T000 不限
     * T001 按天
     * T002 按次数
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelCityUseCarLimitType;


    /**
     * 差旅市内用车限额金额(:元)
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal travelCityUseCarLimitMoney;

    /**
     * 差旅接送机限额类型(此字段仅网约车使用，和字段travel_limit_money一起使用)
     * T000 不限
     * T001 按天
     * T002 按次数
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String travelLimitType;

    /**
     * 差旅接送机限额金额(:元)
     */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private BigDecimal travelLimitMoney;


    /**************************二期添加字段*******************************/


    public String parseApplyType(){
    	if(StringUtil.isEmpty(this.regimenType)){
    		return "";
    	}
    	if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(this.regimenType)){
    		return "公务用车";
    	}
    	if(CarConstant.USE_CAR_TYPE_TRAVEL.equals(this.regimenType)){
    		return "差旅用车";
    	}
    	return "未知类型的用车";
    }

    public RegimeInfo(Long approveTemplateId) {
        this.approveTemplateId = approveTemplateId;
    }
}
