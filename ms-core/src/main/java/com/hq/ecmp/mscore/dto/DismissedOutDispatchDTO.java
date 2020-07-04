package com.hq.ecmp.mscore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 * 外部调度员驳回DTO
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DismissedOutDispatchDTO {

    //预约备注
    private String notes;

    //外部车队名称
    private String carGroupName;

    //外部车队联系电话
    private String telephone;

    //预定车型
    private String carTypeName;

}
