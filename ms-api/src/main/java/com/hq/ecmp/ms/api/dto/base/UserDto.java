package com.hq.ecmp.ms.api.dto.base;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-02 17:36
 */
@Data
public class UserDto {
    /**
     * 用户编号
     */
    @NotEmpty
    Long   userId;
    /**
     * 用户名称
     */
    String userName;

    /**
     * 手机编号
     */
    String phoneNumber;



}
