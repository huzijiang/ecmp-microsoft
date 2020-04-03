package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/2 21:56
 * @Version 1.0
 */
@Data
public class DirectionDto {

    private int count;
    private String info;
    private int infoCode;
    private RouteDto route;
    private int status;
}
