package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 参数配置对象 ecmp_config
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    private Integer configId;

    /** 参数名称 */
    @Excel(name = "参数名称")
    private String configName;

    /** 参数键名 */
    @Excel(name = "参数键名")
    private String configKey;

    /** 参数键值 */
    @Excel(name = "参数键值")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Excel(name = "系统内置", readConverterExp = "Y=是,N=否")
    private String configType;

    public void setConfigId(Integer configId)
    {
        this.configId = configId;
    }

    public Integer getConfigId()
    {
        return configId;
    }
    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    public String getConfigName()
    {
        return configName;
    }
    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getConfigKey()
    {
        return configKey;
    }
    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigValue()
    {
        return configValue;
    }
    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    public String getConfigType()
    {
        return configType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
