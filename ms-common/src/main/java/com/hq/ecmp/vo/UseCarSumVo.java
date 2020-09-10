package com.hq.ecmp.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * UseCarSumVo：
 *
 * @author: ll
 * @date: 2020/9/4 16:09
 */
@Data
public class UseCarSumVo implements Serializable {
    private String deptId;
    private Date beginDate;
    private Date endDate;
}
