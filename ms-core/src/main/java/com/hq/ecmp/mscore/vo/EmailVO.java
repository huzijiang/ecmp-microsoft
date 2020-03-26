package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "邮箱信息")
public class EmailVO {

    /**
     * 邮箱ID
     */
    private Long Id;
    /**
     *用户ID
     */
    private Long userId;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private String state;




}
