package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/3 12:23
 */
@Data
public class PageRequest {

    //起始页码
    private Integer pageNum;
    //每页显示条数
    private Integer pageSize;
    //0:当天,1:明天,2:后天 3:大后天,-1:全部
    private Integer day;
    //搜索字段
    private String search;
}
