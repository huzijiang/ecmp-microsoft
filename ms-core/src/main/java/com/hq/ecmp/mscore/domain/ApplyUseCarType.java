package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 申请单可用车型关系对象 apply_use_car_type
 * 
 * @author hqer
 * @date 2020-05-07
 */
public class ApplyUseCarType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long applyUseCarTypeId;

    /** 申请单id->apply_info表中的apply_id */
    @Excel(name = "申请单id->apply_info表中的apply_id")
    private Long applyId;

    /** 城市code */
    @Excel(name = "城市code")
    private String cityCode;

    /** 接送机自由车可用车型 */
    @Excel(name = "接送机自由车可用车型")
    private String shuttleOwnerCarType;

    /** 接送机网约车可用车型 */
    @Excel(name = "接送机网约车可用车型")
    private String shuttleOnlineCarType;

    /** 自由车可用车型 */
    @Excel(name = "自由车可用车型")
    private String ownerCarType;

    /** 网约车可用车型 */
    @Excel(name = "网约车可用车型")
    private String onlineCarType;

    public void setApplyUseCarTypeId(Long applyUseCarTypeId) 
    {
        this.applyUseCarTypeId = applyUseCarTypeId;
    }

    public Long getApplyUseCarTypeId() 
    {
        return applyUseCarTypeId;
    }
    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }
    public void setCityCode(String cityCode) 
    {
        this.cityCode = cityCode;
    }

    public String getCityCode() 
    {
        return cityCode;
    }
    public void setShuttleOwnerCarType(String shuttleOwnerCarType) 
    {
        this.shuttleOwnerCarType = shuttleOwnerCarType;
    }

    public String getShuttleOwnerCarType() 
    {
        return shuttleOwnerCarType;
    }
    public void setShuttleOnlineCarType(String shuttleOnlineCarType) 
    {
        this.shuttleOnlineCarType = shuttleOnlineCarType;
    }

    public String getShuttleOnlineCarType() 
    {
        return shuttleOnlineCarType;
    }
    public void setOwnerCarType(String ownerCarType) 
    {
        this.ownerCarType = ownerCarType;
    }

    public String getOwnerCarType() 
    {
        return ownerCarType;
    }
    public void setOnlineCarType(String onlineCarType) 
    {
        this.onlineCarType = onlineCarType;
    }

    public String getOnlineCarType() 
    {
        return onlineCarType;
    }

    public ApplyUseCarType() {
    }

    public ApplyUseCarType(Long applyId, String cityCode) {
        this.applyId = applyId;
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyUseCarTypeId", getApplyUseCarTypeId())
            .append("applyId", getApplyId())
            .append("cityCode", getCityCode())
            .append("shuttleOwnerCarType", getShuttleOwnerCarType())
            .append("shuttleOnlineCarType", getShuttleOnlineCarType())
            .append("ownerCarType", getOwnerCarType())
            .append("onlineCarType", getOnlineCarType())
            .toString();
    }
}