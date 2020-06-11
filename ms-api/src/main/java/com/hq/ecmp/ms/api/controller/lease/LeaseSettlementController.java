package com.hq.ecmp.ms.api.controller.lease;


import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.service.lease.LeaseSettlementService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/lease/settlement")
@Api(value = "后管租赁结算模块",tags = "后管租赁结算模块")
public class LeaseSettlementController {


    @Autowired
    private LeaseSettlementService leaseSettlementService;
    @Autowired
    private TokenService tokenService;

    /***
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "租赁普通用户列表查询")
    @PostMapping(value = "/getOrdinaryUserList")
    @Log(title = "普通用户租赁列表", content = "普通用户租赁列表", businessType = BusinessType.OTHER)
    public ApiResponse<PageResult<LeaseSettlementDto>> getOrdinaryUserList(@RequestBody LeaseSettlementDto data){
        try {
            return ApiResponse.success(leaseSettlementService.getOrdinaryUserList(data,tokenService.getLoginUser(ServletUtils.getRequest())));
        } catch (Exception e) {
            log.error("getOrdinaryUserList error",e);
        }
        return ApiResponse.error();

    }



    @ApiOperation(value = "普通用户租赁详情")
    @PostMapping(value = "/getOrdinaryUserDetail")
    @Log(title = "普通用户租赁详情", content = "普通用户租赁详情", businessType = BusinessType.OTHER)
    public ApiResponse getOrdinaryUserDetail(Long collectionId){
        try {
            return ApiResponse.success(leaseSettlementService.getOrdinaryUserDetail(collectionId));
        } catch (Exception e) {
            log.error("getOrdinaryUserDetail error",e);
        }
        return ApiResponse.error();

    }

    /***
     *
     * @param collectionId
     * @return
     */
    @ApiOperation(value = "租赁普通用户确认费用")
    @PostMapping(value = "/ordinaryUserConfirmCost")
    @Log(title = "租赁普通用户确认费用", content = "租赁普通用户确认费用", businessType = BusinessType.OTHER)
    public ApiResponse ordinaryUserConfirmCost(Long collectionId){
        try {
            return ApiResponse.success(leaseSettlementService.ordinaryUserConfirmCost(collectionId,tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId()));
        } catch (Exception e) {
            log.error("ordinaryUserConfirmCost error",e);
        }
        return ApiResponse.error();

    }

    /***
     *
     * @param collectionId
     * @return
     */
    @ApiOperation(value = "租赁普通用户下载pdf详情")
    @PostMapping(value = "/downloadOrdinaryUserDetail")
    @Log(title = "租赁普通用户下载pdf详情", content = "租赁普通用户下载pdf详情", businessType = BusinessType.OTHER)
    public ApiResponse downloadOrdinaryUserDetail(Long collectionId){
        try {
            leaseSettlementService.downloadOrdinaryUserDetail(collectionId);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("downloadOrdinaryUserDetail error",e);
        }
        return ApiResponse.error();
    }


    /***
     * 租赁结算用车单位列表
     * add by liuzb
     * @return
     */
    @ApiOperation(value = "租赁结算用车单位列表")
    @PostMapping(value = "/getUseCarUnit")
    @Log(title = "租赁结算用车单位列表", content = "租赁结算用车单位列表", businessType = BusinessType.OTHER)
    public ApiResponse getUseCarUnit(){
        try{
            return ApiResponse.success(leaseSettlementService.getUseCarUnit(tokenService.getLoginUser(ServletUtils.getRequest())));
        }catch(Exception e){
            log.error("getUseCarUnit error",e);
        }
        return ApiResponse.error();
    }

    @ApiOperation(value = "租赁结算调度员更改结算状态")
    @PostMapping(value = "/updateLeaseSettlementState")
    @Log(title = "租赁结算调度员更改结算状态", content = "租赁结算调度员更改结算状态", businessType = BusinessType.OTHER)
    public ApiResponse updateLeaseSettlementState(Long collectionId,String state){
        try{
            if(leaseSettlementService.updateLeaseSettlementState(collectionId,state,tokenService.getLoginUser(ServletUtils.getRequest()))>0){
                return ApiResponse.success();
            }
        }catch(Exception e){
            log.error("updateLeaseSettlementState error",e);
        }
        return ApiResponse.error();
    }


}
