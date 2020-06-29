package com.hq.ecmp.ms.api.controller.dispatcher;

import com.hq.common.core.api.ApiResponse;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.service.DispatchWorkBenchService;
import com.hq.ecmp.mscore.vo.DisWorkBenchInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>调度工作台</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 11:18
 */
@RestController
@RequestMapping("/dispatch/workBench")
@Slf4j
public class DispatchWorkBenchController {

    @Resource
    private DispatchWorkBenchService dispatchWorkBenchService;

    @PostMapping("/getDispatchOrderListWorkBench")
    @ApiOperation("/工作台列表数据获取")
    @Log(value = "调度工作台")
    @com.hq.core.aspectj.lang.annotation.Log(title = "调度工作台",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    public ApiResponse<DisWorkBenchInfo> getDispatchInfoListWorkBench(String state, Integer pageNum, Integer pageSize){
        try {
            DisWorkBenchInfo dispatchInfoListWorkBench = dispatchWorkBenchService.getDispatchInfoListWorkBench(state, pageNum, pageSize);
            return ApiResponse.success(dispatchInfoListWorkBench);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("获取工作台列表数据失败");
        }
    }


}
