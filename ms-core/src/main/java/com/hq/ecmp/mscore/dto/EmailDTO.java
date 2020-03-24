package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author  shixin
 * @date 2020-03-17
 */
@Data
public class EmailDTO {
    @ApiParam(name = "Id", value = "用户ID", required = false )
    private Integer Id;
    @ApiParam(name = "userId", value = "用户ID", required = true )
    private Long userId;
    @ApiParam(name = "email", value = "邮箱地址", required = true )
    private String email;
    @ApiParam(name = "state", value = "状态", required = false )
    private String state;


}
