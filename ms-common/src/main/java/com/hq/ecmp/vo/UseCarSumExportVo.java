package com.hq.ecmp.vo;

import com.hq.core.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;

/**
 * UseCarSumExportVo：
 *
 * @author: ll
 * @date: 2020/9/4 16:01
 */
public class UseCarSumExportVo {
    private static final long serialVersionUID = 1L;

    @Excel(name = "用车单位")
    private String deptName;

    @Excel(name = "内部用车次数")
    private Integer ordersByIn;

    @Excel(name = "内部用车时间")
    private String useTimesByIn;

    @Excel(name = "内部用车费用")
    private String amountByIn;

    @Excel(name = "外部用车次数")
    private Integer ordersByOut;

    @Excel(name = "外部用车时间")
    private String useTimesByOut;

    @Excel(name = "外部用车费用")
    private String amountByOut;

    @Excel(name = "用车次数")
    private Integer orders;

    @Excel(name = "用车时间")
    private String useTimes;

    @Excel(name = "用车费用")
    private String amount;


}
