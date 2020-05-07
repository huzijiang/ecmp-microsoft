package com.hq.ecmp.ms.api.controller.cost;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.dto.cost.CostConfigInsertDto;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;
import com.hq.ecmp.mscore.service.ICostConfigInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName 成本中心
 * @Description TODO
 * @Author yj
 * @Date 2020/5/6 10:29
 * @Version 1.0
 */
@RestController
@RequestMapping("/cost")
public class CostController {

    @Resource
    private ICostConfigInfoService costConfigInfoService;
    @Resource
    private TokenService tokenService;

    /**
     * 成本设置信息录入
     * @param costConfigDto 成本信息录入model
     */
    @RequestMapping("/createCostConfig")
    @Log(value = "成本设置信息录入")
    @com.hq.core.aspectj.lang.annotation.Log(title = "新增成本设置",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    public ApiResponse  createCostConfig(CostConfigInsertDto costConfigDto){
        try {
            //获取当前登陆用户的信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            Long userId = loginUser.getUser().getUserId();
            costConfigInfoService.createCostConfig(costConfigDto,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("成本录入失败");
        }
        return ApiResponse.success("成本设置录入成功");
    }

    /**
     * 成本设置列表查询
     * @return ApiResponse<List<CostConfigListResult>>
     */
    @Log(value = "成本设置列表查询")
    @com.hq.core.aspectj.lang.annotation.Log(title = "成本设置列表查询",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @RequestMapping("/queryCostConfigList")
    public ApiResponse<List<CostConfigListResult>> queryCostConfigList(CostConfigQueryDto costConfigQueryDto){
        List<CostConfigListResult> costConfigListResults;
        try {
            costConfigListResults = costConfigInfoService.selectCostConfigInfoList(costConfigQueryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("成本设置列表查询失败");
        }
        return ApiResponse.success(costConfigListResults);
    }

    /**
     * 通过id查询成本设置信息 AND 成本设置详情以及修改的成本回显
     * @param costId 成本配置主表id
     * @return ApiResponse<CostConfigListResult>
     */
    @Log(value = "通过id查询成本设置信息 AND 成本设置详情以及修改的成本回显")
    @com.hq.core.aspectj.lang.annotation.Log(title = "通过id查询成本设置信息 AND 成本设置详情以及修改的成本回显",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @RequestMapping("/queryCostConfigById")
    public ApiResponse<CostConfigListResult> queryCostConfigById(Long costId){
        CostConfigListResult costConfigListResult;
        try {
            costConfigListResult = costConfigInfoService.selectCostConfigInfoById(costId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("通过id查询成本信息失败");
        }
        return ApiResponse.success(costConfigListResult);
    }

    /**
     * 成本设置信息修改
     * @return ApiResponse
     */
    @Log(value = "成本设置信息修改")
    @com.hq.core.aspectj.lang.annotation.Log(title = "成本设置信息修改",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @RequestMapping("/updateCostConfigById")
    public ApiResponse updateCostConfig(CostConfigListResult costConfigListResult){
        //获取当前登陆用户的信息
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            Long userId = loginUser.getUser().getUserId();
            costConfigInfoService.updateCostConfig(costConfigListResult, userId);
        } catch (Exception e) {
            return ApiResponse.error("成本设置信息更新失败");

        }
        return ApiResponse.success("成本设置信息更新成功");
    }

    @Log(value = "通过成本设置城市附表ID删除数据")
    @com.hq.core.aspectj.lang.annotation.Log(title = "成本设置信息修改",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @RequestMapping("/deleteCostConfigByCostCityId")
    public ApiResponse deleteCostConfigByCostCityId(@RequestParam("costConfigCityId") Long costConfigCityId, @RequestParam("costId") Long costId,
                                                    @RequestParam("cityCode") Integer cityCode){
        try {
            costConfigInfoService.deleteCostConfigByCostCityId(costConfigCityId,costId,cityCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除失败");
        }
        return ApiResponse.success("删除成功");
    }

    /**
     *
     * @param costConfigQueryDto  车型，城市，服务类型
     * @return ApiResponse
     */
    @Log(value = "车型，城市，服务类型三者校验是否重复")
    @com.hq.core.aspectj.lang.annotation.Log(title = "车型，城市，服务类型三者校验是否重复",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @RequestMapping("/checkDoubleByServiceTypeCityCarType")
    public ApiResponse checkDoubleByServiceTypeCityCarType(CostConfigQueryDto costConfigQueryDto){
        Integer i = 0;
        try {
            i = costConfigInfoService.checkDoubleByServiceTypeCityCarType(costConfigQueryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return  ApiResponse.error("判重失败");
        }
        return ApiResponse.success(i);
    }

}
