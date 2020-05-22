package com.hq.ecmp.mscore.dto.config;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/3/16
 * ecmp-microservice.
 */
@Data
public class EnterPriseBaseInfoDTO {

    private String name;
    private String enName;
    private String shortName;
    private String address;
    private String contractor;
    private String email;
    private String phone;
    private String industry;
    private String scale;
    private Long companyId;
}
