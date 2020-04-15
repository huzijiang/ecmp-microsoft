package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 系统访问记录对象 ecmp_logininfor
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 访问ID */
    private Long infoId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 登录IP地址 */
    @Excel(name = "登录IP地址")
    private String ipaddr;

    /** 登录地点 */
    @Excel(name = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 登录状态（0成功 1失败） */
    @Excel(name = "登录状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /** 提示消息 */
    @Excel(name = "提示消息")
    private String msg;

    /** 访问时间 */
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginTime;

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public Long getInfoId()
    {
        return infoId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }
    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }
    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getBrowser()
    {
        return browser;
    }
    public void setOs(String os)
    {
        this.os = os;
    }

    public String getOs()
    {
        return os;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return msg;
    }
    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("infoId", getInfoId())
            .append("userName", getUserName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("msg", getMsg())
            .append("loginTime", getLoginTime())
            .toString();
    }
}
