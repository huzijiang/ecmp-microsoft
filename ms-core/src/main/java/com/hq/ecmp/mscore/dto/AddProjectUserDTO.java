package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.util.List;

/**
 *
 * @author caobj
 * @date 2020-03-13
 */
@Data
public class AddProjectUserDTO
{
    private Long projectId;
    private Long[] userIds;
}