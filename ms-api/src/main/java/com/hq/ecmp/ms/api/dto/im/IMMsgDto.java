package com.hq.ecmp.ms.api.dto.im;

import lombok.Data;

/**
 * @author crk
 */
@Data
public class IMMsgDto {
    private Integer sendRoleType;
    private Long sendId;
    private Integer receiveRoleType;
    private Long receiveId;
    private String content;
}
