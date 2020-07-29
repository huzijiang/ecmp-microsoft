package com.hq.ecmp.mscore.domain;

import com.hq.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户地址信息表 user_address
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress extends BaseEntity
{
    private Long id;

    /** 用户ID */
    private Long userId;

    /**
     * 地址信息
     */
    private String addressJson;

}
