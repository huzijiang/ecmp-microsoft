package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_group_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class CarGroupInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long carGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long parentCarGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String city;  //TODO Long改动

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerOrg;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long leader;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;    //TODO 新增 启用状态

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String telephone;    //TODO 新增 车队座机

    private String carGroupName;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carGroupCode;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String shortAddress;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String fullAddress;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String province;   //TODO 新增  所属省份代码：110000  北京

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String longitude;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String latitude;   //TODO 新增

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerCompany;   //TODO 新增 所属公司id

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long parentCarGroupId;   //TODO 上级车队id



    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public void setCarGroupId(Long carGroupId)
    {
        this.carGroupId = carGroupId;
    }

    public Long getCarGroupId()
    {
        return carGroupId;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getCity()
    {
        return city;
    }
    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCityName()
    {
        return cityName;
    }
    public void setOwnerOrg(Long ownerOrg)
    {
        this.ownerOrg = ownerOrg;
    }

    public Long getOwnerOrg()
    {
        return ownerOrg;
    }
    public void setLeader(Long leader)
    {
        this.leader = leader;
    }

    public Long getLeader()
    {
        return leader;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carGroupId", getCarGroupId())
            .append("city", getCity())
            .append("cityName", getCityName())
            .append("ownerOrg", getOwnerOrg())
            .append("leader", getLeader())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
