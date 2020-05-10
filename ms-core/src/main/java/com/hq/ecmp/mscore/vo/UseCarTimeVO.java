package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCarTimeVO {

    private String allowDate;
    private Long regimeId;
    private List<CarTimeVO> useTime;

}
