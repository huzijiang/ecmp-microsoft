package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/18 9:47
 */
@Data
public class UserReqimensDTO {

    private Long userId;
    private List<Long> regimenIds;
}
