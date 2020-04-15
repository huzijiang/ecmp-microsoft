package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_heartbeat_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarHeartbeatInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String carHeartbeatId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String attribute9;

    public void setCarHeartbeatId(String carHeartbeatId)
    {
        this.carHeartbeatId = carHeartbeatId;
    }

    public String getCarHeartbeatId()
    {
        return carHeartbeatId;
    }
    public void setAttribute9(String attribute9)
    {
        this.attribute9 = attribute9;
    }

    public String getAttribute9()
    {
        return attribute9;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carHeartbeatId", getCarHeartbeatId())
            .append("attribute9", getAttribute9())
            .toString();
    }
}
