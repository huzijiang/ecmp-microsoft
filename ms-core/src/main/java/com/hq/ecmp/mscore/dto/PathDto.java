package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/2 21:58
 * @Version 1.0
 */
@Data
public class PathDto {
    private int distance;
    private int duration;
    private String restriction;
    private String strategy;
    private String tollDistance;
    private String  tolls;
    private String traffic_lights;
}
