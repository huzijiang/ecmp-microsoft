package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (CarGroupServeOrgRelation)实体类
 *
 * @author makejava
 * @since 2020-05-05 13:15:10
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupServeOrgRelation implements Serializable {
    private static final long serialVersionUID = 168974335357840443L;
    
    private Long carGroupId;
    
    private Long deptId;
    
    private String type;


    public Long getCarGroupId() {
        return carGroupId;
    }

    public void setCarGroupId(Long carGroupId) {
        this.carGroupId = carGroupId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}