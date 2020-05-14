package com.hq.ecmp.ms.api.dto.im;

import lombok.Data;

/**
 * @author crk
 */
@Data
public class IMBaseDto {
    private Integer sendRoleType;
    private Long sendId;
    private Integer receiveRoleType;
    private Long receiveId;
}
