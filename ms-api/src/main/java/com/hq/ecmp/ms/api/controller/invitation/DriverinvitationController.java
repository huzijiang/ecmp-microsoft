package com.hq.ecmp.ms.api.controller.invitation;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.InvitionStateEnum;
import com.hq.ecmp.ms.api.dto.base.InviteDto;
import com.hq.ecmp.mscore.domain.DriverNatureInfo;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.DriverNatureInfoMapper;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    private static final Logger logger = LoggerFactory.getLogger(DriverinvitationController.class);

    @Autowired
    private EcmpEnterpriseInvitationInfoService ecmpEnterpriseInvitationInfoService;
    @Autowired
    private EcmpEnterpriseRegisterInfoService ecmpEnterpriseRegisterInfoService;
    @Autowired
    private IDriverInfoService iDriverInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DriverNatureInfoMapper driverNatureInfoMapper;



    /**
     * 生成邀请
     * @param driverInvitationDTO
     * @return
     */
    @Log(title = "邀请驾驶员模块",content = "生成邀请链接", businessType = BusinessType.OTHER)
    @ApiOperation(value = "interInvitationDriverCommit",notes = "生成邀请",httpMethod = "POST")
    @PostMapping("/interInvitationDriverCommit")
    public ApiResponse interInvitationDriverCommit(@RequestBody DriverInvitationDTO driverInvitationDTO){
        try {
            String urlApi=driverInvitationDTO.getApiUrl();
            //1.新增企业邀请信息表信息
            ecmpEnterpriseInvitationInfoService.insertDriverInvitation(driverInvitationDTO);
            Long invitationId = driverInvitationDTO.getInvitationId();
            System.out.println("新增邀请链接返回ID："+ invitationId);
            InvitationUrlVO invitationUrlVO = new InvitationUrlVO();
            String url=urlApi+"/inviteDriver/"+invitationId;
            invitationUrlVO.setUrl(urlApi+"/inviteDriver/"+invitationId);
            UserInvitationUrlDTO userUrl= new UserInvitationUrlDTO();
            userUrl.setUrl(url);
            userUrl.setInvitationId(invitationId);
            //2.拿到邀请id生成邀请链接后修改新邀请链接
            ecmpEnterpriseInvitationInfoService.updateInvitationUrl(userUrl);
            //3.插入邀请驾驶员性质表信息
            addDriverNatureInfo(invitationId,driverInvitationDTO.getDriverNature(),
                    driverInvitationDTO.getHireBeginTime(),
                    driverInvitationDTO.getHireBeginTime(),
                    driverInvitationDTO.getBorrowBeginTime(),
                    driverInvitationDTO.getBorrowEndTime());

            return ApiResponse.success(invitationUrlVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("新增驾驶员邀请失败");
        }


    }


    /***
     *
     * @param
     * @param driverNature
     * @param hireBeginTime
     * @param hireEndTime
     * @param borrowBeginTime
     * @param borrowEndTime
     * @return
     */
    private int addDriverNatureInfo(Long invitationId, String  driverNature, Date hireBeginTime,
                                    Date hireEndTime, Date borrowBeginTime, Date borrowEndTime){
        try{
            DriverNatureInfo driverNatureInfo = new DriverNatureInfo();
            driverNatureInfo.setInvitationId(invitationId);
            //生成邀请链接时候驾驶员还没生成，没有driver_id,只有邀请id
            driverNatureInfo.setDriverId(null);
            driverNatureInfo.setDriverNature(driverNature);
            driverNatureInfo.setHireBeginTime(hireBeginTime);
            driverNatureInfo.setHireEndTime(hireEndTime);
            driverNatureInfo.setBorrowBeginTime(borrowBeginTime);
            driverNatureInfo.setBorrowEndTime(borrowEndTime);
            return driverNatureInfoMapper.addDriverNatureInfo(driverNatureInfo);
        }catch(Exception e){
            logger.error("addDriverNatureInfo error",e);
        }
        return 0;
    }

      /**
     * 邀请注册
     * @param driverRegisterDTO
     * @return
     */
      @Log(title = "邀请驾驶员模块",content = "邀请注册", businessType = BusinessType.INSERT)
    @ApiOperation(value = "interInvitationDriverZCCommit",notes = "邀请注册",httpMethod = "POST")
    @PostMapping("/interInvitationDriverZCCommit")
    public ApiResponse interInvitationDriverZCCommit(DriverRegisterDTO driverRegisterDTO){
        try {

            //校验手机号的用户是否已经是该企业驾驶员
            int  itIsDriver = iDriverInfoService.driverItisExist(driverRegisterDTO);

           if(itIsDriver > 0){
                return ApiResponse.error("员工已经是该企业驾驶员");
            }
            //校验手机号是否已经申请注册，正在等待审批过程中
            String state="S000";
            int itIsReg = ecmpEnterpriseRegisterInfoService.itIsRegistration(driverRegisterDTO.getMobile(),state);
            if(itIsReg > 0){
                return ApiResponse.error("该手机号已申请完成，请等待审批");
            }
            ecmpEnterpriseRegisterInfoService.insertDriverRegister(driverRegisterDTO);
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
    @Log(title = "邀请驾驶员模块", content = "邀请停用",businessType = BusinessType.UPDATE)
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
    @Log(title = "邀请驾驶员模块",content = "邀请启用", businessType = BusinessType.UPDATE)
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
    @Log(title = "邀请驾驶员模块",content = "驾驶员待审批数量", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getregDriverWaitCount",notes = "驾驶员待审批数量",httpMethod = "POST")
    @PostMapping("/getregDriverWaitCount")
    public ApiResponse<RegisterVO>  getregDriverWaitCount(@RequestBody InviteDto inviteDto){
        RegisterVO registerVO =new RegisterVO();
        inviteDto.setType("T002");
        int waitCount =ecmpEnterpriseRegisterInfoService.waitAmount(inviteDto.getDeptId(),inviteDto.getType());
        registerVO.setRegisterCount(waitCount);
        return ApiResponse.success(registerVO);

    }

    /**
     * 待审批列表-驾驶员
     * @param
     */
    @Log(title = "邀请驾驶员模块",content = "驾驶员审批列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getRegisterDriverList",notes = "驾驶员待审批列表",httpMethod = "POST")
    @PostMapping("/getRegisterDriverList")
    public ApiResponse <PageResult<RegisterDriverVO>>getRegisterDriverList(@RequestBody  PageRequest pageRequest){

        PageResult<RegisterDriverVO>  registerDriverVOlist = ecmpEnterpriseRegisterInfoService.queryRegisterDriverWait(pageRequest.getPageNum(),
                pageRequest.getPageSize(),pageRequest.getCarGroupId(),pageRequest.getType(),pageRequest.getSearch());
        return ApiResponse.success(registerDriverVOlist);
    }
    /**
     * 驾驶员注册详情
     * @param
     */
    @Log(title = "邀请驾驶员模块",content = "驾驶员注册详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "driverRegDetail", notes = "驾驶员注册详情", httpMethod = "POST")
    @PostMapping("/driverRegDetail")
    public ApiResponse<RegisterDriverDetailVO> driverRegDetail(@RequestBody Long registerId) {
        return ApiResponse.success(ecmpEnterpriseRegisterInfoService.queryDriverRegDetail(registerId));
    }


    /**
     * 获取邀请列表-驾驶员
     */
    @Log(title = "邀请驾驶员模块",content = "驾驶员邀请列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationDriverList",notes = "驾驶员邀请列表",httpMethod = "POST")
    @PostMapping("/getInvitationDriverList")
    public ApiResponse<PageResult<InvitationDriverVO>> getInvitationDriverList(@RequestBody PageRequest PageRequest){
        PageResult<InvitationDriverVO> invitationDriverVOList = ecmpEnterpriseInvitationInfoService.queryInvitationDriver(PageRequest);
        return ApiResponse.success(invitationDriverVOList);
    }
    /**
    * 获取邀请详情-驾驶员
    */
    @Log(title = "邀请驾驶员模块",content = "获取驾驶员邀请详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationDriverDetail",notes = "获取驾驶员邀请详情",httpMethod = "POST")
    @PostMapping("/getInvitationDriverDetail")
    public ApiResponse<InvitationDriverVO> getInvitationDriverDetail(InvitationDto invitationDto){

        InvitationDriverVO InvitationDriver=ecmpEnterpriseInvitationInfoService.queryInvitationDriverDetial(invitationDto.getInvitationId());
        return ApiResponse.success(InvitationDriver);

    }

    /**
     * 获取邀请URL-驾驶员
     */
    @Log(title = "邀请驾驶员模块",content = "获取邀请URL", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getInvitationDriverUrl",notes = "获取邀请URL",httpMethod = "POST")
    @PostMapping("/getInvitationDriverUrl")
    public ApiResponse<InvitationUrlVO> getInvitationDriverUrl(@RequestBody InvitationDto invitationDto){

        InvitationUrlVO invitationUrlVO=ecmpEnterpriseInvitationInfoService.queryInvitationUserUrl(invitationDto.getInvitationId());
        return ApiResponse.success(invitationUrlVO);

    }
    /**
     * 删除邀请
     */
    @Log(title = "邀请驾驶员模块",content = "删除邀请", businessType = BusinessType.DELETE)
    @ApiOperation(value = "getInvitationDriverDel",notes = "删除邀请",httpMethod = "POST")
    @PostMapping("/getInvitationDriverDel")
    public ApiResponse  getInvitationDriverDel(@RequestBody InvitationDto invitationDto){

        try {
            ecmpEnterpriseInvitationInfoService.invitationDel(invitationDto.getInvitationId());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("删除失败");
        }
        return ApiResponse.success("删除邮箱成功"+invitationDto.getInvitationId());

    }
    /**
     * 邀请注册通过
     * @param
     */
    @Log(title = "邀请驾驶员模块",content = "邀请注册通过", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateRegisterDriverPast",notes = "邀请注册通过",httpMethod = "POST")
    @PostMapping("/updateRegisterDriverPast")
    public ApiResponse updateRegisterDriverPast(@RequestParam("registerId") Long registerId){
        try {
            //S000 申请中,S001 申请通过,S002 申请拒绝
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long companyId = loginUser.getUser().getOwnerCompany();
            int i=ecmpEnterpriseRegisterInfoService.updateRegisterDriverApprove(companyId,registerId,loginUser.getUser().getUserId(),null, InvitionStateEnum.APPROVEPASS.getKey());
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
    @Log(title = "邀请驾驶员模块",content = "申请拒绝", businessType = BusinessType.OTHER)
    @ApiOperation(value = "updateRegisterDriverRefuse",notes = "申请拒绝",httpMethod = "POST")
    @PostMapping("/updateRegisterDriverRefuse")
    public ApiResponse updateRegisterDriverRefuse(@RequestParam("reason") String reason,@RequestParam("registerId") Long registerId){
        try {
            //registerDTO.setState("S002");//S000 申请中,S001 申请通过,S002 申请拒绝
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            int i=ecmpEnterpriseRegisterInfoService.updateRegisterDriverApprove(null,registerId,loginUser.getUser().getUserId(),reason, InvitionStateEnum.APPROVEREJECT.getKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("申请拒绝失败");
        }
        return ApiResponse.success("申请拒绝成功");
    }


}
