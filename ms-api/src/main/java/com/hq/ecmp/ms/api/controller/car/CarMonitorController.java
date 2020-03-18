package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.CarLocationDto;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.vo.CarLocationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CarMonitorXController
 * @Description TODO 后管车辆监控
 * @Author yj
 * @Date 2020/3/18 10:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/CarMonitor")
@Api(value = "后管车辆监控",tags = "车辆监控")
public class CarMonitorController {

    @Resource
    private ICarInfoService iCarInfoService;

    /**
     * 车辆检索和定位
     */
    @ApiOperation(value = "车辆检索")
    public ApiResponse<List<CarLocationVo>> locationCars(@RequestBody CarLocationDto carLocationDto){
        List<CarLocationVo> carLocationVos = null;
        try {
            carLocationVos = iCarInfoService.locationCars(carLocationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(carLocationVos);
    }
}
