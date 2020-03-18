package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.DriverVO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 12:29
 */
@Data
public class CarDriverDTO {

    @NotEmpty
    @ApiParam(required = true)
    private Long carId;

    private List<DriverVO> drivers;
}
