package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:18
 */
@RestController
@RequestMapping("/apply")
@Slf4j
public class ApplyContoller {

    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private IJourneyInfoService journeyInfoService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApplyApproveResultInfoService resultInfoService;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private EcmpMessageService ecmpMessageService;
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;



    /**
     * 员工提交行程申请     弃用。公务跟差旅申请分两个接口实现
     * @param  journeyCommitApplyDto  行程申请信息
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "applyCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyCommit")
    public ApiResponse   applyCommit(JourneyCommitApplyDto journeyCommitApplyDto){
        //提交行程申请
        applyInfoService.applyCommit(journeyCommitApplyDto);
        return ApiResponse.success();
    }

    /**
     * 员工提交公务行程申请
     * @param  officialCommitApply  行程申请信息
     * @param
     * @return
     */
    @Log("员工提交公务申请")
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:提交公务申请", businessType = BusinessType.INSERT)
    @ApiOperation(value = "applyOfficialCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyOfficialCommit")
    public ApiResponse<ApplyVO>   applyOfficialCommit(@RequestBody ApplyOfficialRequest officialCommitApply){
        //提交公务行程申请
        ApplyVO applyVO = null;
        try {
            applyVO = applyInfoService.applyOfficialCommit(officialCommitApply);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交公务申请失败");
        }
        return ApiResponse.success("提交申请成功",applyVO);
    }

    /**
     * 员工提交差旅行程申请
     * @param  travelCommitApply  行程申请信息
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:提交差旅申请", businessType = BusinessType.INSERT)
    @ApiOperation(value = "applyTravelCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyTravelCommit")
    public ApiResponse<ApplyVO>  applyTravelCommit(@RequestBody ApplyTravelRequest travelCommitApply){
        //提交差旅行程申请
        ApplyVO applyVO = null;
        try {
            applyVO = applyInfoService.applytravliCommit(travelCommitApply);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交差旅申请失败");
        }
        return ApiResponse.success("提交申请成功",applyVO);
    }

    /**
     * 获取行程申请 对应的审批流信息
     * @param journeyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfo",notes = "获取行程申请 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfo")
    public ApiResponse   getApplyApproveNodesInfo(JourneyApplyDto journeyApplyDto){
        if (ObjectUtils.isNotEmpty(journeyApplyDto)){
            JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
            return ApiResponse.success(journeyInfo);
        }
        return ApiResponse.error("获取行程申请对应的审批流信息异常");
    }

    /**
     * 获取用车制度 对应的审批流信息
     * @param regimeDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfoByRegimeInfo",notes = "获取用车制度 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfoByRegimeInfo")
    public ApiResponse   getApplyApproveNodesInfoByApproveTemplate(RegimeDto regimeDto){
        if (ObjectUtils.isNotEmpty(regimeDto)){
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(regimeDto.getRegimenId());
            return ApiResponse.success(applyInfo);
        }
        return ApiResponse.error("获取用车制度对应的审批流信息异常");
    }

    /**
     * 根据申请单查询审批流信息
     * @param journeyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApproveResult",notes = "根据申请单查询审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApproveResult")
    public ApiResponse <List<ApprovalListVO>> getApproveResult(JourneyApplyDto journeyApplyDto){
        ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
        if (applyInfo==null){
            return ApiResponse.error("此行程申请不存在");
        }
        EcmpUser ecmpUser = ecmpUserService.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
        List<ApprovalListVO> approveList = applyInfoService.getApproveList(ecmpUser.getNickName(), ecmpUser.getPhonenumber(), journeyApplyDto.getApplyId(),applyInfo.getCreateTime());
        return ApiResponse.success(approveList);
    }


    /**
     * 获取等待审批员 审批的 行程申请列表
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplies",notes = "获取等待当前用户审批的行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplies")
    public ApiResponse   getWaitApproveApplies(UserDto userDto){
        JourneyInfo journeyInfo = JourneyInfo.builder().userId(userDto.getUserId()).build();
        // PageHelper.startPage(userDto.get)
        List<JourneyInfo> journeyInfos = journeyInfoService.selectJourneyInfoList(journeyInfo);
        return ApiResponse.success("查询单前用户可审批行程单列表成功",journeyInfos);
    }

    /**
     * 获取等待审批员 审批的 行程申请详细信息
     * @param jouneyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplyDetailInfo",notes = "获取待审批的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplyDetailInfo")
    public ApiResponse   getWaitApproveApplyDetailInfo(JourneyApplyDto jouneyApplyDto){

        return null;
    }


    /**
     * 获取乘客自身 行程申请列表
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getPassengerOwnerApplies",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplies")
    public ApiResponse<PageResult<ApplyInfoDTO>>   getPassengerOwnerApplies(@RequestBody PageRequest applyPage){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //分页查询乘客申请列表
        PageResult<ApplyInfoDTO> applyInfoList = applyInfoService.selectApplyInfoListByPage(loginUser.getUser()
                .getUserId(),applyPage.getPageNum(),applyPage.getPageSize());
        return ApiResponse.success(applyInfoList);
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getPassengerOwnerAppliesDetailInfo",notes = "获取乘客自身的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerAppliesDetailInfo")
    public ApiResponse<ApplyDetailVO>   getPassengerOwnerAppliesDetailInfo(@RequestBody JourneyApplyDto journeyApplyDto){
        //根据applyId查询申请详情
        ApplyDetailVO  applyDetailVO = applyInfoService.selectApplyDetail(journeyApplyDto.getApplyId());
        if (applyDetailVO == null){
            return ApiResponse.error("查询申请详情异常");
        }
        return ApiResponse.success(applyDetailVO);
    }

    /**
     * 行程申请-审核通过
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "applyPass",notes = "行程申请-审核通过 ",httpMethod ="POST")
    @PostMapping("/applyPass")
    public ApiResponse applyPass(@RequestBody ApplyDTO journeyApplyDto){
        //1.校验信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoService.beforeInspect(journeyApplyDto, userId);
            if (CollectionUtils.isNotEmpty(applyApproveResultInfos)){
                resultInfoService.applyPass(journeyApplyDto,userId,applyApproveResultInfos);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("行程申请单审批通过");
    }

    /**
     * 行程申请-驳回
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "applyReject",notes = "行程申请-驳回",httpMethod ="POST")
    @PostMapping("/applyReject")
    public ApiResponse  applyReject(@RequestBody ApplyDTO journeyApplyDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoService.beforeInspect(journeyApplyDto, userId);
            if (CollectionUtils.isNotEmpty(applyApproveResultInfos)){
                resultInfoService.applyReject(journeyApplyDto,userId,applyApproveResultInfos);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("行程申请单审批驳回");

    }

    /**
     * 审批列表
     * @param approveInfoDTO
     * @return
     */
    @ApiOperation(value = "getApprovePage",notes = "审批列表 ",httpMethod ="POST")
    @PostMapping("/getApprovePage")
    public ApiResponse<PageResult<ApprovaReesultVO>> getApprovePage(@RequestBody ApproveInfoDTO approveInfoDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId=loginUser.getUser().getUserId();
        List<ApprovaReesultVO> page= applyInfoService.getApprovePage(approveInfoDTO.getPageIndex(),approveInfoDTO.getPageSize(),userId);
        Integer count=applyInfoService.getApprovePageCount(userId);
        return ApiResponse.success(new PageResult<ApprovaReesultVO>(Long.valueOf(count), page));
    }

