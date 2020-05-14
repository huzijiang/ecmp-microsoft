package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @author crk
 */
@Data
public class IMQueryMsgDto {
    private Integer sendRoleType;
    private Long sendId;
    private Integer receiveRoleType;
    private Long receiveId;
    private int offset;
    private int limit;
}
