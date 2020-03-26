package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.service.UserAcceptOrderAccountEmailInfoService;
import com.hq.ecmp.mscore.vo.EmailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 电子邮箱
 * @author shixin
 * @date 2020-3-11
 *
 */
 @RestController
 @RequestMapping("/email")
public class EmailController {

    @Autowired
    private UserAcceptOrderAccountEmailInfoService userAcceptOrderAccountEmailInfoService;
    /**
     * 电子邮箱查询
     * @param
     * @return list
     */
    @ApiOperation(value = "getEmailList",notes = "查询邮箱信息",httpMethod = "POST")
    @PostMapping("/getEmailList")
    public ApiResponse<List<EmailVO>> getEmailList(Long userId){
        List<EmailVO> emailInfoList = userAcceptOrderAccountEmailInfoService.queryEmailByUserId(userId);
        return ApiResponse.success(emailInfoList);
    }
    /**
     * 电子邮箱新增
     * @param emailDTO
     * @return
     */
    @ApiOperation(value = "emailAddCommit",notes = "新增邮箱信息",httpMethod = "POST")
    @PostMapping("/emailAddCommit")
     public ApiResponse emailAddCommit(@RequestBody EmailDTO emailDTO){
         try {
             userAcceptOrderAccountEmailInfoService.insertEmail(emailDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }
    /**
     * 电子邮箱修改
     * @param emailDTO
     * @return
     */
     @ApiOperation(value = "emailInfoUpdate",notes = "修改邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoUpdate")
     public ApiResponse emailInfoUpdate(@RequestBody EmailDTO emailDTO){

         try {
             userAcceptOrderAccountEmailInfoService.updateEmail(emailDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 电子邮箱删除
     * @param Id
     * @return
     */
     @ApiOperation(value = "emailInfoDelete",notes = "删除邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoDelete")
     public ApiResponse emailInfoDelete(Long Id){
         try {
             System.out.println("发票ID"+Id);
             userAcceptOrderAccountEmailInfoService.deleteEmailById(Id);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除邮箱成功"+Id);
     }
}
