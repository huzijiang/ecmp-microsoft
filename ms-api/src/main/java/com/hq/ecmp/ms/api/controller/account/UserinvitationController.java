package com.hq.ecmp.ms.api.controller.account;

import com.hq.common.core.api.ApiResponse;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.InvitationDto;

import com.hq.ecmp.mscore.dto.InvitationInfoDTO;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;

import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.InvitationUserVO;
import com.hq.ecmp.mscore.vo.registerUserVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    /**
     * 生成邀请
     * @param ecmpEnterpriseInvitationInfo
     * @return
     */
    @ApiOperation(value = "interInvitationInfoCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInvitationInfoCommit")
    public ApiResponse interInvitationInfoCommit(@RequestBody EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo){
        try {
            ecmpEnterpriseInvitationInfo.setType("T001");//员工邀请
            ecmpEnterpriseInvitationInfo.setState("Y000");//默认邀请状态为有效
            ecmpEnterpriseInvitationInfoService.insert(ecmpEnterpriseInvitationInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增员工邀请失败");
        }
        return ApiResponse.success("新增员工邀请成功");

    }

      /**
     * 邀请注册
     * @param ecmpEnterpriseRegisterInfo
     * @return
     */
    @ApiOperation(value = "interInvitationInfoCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInvitationInfoCommit")
    public ApiResponse interRegisterInfoCommit(@RequestBody EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo){
        try {

            //校验手机号的用户是否已经是企业用户
            int  itIsUser = iEcmpUserService.userItisExist(ecmpEnterpriseRegisterInfo.getMobile());
            if(itIsUser > 0){
                return ApiResponse.error("员工已经是该企业用户");
            }
            //校验手机号是否已经申请注册，正在等待审批过程中
            String state="S000";
            int itIsReg = ecmpEnterpriseRegisterInfoServicee.itIsRegistration(ecmpEnterpriseRegisterInfo.getMobile(),state);
            if(itIsReg > 0){
                return ApiResponse.error("该手机号已申请完成，请等待审批");
            }
            ecmpEnterpriseRegisterInfoServicee.insert(ecmpEnterpriseRegisterInfo);
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
    @ApiOperation(value = "updateInvatationStop",notes = "邀请停用",httpMethod = "POST")
    @PostMapping("/updateInvatationStop")
     public ApiResponse updateInvatationStop(@RequestBody InvitationDto invitationDto){
        try {
            invitationDto.setState("N000");//默认邀请状态为失效
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
    @ApiOperation(value = "updateInvatationStart",notes = "邀请启用",httpMethod = "POST")
    @PostMapping("/updateInvatationStart")
    public ApiResponse updateInvatationStart(@RequestBody InvitationDto invitationDto){
        try {
            invitationDto.setState("Y000");//默认邀请状态为失效
            ecmpEnterpriseInvitationInfoService.updateInvitationState(invitationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("邀请启用失败");
        }
        return ApiResponse.success("邀请启用成功");
    }

    /**
     * 邀请申请通过
     * @param
     */
    /**
     * 邀请申请拒绝
     * @param
     */
    /**
     * 待审批数量
     * @param
     */
    @ApiOperation(value = "getregisterUserWaitCount",notes = "员工待审批数量",httpMethod = "POST")
    @PostMapping("/getregisterUserWaitCount")
    public ApiResponse getregisterUserWaitCount(@RequestBody RegisterDTO registerDTO){
        registerDTO.setState("S000");
        registerDTO.setType("T001");
        int i =ecmpEnterpriseRegisterInfoServicee.waitAmount(registerDTO);
        if(i>0){
            return ApiResponse.success(i);
        }
        return ApiResponse.error("待审批用户数量为0");
    }
    /**
     * 待审批列表
     * @param
     */
    @ApiOperation(value = "getRegisterUserWaitList",notes = "员工待审批列表",httpMethod = "POST")
    @PostMapping("/getRegisterUserWaitList")
    public ApiResponse <List<registerUserVO>>getRegisterUserWaitList(@RequestBody RegisterDTO registerDTO){
        List<registerUserVO> registerUserVOlist = ecmpEnterpriseRegisterInfoServicee.queryRegisterUserWait(registerDTO);
        if(CollectionUtils.isNotEmpty(registerUserVOlist)){
            return ApiResponse.success(registerUserVOlist);
        }else {
            return ApiResponse.error("未查询到待审批员工列表");
        }
    }

    /**
     * 获取邀请列表-员工
     */
    @ApiOperation(value = "getInvitationUserList",notes = "获取员工邀请列表",httpMethod = "POST")
    @PostMapping("/getInvitationUserList")
    public ApiResponse<List<InvitationUserVO>> getInvitationUserList(@RequestBody InvitationInfoDTO invitationInfoDTO){
        List<InvitationUserVO> invitationVOList = ecmpEnterpriseInvitationInfoService.queryInvitationUser(invitationInfoDTO);
        if(CollectionUtils.isNotEmpty(invitationVOList)){
            return ApiResponse.success(invitationVOList);
        }else {
            return ApiResponse.error("未查询到员工邀请列表");
        }
    }
    /**
    * 获取邀请详情-员工
    */
    @ApiOperation(value = "getInvitationUserDetail",notes = "获取员工邀请详情",httpMethod = "POST")
    @PostMapping("/getInvitationUserDetail")
    public ApiResponse<InvitationUserVO> getInvitationUserDetail(@RequestBody String invitationId){

        InvitationUserVO InvitationUser=ecmpEnterpriseInvitationInfoService.queryInvitationUserDetial(invitationId);
        return ApiResponse.success(InvitationUser);

    }


}
