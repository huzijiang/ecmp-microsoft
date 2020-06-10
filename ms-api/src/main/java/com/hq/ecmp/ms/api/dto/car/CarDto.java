package com.hq.ecmp.ms.api.dto.car;

import com.hq.ecmp.mscore.vo.DriverVO;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 17:28
 */
@Data
public class CarDto {

    @NotEmpty
    @ApiParam(required = true)
    private Long carId;

    private Long driverId;  //驾驶员id

   //禁用、维保说明
    private String content;

    //T001   维保  T002   禁用
    private String logType;

}
