package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: caobj
 * @Date: 2020-03-02 18:26
 */
@Data
public class ProjectDto {
    /**
     * 项目编号/名
     */
    String projectName;
    Long projectId;
    String search;

}
