package com.hq.ecmp.mscore.domain;

import java.io.Serializable;
import java.util.Date;
import com.hq.core.web.domain.BaseEntity;

/**
 * (EcmpEnterpriseInvitationInfo)实体类
 *
 * @author makejava
 * @since 2020-03-18 17:25:07
 */
public class EcmpEnterpriseInvitationInfo implements Serializable {
    private static final long serialVersionUID = 892509722520684530L;
    
    private Long invitationId;
    
    private String name;
    
    private Long enterpriseId;
    
    private Long departmentId;
    /**
    * 车队编号
    */
    private Long carGroupId;
    
    private String type;
    
    private Long roseId;
    
    private String regimeIds;
    
    private String url;
    
    private String state;
    
    private Integer createBy;
    
    private Date createTime;
    
    private Long updateBy;
    
    private Date updateTime;


    public Long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Long invitationId) {
        this.invitationId = invitationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getCarGroupId() {
        return carGroupId;
    }

    public void setCarGroupId(Long carGroupId) {
        this.carGroupId = carGroupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRoseId() {
        return roseId;
    }

    public void setRoseId(Long roseId) {
        this.roseId = roseId;
    }

    public String getRegimeIds() {
        return regimeIds;
    }

    public void setRegimeIds(String regimeIds) {
        this.regimeIds = regimeIds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}