package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2020-01-02 17:36
 */
@Data
public class UserDto {
    /**
     * 用户编号
     */
    @NotEmpty()
    @ApiParam(required = true,value = "注意是长整型")
    Long   userId;
    /**
     * 用户名称
     */
    String userName;

    /**
     * 用户用车场景id
     */
    Long sceneId;

    /**
     * 手机编号
     */
    String phoneNumber;

    /**
     * 密码 ： 明文 验证码
     */
    String password;

    /**
     * 用户当前所在经度
     */
    String longitude;

    /**
     * 用户当前所在纬度
     */
    String latitude;

    /**
     * 用户所在部门
     */
    String deptName;

    String sceneIds;

    Boolean all;


}