    /**
     * 获取审批详情
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请/审批详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getTravelApproval",notes = "获取审批详情",httpMethod ="POST")
    @PostMapping("/getTravelApproval")
    public ApiResponse<TravelApprovalVO> getApplyApproveDetailInfo(@RequestBody ApplyDTO journeyApplyDto){
        TravelApprovalVO vo=new TravelApprovalVO();
        ApplyDetailVO  applyDetailVO = applyInfoService.selectApplyDetail(journeyApplyDto.getApplyId());
        //查询审批流信息
        //如果申请单无需审批则不展示审批流相关信息
        List<ApprovalListVO> approveList =applyInfoService.getApproveList(applyDetailVO.getApplyUser(), applyDetailVO.getApplyMobile(), journeyApplyDto.getApplyId(),applyDetailVO.getTime());
        vo.setApprovalVOS(approveList);
        vo.setApplyDetailVO(applyDetailVO);
        return ApiResponse.success(vo);
    }

    /**
     * 获取我的申请与审批个数
     * @return
     */
    @ApiOperation(value = "getApplyApproveCount",notes = "获取我的申请与审批个数",httpMethod ="POST")
    @PostMapping("/getApplyApproveCount")
    public ApiResponse<String> getApplyApproveCount(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        int count= applyInfoService.getApplyApproveCount(userId);
        return ApiResponse.success("查询成功",count+"");
    }

    /**
     * 校验用车制度的用车时间是否可用
     * @return
     */
    @ApiOperation(value = "checkUseCarTime",notes = "校验申请用车时间是否可用",httpMethod ="POST")
    @PostMapping("/checkUseCarTime")
    public ApiResponse<UseCarTimeVO> checkUseCarTime(@RequestBody RegimeCheckDto regimeDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            UseCarTimeVO useCarTimeVO = regimeInfoService.checkUseCarTime(regimeDto);
            if (useCarTimeVO==null){
                return ApiResponse.success();
            }else{
                if (CollectionUtils.isEmpty(useCarTimeVO.getUseTime())){
                    return ApiResponse.success();
                }else{
                    return ApiResponse.error("时间不可用",useCarTimeVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("网络异常");
        }
    }

    /**
     * 校验用车制度的用车城市是否可用
     * @return
     */
    @ApiOperation(value = "checkUseCarModeAndType",notes = "校验用车制度的用车城市是否可用",httpMethod ="POST")
    @PostMapping("/checkUseCarModeAndType")
    public ApiResponse<String> checkUseCarModeAndType(RegimeCheckDto regimeDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            if (CollectionUtils.isEmpty(regimeDto.getCityCodes())||regimeDto.getRegimeId()==null){
                return ApiResponse.error("参数为空");
            }
            List<UseCarTypeVO> list=regimeInfoService.checkUseCarModeAndType(regimeDto,loginUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success();
    }

    /**
     * 获取自由车开城城市//网约车开城城市
     * @return
     */
    @ApiOperation(value = "getUseCarType",notes = "校验用车制度的用车城市是否可用",httpMethod ="POST")
    @PostMapping("/getUseCarType")
    public ApiResponse<OnLineCarTypeVO> getUseCarType(RegimeCheckDto regimeDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            List<OnLineCarTypeVO> list=regimeInfoService.getUseCarType(regimeDto,loginUser.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success();
    }

}
