package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.CarVO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: xin.shi
 * @Date: 2020/3/19
 */
@Data
public class DriverCarDTO {

    @NotEmpty
    @ApiParam(required = true)
    private Long driverId;

    private List<CarVO> cars;
}
