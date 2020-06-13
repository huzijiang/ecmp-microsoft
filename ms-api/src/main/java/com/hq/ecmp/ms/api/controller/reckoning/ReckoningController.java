package com.hq.ecmp.ms.api.controller.reckoning;


import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.common.utils.http.ApiResponse;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CollectionQuittanceEnum;
import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.service.CollectionQuittanceInfoService;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reckoning")
@Api(tags = {"发起收款"})
public class ReckoningController {


    @Resource
    private CollectionQuittanceInfoService collectionService;

    @Autowired
    private ICarGroupInfoService icarGroupInfoService;
    @Resource
    private TokenService tokenService;

    /**
     * @author ghb
     * @description  添加收款
     */
    @ResponseBody
    @ApiOperation(value = "添加收款",notes = "添加收款")
    @RequestMapping(value = "/addReckoning", method = RequestMethod.POST)
    @Log(title = "添加收款", content = "添加收款成功",businessType = BusinessType.OTHER)
    public ApiResponse addReckoning(@RequestBody ReckoningDto param) {
        log.info("添加收款，传来的参数为："+param);
        try {
            ReckoningInfo reckoningInfo = new ReckoningInfo();
            reckoningInfo.setState(CollectionQuittanceEnum.COLLECTION_WAIT.getKey());
            reckoningInfo.setCreateTime(new Date());
            reckoningInfo.setServiceOrg(param.getCompanyId());
            reckoningInfo.setBeginDate(DateUtils.strToDate(param.getStartDate(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            reckoningInfo.setEndDate(DateUtils.strToDate(param.getEndDate(),DateUtils.YYYY_MM_DD_HH_MM_SS));
            reckoningInfo.setCollectionEndTime(DateUtils.strToDate(param.getOffDate(),DateUtils.YYYY_MM_DD));
            reckoningInfo.setCollectionNumber(param.getCollectionNumber());
            collectionService.addReckoning(reckoningInfo);
            return ApiResponse.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
            return ApiResponse.error("添加收款信息异常");
        }
    }



    /**
     * @author ghb
     * @description  下载收款
     */
    @ResponseBody
    @ApiOperation(value = "下载收款",notes = "下载收款")
    @RequestMapping(value = "/downloadReckoning", method = RequestMethod.POST)
    @Log(title = "下载收款", content = "下载收款",businessType = BusinessType.OTHER)
    public ApiResponse downloadReckoning(@RequestBody ReckoningDto param) {
        log.info("下载收款，传来的参数为："+param);
        try {
            ReckoningInfo reckoningInfo = new ReckoningInfo();
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            reckoningInfo.setCreateBy(userId);
            reckoningInfo.setState(CollectionQuittanceEnum.COLLECTION_WAIT.getKey());
            reckoningInfo.setCarGroupId(param.getCarGroupId());
            reckoningInfo.setBeginDate(DateUtils.strToDate(param.getStartDate(),DateUtils.YYYY_MM_DD));
            reckoningInfo.setEndDate(DateUtils.strToDate(param.getEndDate(),DateUtils.YYYY_MM_DD));
            //reckoningInfo.setCollectionEndTime(DateUtils.strToDate(param.getOffDate(),DateUtils.YYYY_MM_DD));
            reckoningInfo.setCollectionId(param.getCollectionId());
            reckoningInfo.setCompanyId(param.getCompanyId());

            Map<String, Object> map = collectionService.downloadReckoning(reckoningInfo);
            return ApiResponse.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("下载收款异常");
        }
    }

    /**
     * @author ghb
     * @description  改变收款状态
     */

    @ResponseBody
    @ApiOperation(value = "改变收款状态",notes = "条件查询收款")
    @RequestMapping(value = "/updateReckoningStatus", method = RequestMethod.POST)
    @Log(title = "改变收款状态", content = "条件查询收款",businessType = BusinessType.OTHER)
    public ApiResponse updateReckoningStatus(@RequestBody ReckoningDto param, @RequestHeader String token, HttpServletRequest request) {
        log.info("条件查询收款，传来的参数为："+param);
        try {

            if(null != param && !StringUtils.isEmpty(param.getStatus()) && param.getCollectionNumber() > 0){
                collectionService.updateReckoningStatus(param);
                return ApiResponse.success("改变收款状态收款成功！");
            }else {
                return ApiResponse.success("改变收款状态缺失参数！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("改变收款状态异常");
        }

    }






    @ApiOperation(value = "收款详情",notes = "收款详情")
    @RequestMapping(value = "/reckoningDetail", method = RequestMethod.POST)
    @Log(title = "收款详情", content = "收款详情",businessType = BusinessType.OTHER)
    public ApiResponse<Map<String, Object>> reckoningDetail(@RequestBody ReckoningDto param) {
        log.info("条件查询收款，传来的参数为："+param);
        try {
           Map<String, Object> detailMap = collectionService.reckoningDetail(param);
           if(null != detailMap){
               return ApiResponse.success(detailMap);
           }else {
               return ApiResponse.error("收款详情异常");
           }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("收款详情异常");
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
            collectionService.findReckoning(param);
            return ApiResponse.success("条件查询收款成功");
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse.error("分页查询公告列表失败");
            return ApiResponse.error("添加收款信息异常");
        }

    }



}
