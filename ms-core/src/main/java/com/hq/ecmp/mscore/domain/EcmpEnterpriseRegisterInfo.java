package com.hq.ecmp.mscore.domain;

import java.io.Serializable;
import java.util.Date;
import com.hq.core.web.domain.BaseEntity;

/**
 * (EcmpEnterpriseRegisterInfo)实体类
 *
 * @author makejava
 * @since 2020-03-17 18:10:01
 */
public class EcmpEnterpriseRegisterInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;
    
    private Long registerId;
    
    private Long invitationId;
    
    private String type;
    
    private String name;
    
    private String gender;
    
    private String jobNumber;
    
    private String email;
    
    private String mobile;
    
    private String smsAuthCode;
    
    private String reason;
    
    private String idCard;
    /**
    * 车队编号
    */
    private Long carGroupId;
    
    private String licenseType;
    
    private Date licenseInitIssueDate;
    
    private Date licenseIssueDate;
    
    private Date licenseExpireDate;
    
    private String licenseNumber;
    
    private String licenseArchivesNumber;
    
    private String licenseImages;
    
    private String state;
    
    private Long auditor;
    
    private Date auditTime;
    /* 新增审核拒绝原因*/
    private String rejectReason;
    


    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsAuthCode() {
        return smsAuthCode;
    }

    public void setSmsAuthCode(String smsAuthCode) {
        this.smsAuthCode = smsAuthCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Long getCarGroupId() {
        return carGroupId;
    }

    public void setCarGroupId(Long carGroupId) {
        this.carGroupId = carGroupId;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public Date getLicenseInitIssueDate() {
        return licenseInitIssueDate;
    }

    public void setLicenseInitIssueDate(Date licenseInitIssueDate) {
        this.licenseInitIssueDate = licenseInitIssueDate;
    }

    public Date getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(Date licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public Date getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(Date licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseArchivesNumber() {
        return licenseArchivesNumber;
    }

    public void setLicenseArchivesNumber(String licenseArchivesNumber) {
        this.licenseArchivesNumber = licenseArchivesNumber;
    }

    public String getLicenseImages() {
        return licenseImages;
    }

    public void setLicenseImages(String licenseImages) {
        this.licenseImages = licenseImages;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}