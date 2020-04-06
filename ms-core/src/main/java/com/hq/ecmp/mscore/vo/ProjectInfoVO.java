package com.hq.ecmp.mscore.vo;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 *
 * @author caobj
 * @date 2020-03-13
 */
@Data
public class ProjectInfoVO
{
    private Long projectId;

    private String name;
    //项目主管
    private String leader;
    private String leaderName;
    private String leaderPhone;
    private String projectCode;

    private Long fatherProjectId;
    private Long ownerCompany;
    private Long ownerOrg;
    private String fatherProjectName;
    private String isFinite;
    private String startDate;
    //1可以删除，0不可删除
    private Integer isCanDelete;
    private String closeDate;
    private String isAllUserUse;
    private Integer userCount;
    private Integer isEffective;

    private String availableEmployees;

}