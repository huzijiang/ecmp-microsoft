package com.hq.ecmp.mscore.dto.config;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/3/23
 * ecmp-microservice.
 */
@Data
public class OrderConfirmSetting {

    private String status;

    private ConfigOrderConfirmDTO value;
}
