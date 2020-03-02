package com.hq.ecmp.ms.api.controller.threeparty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.threeparty.FlightDto;
import com.hq.ecmp.ms.api.vo.threeparty.FlightVo;
import com.hq.ecmp.ms.api.vo.threeparty.LocationInfoVo;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/location")
public class LocationController {
	
	
	 @ApiOperation(value = "queryByShortAddress",notes = "地址反查查询 ",httpMethod ="POST")
	    @PostMapping("/queryByShortAddress")
	    public ApiResponse<List<LocationInfoVo>> queryByShortAddress(Map<String, Object> map){
		 //调用云端接口  通过短地址反查地址详情 TODO
		 List<LocationInfoVo> list=new ArrayList<LocationInfoVo>();
		 list.add(new LocationInfoVo("北京首都机场T3航站楼", "T3 机场", "152.55", "124.66"));
		 list.add(new LocationInfoVo("北京首都机场A2检票口", "A2检票口", "150.14", "123.88"));
	     return ApiResponse.success(list);
	    }
}
