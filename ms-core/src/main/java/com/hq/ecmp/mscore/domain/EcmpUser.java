package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModelProperty;
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
 * 用户信息对象 ecmp_user
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@Builder
@AllArgsConstructor
public class EcmpUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /**
     * 所属公司ID
     */
    private Long ownerCompany;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 用户类型（00系统用户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户")
    private String userType;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 用户性别（0男 1女 2未知） */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatar;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 帐号状态（0正常 1停用） */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登陆IP */
    @Excel(name = "最后登陆IP")
    private String loginIp;

    /** 最后登陆时间 */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;

    /** 是否司机（0否 1是） */
    @Excel(name = "是否司机", readConverterExp = "1否 0是")
    private String itIsDriver;

    /** 是否车队调度（0否 1是） */
    @Excel(name = "是否车队调度", readConverterExp = "0否 1是")
    private String itIsDispatcher;

    /** 驻地代码 */
    @Excel(name = "驻地代码")
    private String stationCode;

    /** 离职日期 */
    @Excel(name = "预设离职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dimissionTime;

    @Excel(name =  "企业支付")
    private String payMode;

    @Excel(name =  "工号")
    private String jobNumber;

    public EcmpUser() {
    }

    public EcmpUser(String status, String delFlag) {
        this.status = status;
        this.delFlag = delFlag;
    }

    public Long getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(Long ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("ownerCompany", getOwnerCompany())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("userType", getUserType())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
