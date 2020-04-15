package com.hq.ecmp.mscore.domain;

import java.util.Date;

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
    

    public void setPowerId(Long powerId)
    {
        this.powerId = powerId;
    }

    public Long getPowerId()
    {
        return powerId;
    }
    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public Long getApplyId()
    {
        return applyId;
    }
    public void setNodeId(Long nodeId)
    {
        this.nodeId = nodeId;
    }

    public Long getNodeId()
    {
        return nodeId;
    }
    public void setJourneyId(Long journeyId)
    {
        this.journeyId = journeyId;
    }

    public Long getJourneyId()
    {
        return journeyId;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setItIsReturn(String itIsReturn)
    {
        this.itIsReturn = itIsReturn;
    }

    public String getItIsReturn()
    {
        return itIsReturn;
    }
    
    

    public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
    
	
	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
    
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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
