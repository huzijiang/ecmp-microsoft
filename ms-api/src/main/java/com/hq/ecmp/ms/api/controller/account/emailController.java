package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.InvoiceAddress;
import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.mscore.service.IInvoiceAddressService;
import com.hq.ecmp.mscore.service.UserAcceptOrderAccountEmailInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 电子邮箱
 * @author shixin
 * @date 2020-3-11
 *
 */
 @RestController
 @RequestMapping("/email")
public class emailController {

    @Autowired
    private UserAcceptOrderAccountEmailInfoService userAcceptOrderAccountEmailInfoService;
    /**
     * 电子邮箱查询
     * @param userDto
     * @return list
     */
    @ApiOperation(value = "getEmailList",notes = "查询邮箱信息",httpMethod = "POST")
    @PostMapping("/getEmailList")
    public ApiResponse<List<UserAcceptOrderAccountEmailInfo>> getEmailList(UserDto userDto){
        List<UserAcceptOrderAccountEmailInfo> emailInfoList = userAcceptOrderAccountEmailInfoService.queryAllByUserId(userDto.getUserId());
        return ApiResponse.success(emailInfoList);
    }
    /**
     * 电子邮箱新增
     * @param userAcceptOrderAccountEmailInfo
     * @return
     */
    @ApiOperation(value = "emailAddCommit",notes = "新增邮箱信息",httpMethod = "POST")
    @PostMapping("/emailAddCommit")
     public ApiResponse emailAddCommit(@RequestBody UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo){
         try {
             userAcceptOrderAccountEmailInfoService.insert(userAcceptOrderAccountEmailInfo);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }
    /**
     * 电子邮箱修改
     * @param userAcceptOrderAccountEmailInfo
     * @return
     */
     @ApiOperation(value = "emailInfoUpdate",notes = "修改邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoUpdate")
     public ApiResponse emailInfoUpdate(@RequestBody UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo){

         try {
             userAcceptOrderAccountEmailInfoService.update(userAcceptOrderAccountEmailInfo);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 电子邮箱删除
     * @param userAcceptOrderAccountEmailInfo
     * @return
     */
     @ApiOperation(value = "emailInfoDelete",notes = "删除邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoDelete")
     public ApiResponse emailInfoDelete(@RequestBody UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo){
         try {
             userAcceptOrderAccountEmailInfoService.deleteById(userAcceptOrderAccountEmailInfo.getId());
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除成功");
     }
}
