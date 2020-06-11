package com.hq.ecmp.ms.api.controller.cost;

import com.google.gson.JsonObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import com.hq.ecmp.mscore.dto.cost.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.service.ICostConfigInfoService;
import com.hq.ecmp.mscore.vo.CarGroupCostVO;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.vo.PriceOverviewVO;
import com.hq.ecmp.mscore.vo.SupplementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ICarGroupInfoService carGroupInfoService;


    /**
     * 包车服务--根据车队获取适用城市
     * @param  carGroupId 车队id
     */
    @PostMapping("/getCitysBycarGroupId")
    @Log(value = "包车服务--根据车队获取适用城市")
    @com.hq.core.aspectj.lang.annotation.Log(title = "包车服务--根据车队获取适用城市",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    public ApiResponse<List<CostConfigCityInfo>>  getCitysBycarGroupId(Long carGroupId){
        try {
            //获取当前登陆用户的信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            List<CostConfigCityInfo> citys=carGroupInfoService.getCitysBycarGroupId(carGroupId);
            return ApiResponse.success(citys);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    /**
     * 成本设置信息录入
     * @param costConfigDto 成本信息录入model
     */
    @PostMapping("/createCostConfig")
    @Log(value = "成本设置信息录入")
    @com.hq.core.aspectj.lang.annotation.Log(title = "新增成本设置",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    public ApiResponse  createCostConfig(@RequestBody CostConfigInsertDto costConfigDto){
        try {
            //获取当前登陆用户的信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            Long userId = loginUser.getUser().getUserId();
            Long companyId = loginUser.getUser().getOwnerCompany();
            costConfigDto.setCompanyId(companyId);
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
    @PostMapping("/queryCostConfigList")
    public ApiResponse<CostConfigListResultPage> queryCostConfigList(@RequestBody  CostConfigQueryDto costConfigQueryDto){
        CostConfigListResultPage costConfigListResults;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            Long companyId = loginUser.getUser().getOwnerCompany();
            costConfigQueryDto.setCompanyId(companyId);
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
    @PostMapping("/queryCostConfigById")
    public ApiResponse<CostConfigListResult> queryCostConfigById(@RequestParam("costId") Long costId){
        CostConfigListResult costConfigListResult;
        try {
            costConfigListResult = costConfigInfoService.selectCostConfigInfoById(costId,null);
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
    @PostMapping("/updateCostConfigById")
    public ApiResponse updateCostConfig(@RequestBody CostConfigListResult costConfigListResult){
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

    /**
     * 成本设置信息删除
     * @param costConfigCityId
     * @param costId
     * @param cityCode
     * @return
     */
    @Log(value = "通过成本设置城市附表ID删除数据")
    @com.hq.core.aspectj.lang.annotation.Log(title = "成本设置信息修改",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @PostMapping("/deleteCostConfigByCostCityId")
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
    @PostMapping("/checkDoubleByServiceTypeCityCarType")
    public ApiResponse<List<ValidDoubleDtoResult>> checkDoubleByServiceTypeCityCarType(@RequestBody CostConfigQueryDoubleValidDto costConfigQueryDto){
        List<ValidDoubleDtoResult> res = null;
        try {
            res = costConfigInfoService.checkDoubleByServiceTypeCityCarType(costConfigQueryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return  ApiResponse.error("判重失败");
        }
        return ApiResponse.success(res);
    }

    /**
     *
     * @param costConfigQueryDto  车型，城市，服务类型
     * @return ApiResponse
     */
    @Log(value = "车队,车型，服务类型,服务模式四者校验是否重复")
    @com.hq.core.aspectj.lang.annotation.Log(title = "车队,车型，服务类型,服务模式四者校验是否重复",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @PostMapping("/checkCharteredCost")
    public ApiResponse<List<ValidDoubleDtoResult>> checkCharteredCost(@RequestBody CostConfigQueryDoubleValidDto costConfigQueryDto){
        List<ValidDoubleDtoResult> res = null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            //获取当前登陆用户的信息Id
            Long companyId = loginUser.getUser().getOwnerCompany();
            costConfigQueryDto.setCompanyId(companyId);
            res = costConfigInfoService.checkCharteredCost(costConfigQueryDto);
        } catch (Exception e) {
            e.printStackTrace();
            return  ApiResponse.error("判重失败");
        }
        return ApiResponse.success(res);
    }

    /**
     * 补单成本计算
     * @param
     * @return
     */
    @Log(value = "补单成本计算")
    @com.hq.core.aspectj.lang.annotation.Log(title = "补单成本计算",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @PostMapping("/supplementAmountCalculation")
    public ApiResponse supplementAmountCalculation(@RequestBody SupplementVO supplement){
        ApiResponse apiResponse = new ApiResponse();
        try {
            //获取登陆用户的信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long companyId = loginUser.getUser().getOwnerCompany();
            String json = costConfigInfoService.supplementAmountCalculation(supplement,companyId);
            apiResponse.setData(json);
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setMsg("补单成本计算失败");
            return apiResponse;
        }
        return apiResponse;
    }

    /**
     *  成本名稱判重
     */
    @Log(value = "成本名称判重")
    @com.hq.core.aspectj.lang.annotation.Log(title = "成本名称判重",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @PostMapping("/costConfigNameIsDouble")
    public ApiResponse costConfigNameIsDouble(String configName){
        try {
            Boolean isDouble = costConfigInfoService.costConfigNameIsDouble(configName.trim());
            if(isDouble){
                return ApiResponse.success(isDouble);
            }else{
                return ApiResponse.success(Boolean.valueOf(false));
            }
        } catch (Exception e) {
            return ApiResponse.error("判重失败");
        }
    }

    /**
     *  价格总览
     */
    @Log(value = "价格总览")
    @com.hq.core.aspectj.lang.annotation.Log(title = "价格总览",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @PostMapping("/getGroupPrice")
    public ApiResponse<List<PriceOverviewVO>> getGroupPrice(@RequestBody CostConfigQueryPriceDto queryPriceDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long companyId = loginUser.getUser().getOwnerCompany();
            queryPriceDto.setCompanyId(companyId);
            List<PriceOverviewVO> list=costConfigInfoService.getGroupPrice(queryPriceDto,loginUser);
                return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("查询异常");
        }
    }

    /**
     *  价格总览根据角色获取车队列表
     */
    @Log(value = "价格总览根据角色获取车队列表")
    @com.hq.core.aspectj.lang.annotation.Log(title = "价格总览根据角色获取车队列表",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @PostMapping("/getCarGroupListForCost")
    public ApiResponse<List<CarGroupCostVO>> getCarGroupListForCost(@RequestBody CostConfigQueryPriceDto queryPriceDto){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long companyId = loginUser.getUser().getOwnerCompany();
            queryPriceDto.setCompanyId(companyId);
            List<CarGroupCostVO> list=costConfigInfoService.getCarGroupListForCost(queryPriceDto,loginUser);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("查询异常");
        }
    }


    /**
     *  获取配置成本中心的城市列表
     */
    @Log(value = "获取配置成本中心的城市列表")
    @com.hq.core.aspectj.lang.annotation.Log(title = "获取配置成本中心的城市列表",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @PostMapping("/getCostCityList")
    public ApiResponse<List<CityInfo>> getCostCityList(){
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long companyId = loginUser.getUser().getOwnerCompany();
            List<CityInfo> list=costConfigInfoService.getCostCityList(companyId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            return ApiResponse.error("查询异常");
        }
    }

}
