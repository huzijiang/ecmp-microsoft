package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:18
 */
@RestController
@RequestMapping("/apply")
public class ApplyContoller {

    @Autowired
    private IApplyInfoService applyInfoService;

    /**
     * 员工提交行程申请
     * @param  journeyApplyDto  行程申请信息
     * @param  userDto    申请用户信息
     * @return
     */
    @ApiOperation(value = "applyCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyCommit")
    public ApiResponse   applyCommit(JourneyApplyDto journeyApplyDto, UserDto userDto){

        return null;
    }

    /**
     * 获取行程申请 对应的审批流信息
     * @param journeyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfo",notes = "获取行程申请 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfo")
    public ApiResponse   getApplyApproveNodesInfo(JourneyApplyDto journeyApplyDto){
        return null;
    }

    /**
     * 获取用车制度 对应的审批流信息
     * @param regimeDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfoByRegimeInfo",notes = "获取用车制度 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfoByRegimeInfo")
    public ApiResponse   getApplyApproveNodesInfoByApproveTemplate(RegimeDto regimeDto){

        return null;
    }


    /**
     * 获取等待审批员 审批的 行程申请列表
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplies",notes = "获取等待当前用户审批的行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplies")
    public ApiResponse<List<ApplyInfo>>   getWaitApproveApplies(UserDto userDto){

        return null;
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
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerApplies",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplies")
    public ApiResponse   getPassengerOwnerApplies(UserDto userDto){

        return null;
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerAppliesDetailInfo",notes = "获取乘客自身的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerAppliesDetailInfo")
    public ApiResponse   getPassengerOwnerAppliesDetailInfo(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 行程申请-审核通过
     * @param journeyApplyDto  审批员信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyPass",notes = "行程申请-审核通过 ",httpMethod ="POST")
    @PostMapping("/applyPass")
    public ApiResponse   applyPass(JourneyApplyDto journeyApplyDto,UserDto userDto){

        return null;
    }


    /**
     * 行程申请-驳回
     * @param journeyApplyDto  行程申请信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyReject",notes = "行程申请-驳回",httpMethod ="POST")
    @PostMapping("/applyReject")
    public ApiResponse   applyReject(JourneyApplyDto journeyApplyDto,UserDto userDto){

        return null;
    }


    /**
     * 获取行程申请的审批详情
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveDetailInfo",notes = "获取行程申请的审批详情",httpMethod ="POST")
    @PostMapping("/getApplyApproveDetailInfo")
    public ApiResponse   getApplyApproveDetailInfo(JourneyApplyDto journeyApplyDto){

        return null;
    }

}
