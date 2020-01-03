package com.hq.ecmp.ms.api.dto.base;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-02 18:26
 */
public class RegimeDto {
    /**
     * 用车制度编号
     */
    @NotEmpty
    Long regimeId;
}
