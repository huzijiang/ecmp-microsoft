package com.hq.ecmp.mscore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 申请单VO
 * @author xueyong
 * @date 2020/1/3
 * ecmp-proxy.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveInfoDTO {
    private Integer pageSize=20;
    private Integer pageIndex=1;

}