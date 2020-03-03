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
}
