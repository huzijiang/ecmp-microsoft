package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: caobj
 * @Date: 2020-03-04 15:26
 */
@Data
public class EcmpMessageDto {

    private String configType;
    private Long ecmpId;
    private Long categoryId;
    private Long applyId;
    private String status;
    private String category;

    public EcmpMessageDto() {
    }

    public EcmpMessageDto(String configType, Long categoryId, Long applyId, String status) {
        this.configType = configType;
        this.categoryId = categoryId;
        this.applyId = applyId;
        this.status = status;
    }
    public EcmpMessageDto(String configType, Long categoryId, Long applyId, String status,String category) {
        this.configType = configType;
        this.categoryId = categoryId;
        this.applyId = applyId;
        this.status = status;
        this.category = category;
    }
}
