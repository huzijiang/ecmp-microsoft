package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 ecmp_questionnaire
 * 
 * @author hqer
 * @date 2020-06-11
 */
public class EcmpQuestionnaire extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 司机id */
    @Excel(name = "司机id")
    private Long driverId;

    @Excel(name = "订单id")
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /** 车辆id */
    @Excel(name = "车辆id")
    private Long carId;

    /** 是否按时 */
    @Excel(name = "是否按时")
    private String itIsOnTime;

    /** 是否主动开门，拿行李 */
    @Excel(name = "是否主动开门，拿行李")
    private String itIsHelp;

    /** 是否不接私人电话，不主动攀谈 */
    @Excel(name = "是否不接私人电话，不主动攀谈")
    private String itIsCalm;

    /** 是否熟悉路况 */
    @Excel(name = "是否熟悉路况")
    private String itIsKnowTraffic;

    /** 是否平稳驾驶 */
    @Excel(name = "是否平稳驾驶")
    private String itIsSmooth;

    /** 是否干净 */
    @Excel(name = "是否干净")
    private String itIsClean;

    /** 是否有异味 */
    @Excel(name = "是否有异味")
    private String itIsOdor;

    /** 用车时间 */
    @Excel(name = "用车时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date useCarTime;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 星级评分 */
    private Integer score;

    private String carLicense;

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setCarId(Long carId) 
    {
        this.carId = carId;
    }

    public Long getCarId() 
    {
        return carId;
    }
    public void setItIsOnTime(String itIsOnTime) 
    {
        this.itIsOnTime = itIsOnTime;
    }

    public String getItIsOnTime() 
    {
        return itIsOnTime;
    }
    public void setItIsHelp(String itIsHelp) 
    {
        this.itIsHelp = itIsHelp;
    }

    public String getItIsHelp() 
    {
        return itIsHelp;
    }
    public void setItIsCalm(String itIsCalm) 
    {
        this.itIsCalm = itIsCalm;
    }

    public String getItIsCalm() 
    {
        return itIsCalm;
    }
    public void setItIsKnowTraffic(String itIsKnowTraffic) 
    {
        this.itIsKnowTraffic = itIsKnowTraffic;
    }

    public String getItIsKnowTraffic() 
    {
        return itIsKnowTraffic;
    }
    public void setItIsSmooth(String itIsSmooth) 
    {
        this.itIsSmooth = itIsSmooth;
    }

    public String getItIsSmooth() 
    {
        return itIsSmooth;
    }
    public void setItIsClean(String itIsClean) 
    {
        this.itIsClean = itIsClean;
    }

    public String getItIsClean() 
    {
        return itIsClean;
    }
    public void setItIsOdor(String itIsOdor) 
    {
        this.itIsOdor = itIsOdor;
    }

    public String getItIsOdor() 
    {
        return itIsOdor;
    }
    public void setUseCarTime(Date useCarTime) 
    {
        this.useCarTime = useCarTime;
    }

    public Date getUseCarTime() 
    {
        return useCarTime;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("driverId", getDriverId())
            .append("carId", getCarId())
            .append("itIsOnTime", getItIsOnTime())
            .append("itIsHelp", getItIsHelp())
            .append("itIsCalm", getItIsCalm())
            .append("itIsKnowTraffic", getItIsKnowTraffic())
            .append("itIsSmooth", getItIsSmooth())
            .append("itIsClean", getItIsClean())
            .append("itIsOdor", getItIsOdor())
            .append("useCarTime", getUseCarTime())
            .append("phone", getPhone())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}