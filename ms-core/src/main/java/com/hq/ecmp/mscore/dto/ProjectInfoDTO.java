package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * @author caobj
 * @date 2020-03-13
 */
@Data
public class ProjectInfoDTO
{
    private Long projectId;

    private String name;
    //项目主管
    private Long leader;

    private String projectCode;

    private String fatherProjectId;

    private String startDate;

    private String closeDate;

    private Integer isAllUserUse;

}