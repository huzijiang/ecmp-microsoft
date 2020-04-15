package com.hq.ecmp.ms.api.controller.account;


import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.dto.EmailDTO;
import com.hq.ecmp.mscore.dto.EmailDeleteDTO;
import com.hq.ecmp.mscore.dto.EmailUpdateDTO;
import com.hq.ecmp.mscore.service.UserAcceptOrderAccountEmailInfoService;
import com.hq.ecmp.mscore.vo.EmailVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private TokenService tokenService;

    /**
     * 电子邮箱查询
     * @param
     * @return list
     */
    @Log(title = "财务模块:查询邮箱信息", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getEmailList",notes = "查询邮箱信息",httpMethod = "POST")
    @PostMapping("/getEmailList")
    public ApiResponse<List<EmailVO>> getEmailList(){
        //获取调用接口的用户信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();

        List<EmailVO> emailInfoList = userAcceptOrderAccountEmailInfoService.queryEmailByUserId(userId);

        return ApiResponse.success(emailInfoList);
    }
    /**
     * 电子邮箱新增
     * @param emailDTO
     * @return
     */
    @Log(title = "财务模块:新增邮箱信息", businessType = BusinessType.INSERT)
    @ApiOperation(value = "emailAddCommit",notes = "新增邮箱信息",httpMethod = "POST")
    @PostMapping("/emailAddCommit")
     public ApiResponse emailAddCommit(@RequestBody EmailDTO emailDTO){
         try {

             //获取调用接口的用户信息
             HttpServletRequest request = ServletUtils.getRequest();
             LoginUser loginUser = tokenService.getLoginUser(request);
             Long userId = loginUser.getUser().getUserId();
             emailDTO.setState("Y000");
             emailDTO.setUserId(userId);
             userAcceptOrderAccountEmailInfoService.insertEmail(emailDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("新增失败");
         }
         return ApiResponse.success("新增成功");

     }
    /**
     * 电子邮箱修改
     * @param emailUpdateDTO
     * @return
     */
     @Log(title = "财务模块:修改邮箱信息", businessType = BusinessType.UPDATE)
     @ApiOperation(value = "emailInfoUpdate",notes = "修改邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoUpdate")
     public ApiResponse emailInfoUpdate(@RequestBody EmailUpdateDTO emailUpdateDTO){

         try {
             //获取调用接口的用户信息
             HttpServletRequest request = ServletUtils.getRequest();
             LoginUser loginUser = tokenService.getLoginUser(request);
             Long userId = loginUser.getUser().getUserId();
             emailUpdateDTO.setUserId(userId);
             emailUpdateDTO.setState("Y000");
             userAcceptOrderAccountEmailInfoService.updateEmail(emailUpdateDTO);
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("修改失败");
         }
         return ApiResponse.success("修改成功");
     }
    /**
     * 电子邮箱删除
     * @param emailDeleteDTO
     * @return
     */
     @Log(title = "财务模块:删除邮箱信息", businessType = BusinessType.DELETE)
     @ApiOperation(value = "emailInfoDelete",notes = "删除邮箱信息",httpMethod = "POST")
     @PostMapping("/emailInfoDelete")
     public ApiResponse emailInfoDelete(@RequestBody EmailDeleteDTO emailDeleteDTO){
         try {
             System.out.println("发票ID"+emailDeleteDTO.getId());
             userAcceptOrderAccountEmailInfoService.deleteEmailById(emailDeleteDTO.getId());
         } catch (Exception e) {
             e.printStackTrace();
             return ApiResponse.error("删除失败");
         }
         return ApiResponse.success("删除邮箱成功"+emailDeleteDTO.getId());
     }
}
