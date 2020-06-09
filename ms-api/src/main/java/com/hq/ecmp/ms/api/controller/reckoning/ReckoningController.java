package com.hq.ecmp.ms.api.controller.reckoning;


import com.hq.common.utils.DateUtils;
import com.hq.common.utils.http.ApiResponse;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.service.ReckoningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/reckoning")
@Api(tags = {"发起收款"})
public class ReckoningController {


    @Resource
    private ReckoningService reckoningService;

    /**
     * @author ghb
     * @description  添加收款
     */

    @ResponseBody
    @ApiOperation(value = "添加收款",notes = "添加收款")
    @RequestMapping(value = "/addReckoning", method = RequestMethod.POST)
    @Log(title = "添加收款", content = "添加收款成功",businessType = BusinessType.OTHER)
    public ApiResponse addReckoning(@RequestBody ReckoningDto param, @RequestHeader String token, HttpServletRequest request) {
        log.info("添加收款，传来的参数为："+param);
        try {
            ReckoningInfo reckoningInfo = new ReckoningInfo();
            reckoningInfo.setEcmpId(param.getEcmpId());
            reckoningInfo.setStartDate(DateUtils.strToDate(param.getStartDate(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            reckoningInfo.setEndDate(DateUtils.strToDate(param.getEndDate(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            reckoningInfo.setOffDate(DateUtils.strToDate(param.getOffDate(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            //reckoningService.addReckoning(reckoningInfo);
            return ApiResponse.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
            return ApiResponse.error("添加收款信息异常");
        }
    }


    /**
     * @author ghb
     * @description  添加收款
     */

    @ResponseBody
    @ApiOperation(value = "条件查询收款",notes = "条件查询收款")
    @RequestMapping(value = "/findReckoning", method = RequestMethod.POST)
    @Log(title = "条件查询收款", content = "条件查询收款",businessType = BusinessType.OTHER)
    public ApiResponse findReckoning(@RequestBody ReckoningDto param, @RequestHeader String token, HttpServletRequest request) {
        log.info("条件查询收款，传来的参数为："+param);
        try {
            reckoningService.findReckoning(param);
            return ApiResponse.success("条件查询收款成功");
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
            return ApiResponse.error("添加收款信息异常");
        }

    }



}
