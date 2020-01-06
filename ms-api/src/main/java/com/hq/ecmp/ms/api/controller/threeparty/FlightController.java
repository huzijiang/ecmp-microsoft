package com.hq.ecmp.ms.api.controller.threeparty;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.threeparty.FlightDto;
import com.hq.ecmp.ms.api.vo.threeparty.FlightVo;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 *
 * @Author: zj.hu
 * @Date: 2019-12-31 13:19
 */
@RestController
@RequestMapping("/flight")
public class FlightController {

    /**
     * 航班信息查询
     * @param flightDto  航班信息
     * @return
     */
    @ApiOperation(value = "getFlightInfo",notes = "航班信息查询 ",httpMethod ="POST")
    @PostMapping("/getFlightInfo")
    public ApiResponse<List<FlightVo>> getFlightInfo(FlightDto flightDto){

        return null;
    }


}
