package com.hq.ecmp.mscore.bo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 车辆轨迹信息对象 car_track_info
 * 
 * @author hqer
 * @date 2020-06-02
 */
public class CarTrackInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 车架号 */
    @Excel(name = "车架号")
    private String vin;

    /** 经度 */
    @Excel(name = "经度")
    private Double lon;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double lat;

    /** 最新定位时间 */
    @Excel(name = "最新定位时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastRefreshTime;

    /** 修改时间 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVin(String vin) 
    {
        this.vin = vin;
    }

    public String getVin() 
    {
        return vin;
    }
    public void setLon(Double lon) 
    {
        this.lon = lon;
    }

    public Double getLon() 
    {
        return lon;
    }
    public void setLat(Double lat) 
    {
        this.lat = lat;
    }

    public Double getLat() 
    {
        return lat;
    }
    public void setLastRefreshTime(Date lastRefreshTime) 
    {
        this.lastRefreshTime = lastRefreshTime;
    }

    public Date getLastRefreshTime() 
    {
        return lastRefreshTime;
    }
    public void setUpdateDate(Date updateDate) 
    {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() 
    {
        return updateDate;
    }
    public void setCreateDate(Date createDate) 
    {
        this.createDate = createDate;
    }

    public Date getCreateDate() 
    {
        return createDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vin", getVin())
            .append("lon", getLon())
            .append("lat", getLat())
            .append("lastRefreshTime", getLastRefreshTime())
            .append("updateDate", getUpdateDate())
            .append("createDate", getCreateDate())
            .toString();
    }
}