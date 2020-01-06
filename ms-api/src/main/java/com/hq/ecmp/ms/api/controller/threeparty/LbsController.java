package com.hq.ecmp.ms.api.controller.threeparty;

import com.hq.common.core.api.ApiResponse;
import com.hq.core.web.domain.AjaxResult;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.bo.LbsAddress;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:19
 */
@RestController
@RequestMapping("/lbs")
public class LbsController {

    /**
     * 根据经纬度 反向查询 附近的地址名称
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "getAddressByLL",notes = "据经纬度 反向查询 附近的地址名称 ",httpMethod ="POST")
    @PostMapping("/getAddressByLL")
    public ApiResponse<List<LbsAddress>> getAddressByLL(UserDto userDto){

        return null;
    }

    /**
     * 查询行程节点列表，
     * 方便端上进行地图描图   LbsAddress 可以根据需要简化
     * @param jouneyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getAddressByJouneyInfo",notes = "查询用户 所在（子）公司的项目信息 ",httpMethod ="POST")
    @PostMapping("/getAddressByJouneyInfo")
    public ApiResponse<List<LbsAddress>> getAddressByJouneyInfo(JourneyApplyDto jouneyApplyDto){

        return null;
    }



}
