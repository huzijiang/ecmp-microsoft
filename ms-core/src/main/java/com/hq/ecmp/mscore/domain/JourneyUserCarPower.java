package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 journey_user_car_power
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class JourneyUserCarPower extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long powerId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long applyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long nodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsReturn;

    private String cityName;

    private Long createUser;

    private Date createDate;
    public JourneyUserCarPower(){};

    public JourneyUserCarPower(Long applyId,Long journeyId,Date createDate,Long createUser,String state,String type,Long nodeId){
    	this.applyId=applyId;
    	this.journeyId=journeyId;
    	this.createUser=createUser;
    	this.createDate=createDate;
    	this.state=state;
    	this.type=type;
    	this.nodeId=nodeId;
    }

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("powerId", getPowerId())
            .append("applyId", getApplyId())
            .append("nodeId", getNodeId())
            .append("journeyId", getJourneyId())
            .append("state", getState())
            .append("type", getType())
            .append("itIsReturn", getItIsReturn())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
