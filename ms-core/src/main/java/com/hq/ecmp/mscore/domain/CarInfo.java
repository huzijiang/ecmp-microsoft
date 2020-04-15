package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 car_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long carId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long deptId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carTypeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicense;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer seatNum;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carColor;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicenseColor;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String powerType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carType01;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String engineNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String engineCc;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String enginePower;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carNumber;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carImgaeUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carDrivingLicenseImagesUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicenseImageUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date buyDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date rentEndDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerOrgId;  //TODO String 改动

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long enterpriseCarTypeId;  //TODO er模型数据   车型 商务 公务 等

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date rentStartDate;  //TODO 新增 租赁开始时间

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date borrowStartDate; // TODO 新增 借调开始时间

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date borrowEndDate; // TODO 新增 借调结束时间

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String  assetTag; // TODO 新增 资产标签

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String  source; // TODO 新增 车辆来源

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double  price; // TODO 新增 购车价格

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double  tax; // TODO 新增 购车价格

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Double  licensePrice; // TODO 新增 牌号费

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String  drivingLicense; // TODO 新增 牌号费

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date  drivingLicenseStartDate; // TODO 新增 牌号费

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date  drivingLicenseEndDate; // TODO 新增 牌号费

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String  lockState; // TODO 新增 牌号费

    public void setCarId(Long carId)
    {
        this.carId = carId;
    }

    public Long getCarId()
    {
        return carId;
    }
    public void setCarGroupId(Long carGroupId)
    {
        this.carGroupId = carGroupId;
    }

    public Long getCarGroupId()
    {
        return carGroupId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setCarTypeId(Long carTypeId)
    {
        this.carTypeId = carTypeId;
    }

    public Long getCarTypeId()
    {
        return carTypeId;
    }
    public void setCarLicense(String carLicense)
    {
        this.carLicense = carLicense;
    }

    public String getCarLicense()
    {
        return carLicense;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setCarType(String carType)
    {
        this.carType = carType;
    }

    public String getCarType()
    {
        return carType;
    }
    public void setSeatNum(Integer seatNum)
    {
        this.seatNum = seatNum;
    }

    public Integer getSeatNum()
    {
        return seatNum;
    }
    public void setCarColor(String carColor)
    {
        this.carColor = carColor;
    }

    public String getCarColor()
    {
        return carColor;
    }
    public void setCarLicenseColor(String carLicenseColor)
    {
        this.carLicenseColor = carLicenseColor;
    }

    public String getCarLicenseColor()
    {
        return carLicenseColor;
    }
    public void setPowerType(String powerType)
    {
        this.powerType = powerType;
    }

    public String getPowerType()
    {
        return powerType;
    }
    public void setCarType01(String carType01)
    {
        this.carType01 = carType01;
    }

    public String getCarType01()
    {
        return carType01;
    }
    public void setEngineNumber(String engineNumber)
    {
        this.engineNumber = engineNumber;
    }

    public String getEngineNumber()
    {
        return engineNumber;
    }
    public void setEngineCc(String engineCc)
    {
        this.engineCc = engineCc;
    }

    public String getEngineCc()
    {
        return engineCc;
    }
    public void setEnginePower(String enginePower)
    {
        this.enginePower = enginePower;
    }

    public String getEnginePower()
    {
        return enginePower;
    }
    public void setCarNumber(String carNumber)
    {
        this.carNumber = carNumber;
    }

    public String getCarNumber()
    {
        return carNumber;
    }
    public void setCarImgaeUrl(String carImgaeUrl)
    {
        this.carImgaeUrl = carImgaeUrl;
    }

    public String getCarImgaeUrl()
    {
        return carImgaeUrl;
    }
    public void setCarDrivingLicenseImagesUrl(String carDrivingLicenseImagesUrl)
    {
        this.carDrivingLicenseImagesUrl = carDrivingLicenseImagesUrl;
    }

    public String getCarDrivingLicenseImagesUrl()
    {
        return carDrivingLicenseImagesUrl;
    }
    public void setCarLicenseImageUrl(String carLicenseImageUrl)
    {
        this.carLicenseImageUrl = carLicenseImageUrl;
    }

    public String getCarLicenseImageUrl()
    {
        return carLicenseImageUrl;
    }
    public void setBuyDate(Date buyDate)
    {
        this.buyDate = buyDate;
    }

    public Date getBuyDate()
    {
        return buyDate;
    }
    public void setRentEndDate(Date rentEndDate)
    {
        this.rentEndDate = rentEndDate;
    }

    public Date getRentEndDate()
    {
        return rentEndDate;
    }
    public void setOwnerOrgId(Long ownerOrgId)
    {
        this.ownerOrgId = ownerOrgId;
    }

    public Long getOwnerOrgId()
    {
        return ownerOrgId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carId", getCarId())
            .append("carGroupId", getCarGroupId())
            .append("deptId", getDeptId())
            .append("carTypeId", getCarTypeId())
            .append("carLicense", getCarLicense())
            .append("state", getState())
            .append("carType", getCarType())
            .append("seatNum", getSeatNum())
            .append("carColor", getCarColor())
            .append("carLicenseColor", getCarLicenseColor())
            .append("powerType", getPowerType())
            .append("carType01", getCarType01())
            .append("engineNumber", getEngineNumber())
            .append("engineCc", getEngineCc())
            .append("enginePower", getEnginePower())
            .append("carNumber", getCarNumber())
            .append("carImgaeUrl", getCarImgaeUrl())
            .append("carDrivingLicenseImagesUrl", getCarDrivingLicenseImagesUrl())
            .append("carLicenseImageUrl", getCarLicenseImageUrl())
            .append("buyDate", getBuyDate())
            .append("rentEndDate", getRentEndDate())
            .append("ownerOrgId", getOwnerOrgId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
