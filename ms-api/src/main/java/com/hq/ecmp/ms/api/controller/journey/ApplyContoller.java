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
     * @param  jouneyApplyDto  行程申请信息
     * @param  userDto    申请用户信息
     * @return
     */
    @ApiOperation(value = "applyCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyCommit")
    public ApiResponse   applyCommit(JourneyApplyDto jouneyApplyDto, UserDto userDto){

        return null;
    }

    /**
     * 获取行程申请 对应的审批流信息
     * @param jouneyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproverNodesInfo",notes = "获取行程申请 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproverNodesInfo")
    public ApiResponse   getApplyApproverNodesInfo(JourneyApplyDto jouneyApplyDto){
        return null;
    }

    /**
     * 获取用车制度 对应的审批流信息
     * @param regimeDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproverNodesInfoByRegimeInfo",notes = "获取用车制度 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproverNodesInfoByRegimeInfo")
    public ApiResponse   getApplyApproverNodesInfoByApproveTemplate(RegimeDto regimeDto){

        return null;
    }


    /**
     * 获取等待审批员 审批的 行程申请列表
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplys",notes = "获取等待当前用户审批的行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplys")
    public ApiResponse<List<ApplyInfo>>   getWaitApproveApplys(UserDto userDto){

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
    @ApiOperation(value = "getPassengerOwnerApplys",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplys")
    public ApiResponse   getPassengerOwnerApplys(UserDto userDto){

        return null;
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param jouneyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerApplysDetailInfo",notes = "获取乘客自身的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplysDetailInfo")
    public ApiResponse   getPassengerOwnerApplysDetailInfo(JourneyApplyDto jouneyApplyDto){

        return null;
    }

    /**
     * 行程申请-审核通过
     * @param jouneyApplyDto  审批员信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyPass",notes = "行程申请-审核通过 ",httpMethod ="POST")
    @PostMapping("/applyPass")
    public ApiResponse   applyPass(JourneyApplyDto jouneyApplyDto,UserDto userDto){

        return null;
    }


    /**
     * 行程申请-驳回
     * @param jouneyApplyDto  行程申请信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyReject",notes = "行程申请-驳回",httpMethod ="POST")
    @PostMapping("/applyReject")
    public ApiResponse   applyReject(JourneyApplyDto jouneyApplyDto,UserDto userDto){

        return null;
    }


    /**
     * 获取行程申请的审批详情
     * @param jouneyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveDetailInfo",notes = "获取行程申请的审批详情",httpMethod ="POST")
    @PostMapping("/getApplyApproveDetailInfo")
    public ApiResponse   getApplyApproveDetailInfo(JourneyApplyDto jouneyApplyDto){

        return null;
    }

}
