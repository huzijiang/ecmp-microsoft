package com.hq.ecmp.mscore.domain;

import com.hq.core.web.domain.BaseEntity;

/**
 * (EcmpEnterpriseInfo)实体类
 *
 * @author makejava
 * @since 2020-03-17 18:09:19
 */
public class EcmpEnterpriseInfo implements Serializable {
    private static final long serialVersionUID = 117119065421503810L;
    
    private Integer enterpriseId;
    
    private Long deptId;
    
    private String name;
    
    private String address;
    
    private String mobile;
    
    private String uscc;


    public Integer getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Integer enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUscc() {
        return uscc;
    }

    public void setUscc(String uscc) {
        this.uscc = uscc;
    }

}