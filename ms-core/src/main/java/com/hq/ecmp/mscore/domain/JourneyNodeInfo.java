package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 journey_node_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class JourneyNodeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long nodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planBeginAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planEndAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date planSetoutTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date planArriveTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planBeginLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planBeginLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planEndLongitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planEndLatitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsViaPoint;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String vehicle;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String duration;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String distance;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String waitDuration;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String nodeState;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer number;
    

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planBeginLongAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planEndLongAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planBeginCityCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String planEndCityCode;

    public String getPlanBeginCityCode() {
        return planBeginCityCode;
    }

    public void setPlanBeginCityCode(String planBeginCityCode) {
        this.planBeginCityCode = planBeginCityCode;
    }

    public String getPlanEndCityCode() {
        return planEndCityCode;
    }

    public void setPlanEndCityCode(String planEndCityCode) {
        this.planEndCityCode = planEndCityCode;
    }

    public String getPlanEndLongAddress() {
        return planEndLongAddress;
    }

    public void setPlanEndLongAddress(String planEndLongAddress) {
        this.planEndLongAddress = planEndLongAddress;
    }

    public String getPlanBeginLongAddress() {
        return planBeginLongAddress;
    }

    public void setPlanBeginLongAddress(String planBeginLongAddress) {
        this.planBeginLongAddress = planBeginLongAddress;
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
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setPlanBeginAddress(String planBeginAddress)
    {
        this.planBeginAddress = planBeginAddress;
    }

    public String getPlanBeginAddress()
    {
        return planBeginAddress;
    }
    public void setPlanEndAddress(String planEndAddress)
    {
        this.planEndAddress = planEndAddress;
    }

    public String getPlanEndAddress()
    {
        return planEndAddress;
    }
    public void setPlanSetoutTime(Date planSetoutTime)
    {
        this.planSetoutTime = planSetoutTime;
    }

    public Date getPlanSetoutTime()
    {
        return planSetoutTime;
    }
    public void setPlanArriveTime(Date planArriveTime)
    {
        this.planArriveTime = planArriveTime;
    }

    public Date getPlanArriveTime()
    {
        return planArriveTime;
    }
    public void setPlanBeginLongitude(String planBeginLongitude)
    {
        this.planBeginLongitude = planBeginLongitude;
    }

    public String getPlanBeginLongitude()
    {
        return planBeginLongitude;
    }
    public void setPlanBeginLatitude(String planBeginLatitude)
    {
        this.planBeginLatitude = planBeginLatitude;
    }

    public String getPlanBeginLatitude()
    {
        return planBeginLatitude;
    }
    public void setPlanEndLongitude(String planEndLongitude)
    {
        this.planEndLongitude = planEndLongitude;
    }

    public String getPlanEndLongitude()
    {
        return planEndLongitude;
    }
    public void setPlanEndLatitude(String planEndLatitude)
    {
        this.planEndLatitude = planEndLatitude;
    }

    public String getPlanEndLatitude()
    {
        return planEndLatitude;
    }
    public void setItIsViaPoint(String itIsViaPoint)
    {
        this.itIsViaPoint = itIsViaPoint;
    }

    public String getItIsViaPoint()
    {
        return itIsViaPoint;
    }
    public void setVehicle(String vehicle)
    {
        this.vehicle = vehicle;
    }

    public String getVehicle()
    {
        return vehicle;
    }
    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getDuration()
    {
        return duration;
    }
    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public String getDistance()
    {
        return distance;
    }
    public void setWaitDuration(String waitDuration)
    {
        this.waitDuration = waitDuration;
    }

    public String getWaitDuration()
    {
        return waitDuration;
    }
    public void setNodeState(String nodeState)
    {
        this.nodeState = nodeState;
    }

    public String getNodeState()
    {
        return nodeState;
    }
    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public Integer getNumber()
    {
        return number;
    }

    public JourneyNodeInfo() {
    }

    public JourneyNodeInfo(Long journeyId, Long userId) {
        this.journeyId = journeyId;
        this.userId = userId;
    }

    public JourneyNodeInfo(Long journeyId, String itIsViaPoint) {
        this.journeyId = journeyId;
        this.itIsViaPoint = itIsViaPoint;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("nodeId", getNodeId())
            .append("journeyId", getJourneyId())
            .append("userId", getUserId())
            .append("planBeginAddress", getPlanBeginAddress())
            .append("planEndAddress", getPlanEndAddress())
            .append("planSetoutTime", getPlanSetoutTime())
            .append("planArriveTime", getPlanArriveTime())
            .append("planBeginLongitude", getPlanBeginLongitude())
            .append("planBeginLatitude", getPlanBeginLatitude())
            .append("planEndLongitude", getPlanEndLongitude())
            .append("planEndLatitude", getPlanEndLatitude())
            .append("itIsViaPoint", getItIsViaPoint())
            .append("vehicle", getVehicle())
            .append("duration", getDuration())
            .append("distance", getDistance())
            .append("waitDuration", getWaitDuration())
            .append("nodeState", getNodeState())
            .append("number", getNumber())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
