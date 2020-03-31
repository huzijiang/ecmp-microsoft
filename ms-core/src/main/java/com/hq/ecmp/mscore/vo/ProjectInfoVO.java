package com.hq.ecmp.mscore.vo;

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

    private String fatherProjectId;
    private String fatherProjectName;

    private String startDate;

    private String closeDate;

    private Integer userCount;
    private Integer isEffective;

    private String availableEmployees;

}