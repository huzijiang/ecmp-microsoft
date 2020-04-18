package com.hq.ecmp.mscore.domain;

import com.hq.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (CarGroupServeScopeInfo)实体类
 *
 * @author zj.hu
 * @since 2020-03-21 12:11:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarGroupServeScopeInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -22677357277542735L;

    private Long id;

    private Long carGroupId;

    private String province;

    private String city;

}
