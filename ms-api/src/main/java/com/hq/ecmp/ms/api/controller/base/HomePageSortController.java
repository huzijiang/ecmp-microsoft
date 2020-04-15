package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;
import com.hq.ecmp.mscore.service.IHomePageSortService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**后台首页排序
 * gaoweiwei
 */
@RestController
@RequestMapping("/home")
public class HomePageSortController {

    @Autowired
    private IHomePageSortService homePageSortService;

    /**
     * 获取首页排序数据
     * @return
     */
    @ApiOperation(value = "getHomeSort",notes = "获取首页排序数据",httpMethod ="POST")
    @PostMapping("/getHomeSort")
    public ApiResponse<List<UserConsoleHomePageSortInfo>> getHomeSort(){
        try {
            List<UserConsoleHomePageSortInfo> homeSort = homePageSortService.getHomeSort();
            return ApiResponse.success(homeSort);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("获取首页排序数据");
        }
    }
    /**
     * 修改首页排序数据
     */
    @ApiOperation(value = "updateHomeSort",notes = "修改首页排序数据",httpMethod ="POST")
    @PostMapping("/updateHomeSort")
    public ApiResponse updateHomeSort(@RequestBody List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo){
        try {
            homePageSortService.updateHomeSort(userConsoleHomePageSortInfo);
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改首页排序数据失败");
        }
    }
}
