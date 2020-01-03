package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 driver_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class DriverInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String driverName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String country;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String nation;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String idCard;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date birthday;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String gender;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String mobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String address;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String emergencyContactA;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String emergencyContactB;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String emergencyContactC;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsFullTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String licenseType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date licenseIssueDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date licenseInitIssueDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String licenseNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String licenseArchivesNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date licenseExpireDate;

    public void setDriverId(Long driverId)
    {
        this.driverId = driverId;
    }

    public Long getDriverId()
    {
        return driverId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }

    public String getDriverName()
    {
        return driverName;
    }
    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCountry()
    {
        return country;
    }
    public void setNation(String nation)
    {
        this.nation = nation;
    }

    public String getNation()
    {
        return nation;
    }
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getIdCard()
    {
        return idCard;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setEmergencyContactA(String emergencyContactA)
    {
        this.emergencyContactA = emergencyContactA;
    }

    public String getEmergencyContactA()
    {
        return emergencyContactA;
    }
    public void setEmergencyContactB(String emergencyContactB)
    {
        this.emergencyContactB = emergencyContactB;
    }

    public String getEmergencyContactB()
    {
        return emergencyContactB;
    }
    public void setEmergencyContactC(String emergencyContactC)
    {
        this.emergencyContactC = emergencyContactC;
    }

    public String getEmergencyContactC()
    {
        return emergencyContactC;
    }
    public void setItIsFullTime(String itIsFullTime)
    {
        this.itIsFullTime = itIsFullTime;
    }

    public String getItIsFullTime()
    {
        return itIsFullTime;
    }
    public void setLicenseType(String licenseType)
    {
        this.licenseType = licenseType;
    }

    public String getLicenseType()
    {
        return licenseType;
    }
    public void setLicenseIssueDate(Date licenseIssueDate)
    {
        this.licenseIssueDate = licenseIssueDate;
    }

    public Date getLicenseIssueDate()
    {
        return licenseIssueDate;
    }
    public void setLicenseInitIssueDate(Date licenseInitIssueDate)
    {
        this.licenseInitIssueDate = licenseInitIssueDate;
    }

    public Date getLicenseInitIssueDate()
    {
        return licenseInitIssueDate;
    }
    public void setLicenseNumber(String licenseNumber)
    {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber()
    {
        return licenseNumber;
    }
    public void setLicenseArchivesNumber(String licenseArchivesNumber)
    {
        this.licenseArchivesNumber = licenseArchivesNumber;
    }

    public String getLicenseArchivesNumber()
    {
        return licenseArchivesNumber;
    }
    public void setLicenseExpireDate(Date licenseExpireDate)
    {
        this.licenseExpireDate = licenseExpireDate;
    }

    public Date getLicenseExpireDate()
    {
        return licenseExpireDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("driverId", getDriverId())
            .append("userId", getUserId())
            .append("driverName", getDriverName())
            .append("country", getCountry())
            .append("nation", getNation())
            .append("idCard", getIdCard())
            .append("birthday", getBirthday())
            .append("gender", getGender())
            .append("mobile", getMobile())
            .append("address", getAddress())
            .append("emergencyContactA", getEmergencyContactA())
            .append("emergencyContactB", getEmergencyContactB())
            .append("emergencyContactC", getEmergencyContactC())
            .append("itIsFullTime", getItIsFullTime())
            .append("licenseType", getLicenseType())
            .append("licenseIssueDate", getLicenseIssueDate())
            .append("licenseInitIssueDate", getLicenseInitIssueDate())
            .append("licenseNumber", getLicenseNumber())
            .append("licenseArchivesNumber", getLicenseArchivesNumber())
            .append("licenseExpireDate", getLicenseExpireDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
