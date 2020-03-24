package com.hq.ecmp.mscore.dto.config;

import lombok.Data;

import java.util.List;

/**
 * @author xueyong
 * @date 2020/3/23
 * ecmp-microservice.
 */
@Data
public class ConfigAutoDispatchDTO {

    private String status;

    private List<AutoDispatchSetting> value;

}
