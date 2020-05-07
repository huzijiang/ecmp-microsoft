package com.hq.ecmp.mscore.domain;

import com.hq.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * (EcmpEnterpriseInvitationInfo)实体类
 *
 * @author makejava
 * @since 2020-03-17 18:09:52
 */
@Data
public class EcmpEnterpriseInvitationInfo  extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long invitationId;

    private String name;

    private Long enterpriseId;

    private Long departmentId;
    /**
    * 车队编号
    */
    private Long carGroupId;

    private String type;

    private Long roseId;

    private String regimeIds;

    private String url;

    private String state;

}
