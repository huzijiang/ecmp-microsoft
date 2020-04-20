package com.hq.ecmp.ms.api.controller.invitation;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.InvitionStateEnum;
import com.hq.ecmp.constant.InvitionTypeEnum;
import com.hq.ecmp.ms.api.dto.base.InviteDto;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * 员工邀请
 * @Author: ShiXin
 * @Date: 2020-3-16 12:00
 */
@RestController
@RequestMapping("/invitationUser")
public class UserinvitationController {
    @Autowired
    private EcmpEnterpriseInvitationInfoService ecmpEnterpriseInvitationInfoService;
    @Autowired
    private EcmpEnterpriseRegisterInfoService ecmpEnterpriseRegisterInfoServicee;
    @Autowired
    private IEcmpUserService iEcmpUserService;
    @Autowired
    private TokenService tokenService;


    /**
     * 生成邀请
     * @param userInvitationDTO
     * @return
     */
    @Log(title = "邀请员工模块",content = "生成邀请链接", businessType = BusinessType.OTHER)
    @ApiOperation(value = "interInvitationUserCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInvitationUserCommit")
    public ApiResponse<InvitationUrlVO> interInvitationUserCommit(@RequestBody UserInvitationDTO userInvitationDTO){
        try {
            String urlApi=userInvitationDTO.getApiUrl();
            //String urlApi="http://10.241.9.4:28080";
            ecmpEnterpriseInvitationInfoService.insertUserInvitation(userInvitationDTO);
            Long invitationId = userInvitationDTO.getInvitationId();
            System.out.println("新增邀请链接返回ID："+ invitationId);
            InvitationUrlVO invitationUrlVO = new InvitationUrlVO();
            String url=urlApi+"/invitePage/"+invitationId;
            invitationUrlVO.setUrl(urlApi+"/invitePage/"+invitationId);
            UserInvitationUrlDTO userUrl= new UserInvitationUrlDTO();
            userUrl.setUrl(url);
            userUrl.setInvitationId(invitationId);
            ecmpEnterpriseInvitationInfoService.updateInvitationUrl(userUrl);
            return ApiResponse.success(invitationUrlVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增员工邀请失败");
        }
    }

      /**
     * 邀请注册
     * @param userRegisterDTO
     * @return
     */
      @Log(title = "邀请员工模块",content = "邀请注册", businessType = BusinessType.INSERT)
    @ApiOperation(value = "interInvitationUserZcCommit",notes = "邀请注册",httpMethod = "POST")
    @PostMapping("/interRegisterInfoCommit")
    public ApiResponse interRegisterInfoCommit(UserRegisterDTO userRegisterDTO){
        try {

            //校验手机号的用户是否已经是企业用户
            int  itIsUser = iEcmpUserService.userItisExist(userRegisterDTO);
            if(itIsUser != 0){
                return ApiResponse.error("员工已经是该企业用户");
            }
            //校验手机号是否已经申请注册，正在等待审批过程中
            String state="S000";
            int itIsReg = ecmpEnterpriseRegisterInfoServicee.itIsRegistration(userRegisterDTO.getMobile(),state);
            if(itIsReg > 0){
                return ApiResponse.error("该手机号已申请完成，请等待审批");
            }
            ecmpEnterpriseRegisterInfoServicee.insertUserRegister(userRegisterDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("员工邀请注册失败");
        }
        return ApiResponse.success("员工邀请注册成功");

    }
    /**
     * 邀请停用
     * @param
     */
    @Log(title = "邀请员工模块",content = "邀请停用", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateInvatationStop",notes = "邀请停用",httpMethod = "POST")
    @PostMapping("/updateInvatationStop")
     public ApiResponse updateInvatationStop(@RequestBody InvitationDto invitationDto){
        try {
            invitationDto.setState("N000");//默认邀请状态为失效
            invitationDto.setUpdateTime(DateUtils.getNowDate());
            ecmpEnterpriseInvitationInfoService.updateInvitationState(invitationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("邀请停用失败");
        }
        return ApiResponse.success("邀请停用成功");
     }

    /**
     * 邀请启用
     * @param
     */
    @Log(title = "邀请员工模块",content = "邀请启用", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateInvatationStart",notes = "邀请启用",httpMethod = "POST")
    @PostMapping("/updateInvatationStart")
    public ApiResponse updateInvatationStart(@RequestBody InvitationDto invitationDto){
        try {
            invitationDto.setState("Y000");//默认邀请状态为失效
            invitationDto.setUpdateTime(DateUtils.getNowDate());
            ecmpEnterpriseInvitationInfoService.updateInvitationState(invitationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("邀请启用失败");
        }
        return ApiResponse.success("邀请启用成功");
    }

    /**
     * 邀请注册通过
     * @param
     */
    @Log(title = "邀请员工模块",content = "邀请注册通过", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateRegisterPast",notes = "邀请注册通过",httpMethod = "POST")
    @PostMapping("/updateRegisterPast")
    public ApiResponse updateRegisterPast(@RequestParam("registerId") Long registerId){
        try {
            //S000 申请中,S001 申请通过,S002 申请拒绝
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            int i=ecmpEnterpriseRegisterInfoServicee.updateRegisterApprove(registerId,loginUser.getUser().getUserId(),null, InvitionStateEnum.APPROVEPASS.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("审核通过");
    }
    /**
     * 邀请注册拒绝
     * @param
     */
    @Log(title = "邀请员工模块", content = "邀请注册拒绝",businessType = BusinessType.OTHER)
    @ApiOperation(value = "申请拒绝",notes = "申请拒绝",httpMethod = "POST")
    @PostMapping("/updateRegisterRefuse")
    public ApiResponse updateRegisterRefuse(@RequestParam("reason") String reason,@RequestParam("registerId") Long registerId){
        try {
            //registerDTO.setState("S002");//S000 申请中,S001 申请通过,S002 申请拒绝
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            int i=ecmpEnterpriseRegisterInfoServicee.updateRegisterApprove(registerId,loginUser.getUser().getUserId(),reason, InvitionStateEnum.APPROVEREJECT.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("申请拒绝失败");
        }
        return ApiResponse.success("申请拒绝成功");
    }
    /**
     * 待审批数量
     * @param
     */
    @Log(title = "邀请员工模块", content = "员工待审批数量",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getregisterUserWaitCount",notes = "员工待审批数量",httpMethod = "POST")
    @PostMapping("/getregisterUserWaitCount")
    public ApiResponse<RegisterVO> getregisterUserWaitCount(@RequestBody InviteDto inviteDto){
        RegisterVO registerVO =new RegisterVO();
        int waitCount =ecmpEnterpriseRegisterInfoServicee.waitAmount(inviteDto.getDeptId(),inviteDto.getType());
        registerVO.setRegisterCount(waitCount);
        if (InvitionTypeEnum.USER.getKey().equals(inviteDto.getType())){
            int resignationCount = iEcmpUserService.selectDimissionCount(inviteDto.getDeptId());
            registerVO.setResignationCount(resignationCount);
        }
        return ApiResponse.success(registerVO);
    }
    /**
     * 待审批列表
     * @param
     */
    @Log(title = "邀请员工模块",content = "员工审批列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getRegisterUserWaitList",notes = "员工待审批列表",httpMethod = "POST")
    @PostMapping("/getRegisterUserWaitList")
    public ApiResponse <PageResult<RegisterUserVO>>getRegisterUserWaitList(@RequestBody PageRequest pageRequest){
        PageResult<RegisterUserVO> registerUserVOlist = ecmpEnterpriseRegisterInfoServicee.queryRegisterUserWait(pageRequest);
        return ApiResponse.success(registerUserVOlist);
    }

    /**
     * 获取邀请列表-员工
     */
    @Log(title = "邀请员工模块",content = "获取员工邀请列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationUserList",notes = "获取员工邀请列表",httpMethod = "POST")
    @PostMapping("/getInvitationUserList")
    public ApiResponse<PageResult<InvitationUserVO>> getInvitationUserList(@RequestBody PageRequest PageRequest){
        PageResult<InvitationUserVO> invitationVOList = ecmpEnterpriseInvitationInfoService.queryInvitationUser(PageRequest);
        return ApiResponse.success(invitationVOList);
    }
    /**
    * 获取邀请详情-员工
    */
    @Log(title = "邀请员工模块",content = "获取员工邀请详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationUserDetail",notes = "获取员工邀请详情",httpMethod = "POST")
    @PostMapping("/getInvitationUserDetail")
    public ApiResponse<InvitationUserVO> getInvitationUserDetail(InvitationDto invitationDto){

        InvitationUserVO InvitationUser=ecmpEnterpriseInvitationInfoService.queryInvitationUserDetial(invitationDto.getInvitationId());
        return ApiResponse.success(InvitationUser);

    }
    /**
     * 获取邀请URL-员工
     */
    @Log(title = "邀请员工模块", content = "获取邀请URL",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationUserUrl",notes = "获取邀请URL",httpMethod = "POST")
    @PostMapping("/getInvitationUserUrl")
    public ApiResponse<InvitationUrlVO> getInvitationUserUrl(@RequestBody InvitationDto invitationDto){

        InvitationUrlVO invitationUrlVO=ecmpEnterpriseInvitationInfoService.queryInvitationUserUrl(invitationDto.getInvitationId());
        return ApiResponse.success(invitationUrlVO);

    }
    /**
     * 删除邀请
     */
    @Log(title = "邀请员工模块",content = "删除邀请", businessType = BusinessType.DELETE)
    @ApiOperation(value = "getInvitationUserDel",notes = "删除邀请",httpMethod = "POST")
    @PostMapping("/getInvitationUserDel")
    public ApiResponse  getInvitationUserDel(@RequestBody InvitationDto invitationDto){

        try {
            ecmpEnterpriseInvitationInfoService.invitationDel(invitationDto.getInvitationId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除失败");
        }
        return ApiResponse.success("删除邮箱成功"+invitationDto.getInvitationId());

    }



}
