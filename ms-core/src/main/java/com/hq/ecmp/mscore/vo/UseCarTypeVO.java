package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCarTypeVO {

    private Long applyId;
    private String useCarModeStr;
    private String useCarMode;
    private String carTypeStr;
    private String carTypeId;
    private String cityCode;

}
