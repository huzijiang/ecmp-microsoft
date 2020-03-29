package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/27 23:44
 */
@Data
public class DispatcherAndFixedLineVO {



    @ApiModelProperty(name = "dispatchers",value = "车队调度员信息")
    private UserVO dispatcher;

    private List<CarGroupFixedPhoneVO> groupPhones;
}
