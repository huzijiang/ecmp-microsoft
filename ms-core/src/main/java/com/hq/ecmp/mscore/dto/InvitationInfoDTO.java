package com.hq.ecmp.mscore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邀请类别
 * @author shixin
 * @date 2020/3/17
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationInfoDTO {
    //邀请类型
    private String type;


}
