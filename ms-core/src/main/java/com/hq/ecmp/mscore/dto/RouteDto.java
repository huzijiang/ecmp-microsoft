package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/2 21:58
 * @Version 1.0
 */
@Data
public class RouteDto {

    private String destination;
    private String origin;
    private List<PathDto> paths;
}
