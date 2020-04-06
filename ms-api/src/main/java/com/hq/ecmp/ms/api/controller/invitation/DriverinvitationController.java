package com.hq.ecmp.ms.api.controller.invitation;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.InviteDto;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.DriverInvitationDTO;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.InvitationInfoDTO;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.vo.InvitationDriverVO;
import com.hq.ecmp.mscore.vo.RegisterDriverVO;
import com.hq.ecmp.mscore.vo.RegisterVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * 驾驶员邀请
 * @Author: ShiXin
 * @Date: 2020-3-16 12:00
 */
@RestController
@RequestMapping("/invDriver")
public class DriverinvitationController {
    @Autowired
    private EcmpEnterpriseInvitationInfoService ecmpEnterpriseInvitationInfoService;
    @Autowired
    private EcmpEnterpriseRegisterInfoService ecmpEnterpriseRegisterInfoServicee;
    @Autowired
    private IDriverInfoService iDriverInfoService;
    @Autowired
    private TokenService tokenService;



    /**
     * 生成邀请
     * @param driverInvitationDTO
     * @return
     */
    @ApiOperation(value = "interInvitationDriverCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInvitationDriverCommit")
    public ApiResponse interInvitationDriverCommit(@RequestBody DriverInvitationDTO driverInvitationDTO){
        try {

            ecmpEnterpriseInvitationInfoService.insertDriverInvitation(driverInvitationDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增驾驶员邀请失败");
        }
        return ApiResponse.success("新增驾驶员邀请成功");

    }

      /**
     * 邀请注册
     * @param ecmpEnterpriseRegisterInfo
     * @return
     */
    @ApiOperation(value = "interInviDriverZCCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInviDriverZCCommit")
    public ApiResponse interInviDriverZCCommit(@RequestBody EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo){
        try {

            //校验手机号的用户是否已经是企业用户
            int  itIsDriver = iDriverInfoService.driverItisExist(ecmpEnterpriseRegisterInfo.getMobile());

            if(itIsDriver > 0){
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
            return ApiResponse.error("驾驶员邀请注册失败");
        }
        return ApiResponse.success("驾驶员邀请注册成功");

    }
    /**
     * 邀请停用
     * @param
     */
    @ApiOperation(value = "updateInDriverStop",notes = "邀请停用",httpMethod = "POST")
    @PostMapping("/updateInDriverStop")
     public ApiResponse updateInDriverStop(@RequestBody InvitationDto invitationDto){
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
    @ApiOperation(value = "updateInDriverStart",notes = "邀请启用",httpMethod = "POST")
    @PostMapping("/updateInDriverStart")
    public ApiResponse updateInDriverStart(@RequestBody InvitationDto invitationDto){
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
     * 待审批数量
     * @param
     */
    @ApiOperation(value = "getregDriverWaitCount",notes = "驾驶员待审批数量",httpMethod = "POST")
    @PostMapping("/getregDriverWaitCount")
    public ApiResponse<RegisterVO>  getregDriverWaitCount(@RequestBody InviteDto inviteDto){
        RegisterVO registerVO =new RegisterVO();
        inviteDto.setType("T002");
        int waitCount =ecmpEnterpriseRegisterInfoServicee.waitAmount(inviteDto.getDeptId(),inviteDto.getType());
        registerVO.setRegisterCount(waitCount);
        return ApiResponse.success(registerVO);

    }

    /**
     * 待审批列表
     * @param
     */
    @ApiOperation(value = "getRegisterDriverList",notes = "驾驶员待审批列表",httpMethod = "POST")
    @PostMapping("/getRegisterDriverList")
    public ApiResponse <List<RegisterDriverVO>>getRegisterDriverList(@RequestBody RegisterDTO registerDTO){

        List<RegisterDriverVO> registerDriverVOlist = ecmpEnterpriseRegisterInfoServicee.queryRegisterDriverWait(registerDTO);
        if(CollectionUtils.isNotEmpty(registerDriverVOlist)){
            return ApiResponse.success(registerDriverVOlist);
        }else {
            return ApiResponse.error("未查询到待审批驾驶员列表");
        }
    }

    /**
     * 获取邀请列表-驾驶员
     */
    @ApiOperation(value = "getInvitationDriverList",notes = "驾驶员邀请列表",httpMethod = "POST")
    @PostMapping("/getInvitationDriverList")
    public ApiResponse<List<InvitationDriverVO>> getInvitationDriverList(@RequestBody InvitationInfoDTO invitationInfoDTO){

        List<InvitationDriverVO> invitationDriverVOList = ecmpEnterpriseInvitationInfoService.queryInvitationDriver(invitationInfoDTO);

        if(CollectionUtils.isNotEmpty(invitationDriverVOList)){
            return ApiResponse.success(invitationDriverVOList);
        }else {
            return ApiResponse.error("未查询到驾驶员邀请列表");
        }
    }
    /**
    * 获取邀请详情-驾驶员
    */
    @ApiOperation(value = "getInvitationDriverDetail",notes = "获取驾驶员邀请详情",httpMethod = "POST")
    @PostMapping("/getInvitationDriverDetail")
    public ApiResponse<InvitationDriverVO> getInvitationDriverDetail(@RequestBody InvitationDto invitationDto){

        InvitationDriverVO InvitationDriver=ecmpEnterpriseInvitationInfoService.queryInvitationDriverDetial(invitationDto.getInvitationId());
        return ApiResponse.success(InvitationDriver);

    }


}
