package com.hq.ecmp.vo;

import lombok.Data;

/**
 * UseCarSumExportVo：
 *
 * @author: ll
 * @date: 2020/9/4 16:01
 */
@Data
public class UseCarSumExportVo {
    private static final long serialVersionUID = 1L;

    private String deptName;

    private Integer ordersByIn;

    private String useTimesByIn;

    private String amountByIn;

    private Integer ordersByOut;

    private String useTimesByOut;

    private String amountByOut;

    private Integer orders;

    private String useTimes;

    private String amount;


}
