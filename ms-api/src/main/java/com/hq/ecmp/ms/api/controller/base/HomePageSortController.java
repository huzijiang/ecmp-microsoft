package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;
import com.hq.ecmp.mscore.dto.config.ConfigValueDTO;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import com.hq.ecmp.mscore.service.IHomePageSortService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**后台首页排序
 * gaoweiwei
 */
@RestController
@RequestMapping("/home")
@Slf4j
public class HomePageSortController {



    @Autowired
    private IHomePageSortService homePageSortService;

    @Autowired
    private IEcmpConfigService ecmpConfigService;

    @Autowired
    private TokenService tokenService;

//    /**
//     * 获取首页排序数据
//     * @return
//     */
//    @ApiOperation(value = "getHomeSort",notes = "获取首页排序数据",httpMethod ="POST")
//    @PostMapping("/getHomeSort")
//    public ApiResponse<List<UserConsoleHomePageSortInfo>> getHomeSort(){
//        try {
//            List<UserConsoleHomePageSortInfo> homeSort = homePageSortService.getHomeSort();
//            return ApiResponse.success(homeSort);
//        } catch (Exception e) {
//            log.error("业务处理异常", e)();
//            return ApiResponse.error("获取首页排序数据");
//        }
//    }

    /**
     * 根据角色获取模块
     * @return
     */
    @ApiOperation(value = "getHomeSort",notes = "根据角色获取模块",httpMethod ="POST")
    @PostMapping("/getHomeSort")
    public ApiResponse<List<UserConsoleHomePageSortInfo>> getHomeSort(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);

            List<UserConsoleHomePageSortInfo> list2 = homePageSortService.getHomePageSort(loginUser.getUser().getUserId());
//            if(list2.size() == 0){
                String roleIds = homePageSortService.getRoleIds(loginUser.getUser().getUserId());
                List<UserConsoleHomePageSortInfo> homeSort;
//                if(roleIds.contains("管理")){
                if(roleIds.contains("admin")){
                    homeSort = homePageSortService.getPanelByRoleId(CommonConstant.ADMIN);
//                }else if(roleIds.contains("调度")){
                }else if(roleIds.contains("dispatcher")){
                    homeSort = homePageSortService.getPanelByRoleId(CommonConstant.DISPATCHER);
                }else{
                    homeSort = homePageSortService.getPanelByRoleId(CommonConstant.EMPLOYEE);
                }
                StringBuffer s1 = new StringBuffer();
                for (UserConsoleHomePageSortInfo obj : list2){
                        s1.append(obj.getPanelName());
                }
                StringBuffer s2 = new StringBuffer();
                for (UserConsoleHomePageSortInfo obj : homeSort){
                    s2.append(obj.getPanelName());
                }
                if(s1.toString().equals(s2.toString())){
                    return ApiResponse.success(list2);
                }else{
                    List<UserConsoleHomePageSortInfo> list = new ArrayList();
                    for (UserConsoleHomePageSortInfo userConsoleHomePageSortInfo : homeSort){
                        if(null == userConsoleHomePageSortInfo.getUserId() || "".equals(userConsoleHomePageSortInfo.getUserId())){
                            userConsoleHomePageSortInfo.setUserId(loginUser.getUser().getUserId());
                            list.add(userConsoleHomePageSortInfo);
                        }
                    }
                    homePageSortService.deleteHomeSorts(list2);
                    homePageSortService.updateHomeSorts(list);
                    return ApiResponse.success(list);
                }
//                homeSort = homePageSortService.getPanelByRoleId(roleIds);

//            }else{
//                return  ApiResponse.success(list2);
//            }

        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("根据角色获取模块");
        }
    }

    /**
     * 修改首页排序数据
     */
    @ApiOperation(value = "updateHomeSort",notes = "修改首页排序数据",httpMethod ="POST")
    @PostMapping("/updateHomeSort")
    public ApiResponse updateHomeSort(@RequestBody List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo){
        try {
            homePageSortService.deleteHomeSorts(userConsoleHomePageSortInfo);
            homePageSortService.updateHomeSorts(userConsoleHomePageSortInfo);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("修改首页排序数据失败");
        }
    }

    @ApiOperation(value = "backgroundImage ", notes = "设置背景图片信息默认")
    @PostMapping("/backgroundImage")
    public ApiResponse backgroundImage(@RequestBody ConfigValueDTO configValueDTO,MultipartFile file) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId =  loginUser.getUser().getOwnerCompany();
        ApiResponse apiResponse =ecmpConfigService.setUpBackGroundImage(configValueDTO.getStatus(),configValueDTO.getValue(),file,companyId);
        return apiResponse;
    }

    @ApiOperation(value = "screen ", notes = "设置开屏图片信息为null")
    @PostMapping(value = "/screen", produces = "application/json;charset=UTF-8")
    public ApiResponse screen(@RequestBody ConfigValueDTO configValueDTO,MultipartFile file) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId =  loginUser.getUser().getOwnerCompany();
        ecmpConfigService.setUpWelComeImage(configValueDTO.getStatus(),configValueDTO.getValue(),file,companyId);
        return ApiResponse.success();
    }

}
